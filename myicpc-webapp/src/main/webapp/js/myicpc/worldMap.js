var globalMapController,
    MapConfig = {
        centered: null,
        config: null,
        projection: null,
        feature: null,
        // team coordinates
        teamCoorData: null,
        // teams with coordinates
        teamsWithCoor: new Array(),
        //
        circleData: new Array(),
        //circle size
        circleSize: 5,
        teamCount: 0,
        //settings for color interpolation
        fullColor: 255,
        darkColor: 100,

        refreshAnimationLength: 750,
        // settings for color legend
        legendWidth: 1000, // width of the legend
        legendYTextBottomCorner: 200, // Y coor. of bottom corner of title text
        legendYSpaceTitleBars: 5, // space between title and top corner or bars
        legendYSpaceBottomTicks: 15, // space between bottom corner of bars and
        // bottom corner of labels
        legendXCorner: 10, // X coordinate of legend
        legendBarHeight: 10, // height of bars in legend
        legendLabelFrequency: 10 // how often should the text label appear
    };

updateTeamOnMap = function (team, solved) {
    solved = solved || false;
    if (MapConfig.teamCoorData) {
        var coor = MapConfig.teamCoorData[team["teamExternalId"]];

        if (coor) {
            team.longtitude = coor.longtitude;
            team.latitude = coor.latitude;
        } else {
            team.longtitude = 0;
            team.latitude = 0;
        }
    }

    d3.select("#map-team-" + team["teamExternalId"]).data([team, ])
        .style("position", "absolute")
        .style("top", coorTop)
        .style("left", coorLeft);

    // special animation if team moved up in rank
    if (solved) {
        showMapPopover({'id': team["teamExternalId"]}, globalMapController);
        d3.select("#map-circle-" + team["teamExternalId"]).transition().duration(500).style("fill", textRankColor(team.rank)).attr("r", 2 * MapConfig.circleSize).transition()
            .duration(2500).transition().duration(500).attr("r", MapConfig.circleSize).each("end", function () {
                hideMapPopover();
            });

    } else {
        // otherwise just change color by new rank
        d3.select("#map-circle-" + team["teamExternalId"]).transition().duration(500).style("fill", textRankColor(team.rank)).attr("r", MapConfig.circleSize);
    }
};

update = function (teams) {
    for (var key in teams) {
        updateTeamOnMap(teams[key]);
    }
};

renderMap = function (appPath, conf, mapController) {
    globalMapController = mapController;
    MapConfig.teamCount = mapController.teams.length;
    conf = $.extend({
        width: 1000,
        ratio: 0.65,
        scale: 150,
        translate: [ 490, 380 ],
        circleSize: 6
    }, conf);

    MapConfig.config = conf;

    width = conf.width;
    MapConfig.legendWidth = width - 20;
    height = Math.round(width * conf.ratio);
    MapConfig.legendYTextBottomCorner = height - 40;
    MapConfig.circleSize = conf.circleSize;

    var currentScale = conf.scale,
        currentTranslate = conf.translate,
    // create map svg
        svg = d3.select("#mapContainer").append("svg:svg").attr("width", width).attr("height", height);

    // create projection
    MapConfig.projection = d3.geo.mercator().scale(currentScale).translate(currentTranslate);

    // create object for GPS coordinate conversions using projection
    path = d3.geo.path().projection(MapConfig.projection);


    d3.json(appPath + "/static/maps/world-countries.json", function (collection) {
        MapConfig.feature = svg.selectAll("path").data(collection.features).enter().append("svg:path").attr("d", clip).attr("id", function (d) {
            return d.id;
        }).on("click", countryClicked);
        MapConfig.feature.append("svg:title").text(function (d) {
            return d.properties.name;
        });

        // download data for universities (circles on maps)
        $.getJSON(appPath + "/static/maps/universities.json", function (data) {
            MapConfig.teamCoorData = data;
            for (i in data) {
                var c = {
                    "id": i,
                    "x": data[i].longtitude,
                    "y": data[i].latitude,
                    "country": data[i].country,
                };
                MapConfig.circleData.push(c);
            }
            // circle data dowloaded though ajax (in Template created) as well,
            // reactivity will call this again
            // but again, this must be drawn on enter to prevent multiple copies
            universities = svg.selectAll("circle").data(MapConfig.circleData);
            universities.enter().append("circle").attr("cx", function (d) {
                return computeCoord(d.x, d.y)[0];
            }).attr("cy", function (d) {
                return computeCoord(d.x, d.y)[1];
            }).attr("id", function (d) {
                return "map-circle-" + d.id;
            }).attr("r", MapConfig.circleSize)
                .on("click", function (d) {
                    clickCircle(d);
                    $("#team_" + d.id).click();
                    d3.event.stopPropagation();
                })
                .on("mouseover", function (d) {
                    showMapPopover(d, mapController);
                    $("#map-circle-" + d.id).attr({"r": 2 * MapConfig.circleSize});
                    $("#team_" + d.id).addClass("hightlightedTeam");

                })
                .on("mouseout", function (d) {
                    hideMapPopover();
                    $("#map-circle-" + d.id).attr({"r": MapConfig.circleSize});
                    $("#team_" + d.id).removeClass("hightlightedTeam");
                });

            update(mapController.teams);

            // draw legend bar
            createLegendBar(svg);
        });

    });

    function clickCircle(d) {
        for (var i = 0; i < MapConfig.feature[0].length; i++) {
            if (MapConfig.feature[0][i].id === d.country) {
                countryClicked(MapConfig.feature[0][i].__data__);
                $("#team_" + d.id).mouseenter();
                return;
            }
        }
    }

    function countryClicked(d) {
        if (d && MapConfig.centered !== d) {
            MapConfig.centered = d;
        } else {
            MapConfig.centered = null;
            return null;
        }
        svg.selectAll("path").classed("activeContinent", MapConfig.centered && function (d) {
            return d.properties.continent === MapConfig.centered.properties.continent;
        });

        mapController.decodeContinentCode(MapConfig.centered.properties.continent);

        mapController.$apply();
    }

};

//Helper function
// Sets map scaling (remember and set for projection)
function setScale(scale) {
    var currentScale = scale;
    MapConfig.projection.scale(currentScale);

}

// Helper function
// Sets map coordinate translation (remember and set for projection)
function setTranslate(trans) {
    var currentTranslate = trans;
    MapConfig.projection.translate(currentTranslate);
}

//Called when need to redraw map (after zoom, movement, etc.)
// Recomputes coordinates of all map elements using chosen projection
// and animates map zoom/movement
function refresh(duration) {
    (duration ? MapConfig.feature.transition().duration(duration) : MapConfig.feature).attr("d", clip);
    (duration ? universities.transition().duration(duration) : universities)
        .attr("cx", function (d) {
            return computeCoord(d.x, d.y)[0];
        })
        .attr("cy", function (d) {
            return computeCoord(d.x, d.y)[1];
        })
        .attr("r", MapConfig.config.circleSize);

    for (i in MapConfig.teamsWithCoor) {
        t = MapConfig.teamsWithCoor[i];
        (duration ? t.transition().duration(duration) : t)
            .style("top", coorTop)
            .style("left", coorLeft);
    }
}

function zoomToWorld() {
    setScale(MapConfig.config.scale);
    setTranslate(MapConfig.config.translate);
    refresh(MapConfig.refreshAnimationLength);
}

function zoomToArea(areaId) {
    d3.select("#mapContainer").selectAll("path").classed("activeContinent", function (d) {
        return d.properties.continent === areaId;
    });
    setScale(MapConfig.config.areas[areaId].scale);
    setTranslate(MapConfig.config.areas[areaId].translate);
    refresh(MapConfig.refreshAnimationLength);

    var mapController = angular.element($("#mapContent")).scope();
    mapController.decodeContinentCode(areaId);
    mapController.$apply();
}


//Draws color legend of the team rank
//Uses textRankColor and rankColor for color interpolation
function createLegendBar(svg) {
    var barSpace = MapConfig.legendWidth / MapConfig.teamCount, i;
    svg.append("text").attr("text-anchor", "start").attr("y", MapConfig.legendYTextBottomCorner).attr("x", MapConfig.legendXCorner).text("Team rank legend");
    for (i = 0; i < MapConfig.teamCount; i++) {
        svg.append("rect").attr("x", i * barSpace + MapConfig.legendXCorner).attr("y", MapConfig.legendYTextBottomCorner + MapConfig.legendYSpaceTitleBars).attr("width",
                barSpace - 1).attr("height", MapConfig.legendBarHeight).style("fill", textRankColor(i)).style("z-index", 99);
        if ((i + 1) % MapConfig.legendLabelFrequency == 0 || (i + 1) == 1) {
            svg.append("text").attr("text-anchor", "middle").attr("y",
                    MapConfig.legendYTextBottomCorner + MapConfig.legendYSpaceTitleBars + MapConfig.legendBarHeight + MapConfig.legendYSpaceBottomTicks).attr("x",
                    i * barSpace + MapConfig.legendXCorner + (barSpace / 2)).text(i + 1);
        }
    }
}

/*
 * Color interpolation Used to compute color from team rank From dark green
 * (rank 1), through yellow to red (last rank)
 */
rankColor = function (rank) {
    var boxSize = MapConfig.teamCount / 2,
        box = Math.floor(rank / boxSize),
        boxPos = (rank) % boxSize,
        rankInc = MapConfig.fullColor / boxSize,
        secInc = (MapConfig.fullColor - MapConfig.darkColor) / boxSize,
        r = g = b = 0,
        colorChange = Math.round(rankInc * boxPos),
        colorChange2 = Math.round(secInc * boxPos);
    if (box == 0) {
        r = 0 + colorChange;
        g = MapConfig.darkColor + colorChange2;
        b = 0;
    }
    if (box == 1) {
        r = MapConfig.fullColor;
        g = MapConfig.fullColor - colorChange;
        b = 0;
    }
    return [ r, g, b ];
};

//Helper function for color legend drawing
//Converts rank via rankColor interpolation function into text representation
//required by CSS
function textRankColor(rank) {
    var c = rankColor(rank);
    return "rgb(" + c[0] + "," + c[1] + "," + c[2] + ")";
};

//Helper functions for team drawing
coorTop = function (d) {
    return (parseFloat(computeCoord(d.longtitude, d.latitude)[1]) - 10) + "px";
};
coorLeft = function (d) {
    return (parseFloat(computeCoord(d.longtitude, d.latitude)[0]) + 15) + "px";
};
coorTopText = function (d) {
    return (parseFloat(computeCoord(d.longtitude, d.latitude)[1]) + 6) + "px";
};
coorLeftText = function (d) {
    return (parseFloat(computeCoord(d.longtitude, d.latitude)[0])) + "px";
};

//Clears selection 
function clearSelection() {
    d3.selectAll("#mapContainer .activeContinent").classed("activeContinent", false);
    d3.selectAll("#mapContainer .active").classed("active", false);
    ngController = angular.element($("#mapContent")).scope();
    ngController.reset();
    ngController.$apply();
    zoomToWorld();
    MapConfig.centered = null;
}
// Shows map popover
function showMapPopover(d, mapController) {
    $("#mapPopoverPanel").removeClass("hidden");
    $("#mapPopoverPanel").position({
        my: "center top+18",
        at: "center bottom",
        of: "#map-circle-" + d.id,
        collision: "fit"
    });
    mapController.showMapPopover(d.id);
}
// Hides map popver
function hideMapPopover() {
    $("#mapPopoverPanel").addClass("hidden");
}
// computes map coordinates on the map
function computeCoord(x, y) {
    var i = path({
        "type": "Feature",
        "geometry": {
            "type": "Polygon",
            "coordinates": [
                [
                    [ x, y ],
                    [ x, y ]
                ]
            ]
        }
    }, 1);
    return i.substring(1, i.length - 1).split(",");
};

function clip(d) {
    return path(d);
};

/**
 * AngularJS controller
 * @param $scope
 */
function MapController($scope) {
    /**
     * Selected country
     */
    $scope.selectedCountry = {name: "", code: null};
    /**
     * Selected region
     */
    $scope.selectedContinent = null;
    $scope.continents = {'EU': {'name': 'Europe', 'id': 'Europe'}, 'NA': {'name': "North America", 'id': "North America"}, 'AS': {'name': "Asia", 'id': "Asia"}, 'SA': {'name': "Latin America", 'id': "Latin America"}, 'AF': {'name': "Africa &  M. East", 'id': "Africa and the Middle East"}, 'AU': {'name': "South Pacific", 'id': "South Pacific"}};

    /**
     * Default settings
     */
    $scope.defaultSelectedOption = {code: "select", name: "Select"};
    /**
     * Selected option
     */
    $scope.selectedOption = {code: "scoreboard", name: "Scoreboard"};
    /**
     * View options
     */
    $scope.options = [
        {code: "feed", name: "Feed"},
        {code: "scoreboard", name: "Scoreboard"}
    ];

    /**
     * Teams
     */
    $scope.teams = [];
    /**
     * Problems
     */
    $scope.problems = [];
    $scope.teamIdMap = null;

    /**
     * Deselect country
     */
    $scope.resetCountry = function () {
        $scope.selectedCountry = {name: "", code: null};
    };

    /**
     * Deselect coutry and region
     */
    $scope.reset = function () {
        $scope.selectedCountry = {name: "", code: null};
        $scope.selectedContinent = null;
    };

    /**
     * Find team by ID
     */
    $scope.getTeamById = function (teamId) {
        obj = _.find($scope.teams, function (obj) {
            return obj.teamId === teamId;
        });
        if (typeof obj === "undefined") {
            return;
        }
        return obj;
    };

    /**
     * Proccess view option change
     */
    $scope.changeOption = function (code) {
        for (var i = 0; i < $scope.options.length; i++) {
            if (code === $scope.options[i].code) {
                $scope.selectedOption = $scope.options[i];
                return;
            }
        }
        $scope.selectedOption = $scope.defaultSelectedOption;
    };

    /**
     * Show team detailed popover panel
     */
    $scope.showMapPopover = function (externalTeamId) {
        var team = _.find($scope.teams, function (obj) {
            return obj.teamExternalId == externalTeamId;
        });
        if (typeof team === "undefined") {
            return;
        }
        $("#mapPopoverPanel .panel-heading").html('<strong>' + team.rank + '</strong> ' + team.teamName);
        $("#mapPopoverPanel .panel-body").html($scope.popoverBody(team, $("#mapPopoverPanel .panel-body").width() + "px"));
    };

    /**
     * Decode region based on region code
     */
    $scope.decodeContinentCode = function (code) {
        $scope.selectedContinent = $scope.continents[code];
    };

    /**
     * Highlights team point on the map
     */
    $scope.highlightPoint = function (team) {
        $("#map-circle-" + team.teamExternalId).attr({"r": 2 * MapConfig.circleSize});
        $("#mapContainer svg").append($("#map-circle-" + team.teamExternalId));

        $("#mapPopoverPanel").removeClass("hidden");
        $("#mapPopoverPanel").position({
            my: "center top+23",
            at: "center bottom",
            of: "#map-circle-" + team.teamExternalId,
            collision: "fit"
        });
        $scope.showMapPopover(team.teamExternalId);
    };

    /**
     * Dehighlights team point on the map
     */
    $scope.highlightPointOut = function (team) {
        $("#map-circle-" + team.teamExternalId).attr({"r": MapConfig.circleSize});
        $("#mapPopoverPanel").addClass("hidden");
    };

    /**
     * Create a body for popover panel
     */
    $scope.popoverBody = function (team, width) {

        var html, tp, i;
        html = '<table style="width:' + width + '">';
        html += '<tbody>';
        html += '<tr>';
        for (i = 0; i < $scope.problems.length; i++) {
            if ((i) % 4 == 0) {
                html += '</tr><tr>';
            }
            tp = team.teamProblems[$scope.problems[i].id];
            if (typeof tp === "undefined") {
                html += '<td class="popoverCell"><span class="label label-shadow">' + $scope.problems[i].code + '</span></td>';
            } else if (tp.judged) {
                if (tp.solved) {
                    html += '<td class="popoverCell"><span class="label label-success">' + tp.attempts + ' - ' + convertSecondsToMinutes(tp.time) + '</span></td>';
                } else {
                    html += '<td class="popoverCell"><span class="label label-danger">' + tp.attempts + '</span></td>';
                }
            }
        }
        html += '</tr>';
        html += '<tbody>';
        html += '</table>';
        return html;
    };

    /**
     * Set teams
     */
    $scope.setTeams = function (teams) {
        $scope.teams = teams;
        $scope.$apply();
    };

    /**
     * Set problems
     */
    $scope.setProblems = function (problems) {
        $scope.problems = problems;
        $scope.$apply();
    };

    /**
     * Update rank for a team
     */
    $scope.updateRank = function (teamId, rank) {
        obj = _.find($scope.teams, function (obj) {
            return obj.teamId === teamId;
        });
        if (typeof obj === "undefined") {
            return;
        }
        obj.rank = rank;
    };

    /**
     * Update number of solved problems and total time for a team
     */
    $scope.updateNumSolvedAndTotalTime = function (teamId, numSolved, totalTime) {
        obj = _.find($scope.teams, function (obj) {
            return obj.teamId === teamId;
        });
        if (typeof obj === "undefined") {
            return;
        }
        obj.nSolved = numSolved;
        obj.totalTime = totalTime;
    };

    /**
     * Update team problem for a team
     */
    $scope.updateTeamProblem = function (teamId, problemId, judged, solved, attempts, time, first) {
        team = _.find($scope.teams, function (obj) {
            return obj.teamId === teamId;
        });
        if (typeof team === "undefined" || typeof team.teamProblems === "undefined") {
            return;
        }
        if (typeof team.teamProblems[problemId] === "undefined") {
            team.teamProblems[problemId] = {};
        }
        team.teamProblems[problemId].judged = judged;
        team.teamProblems[problemId].solved = solved;
        team.teamProblems[problemId].attempts = attempts;
        team.teamProblems[problemId].time = time;
        team.teamProblems[problemId].first = first;
    };
}