// Generated by CoffeeScript 1.8.0
var scorebar, updateWorldMap;

scorebar = angular.module('worldMap', []);

scorebar.controller('worldMapCtrl', function($scope) {
  $scope.teams = [];
  $scope.problems = [];
  $scope.config = {
    centered: null,
    config: null,
    projection: null,
    feature: null,
    isCountryBased: false,
    teamCoorData: null,
    teamsWithCoor: new Array(),
    circleData: new Array(),
    circleSize: 5,
    teamCount: 0,
    fullColor: 255,
    darkColor: 100,
    refreshAnimationLength: 750,
    legendWidth: 1000,
    legendYTextBottomCorner: 200,
    legendYSpaceTitleBars: 5,
    legendYSpaceBottomTicks: 15,
    legendXCorner: 10,
    legendBarHeight: 10,
    legendLabelFrequency: 10
  };
  $scope.continents = {
    'EU': {
      'name': 'Europe',
      'id': 'Europe'
    },
    'NA': {
      'name': "North America",
      'id': "North America"
    },
    'AS': {
      'name': "Asia",
      'id': "Asia"
    },
    'SA': {
      'name': "Latin America",
      'id': "Latin America"
    },
    'AF': {
      'name': "Africa &  M. East",
      'id': "Africa and the Middle East"
    },
    'AU': {
      'name': "South Pacific",
      'id': "South Pacific"
    }
  };
  $scope.init = function(teams, problems) {
    return $scope.$apply(function() {
      $scope.teams = teams;
      return $scope.problems = problems;
    });
  };
  $scope.getTeamById = function(teamId) {
    return _.find($scope.teams, function(obj) {
      return obj.teamId === teamId;
    });
  };
  $scope.filterTeams = function(team) {
    if ($scope.config.isCountryBased) {
      return team.nationality === $scope.activeComponent.id;
    } else {
      return team.regionName === $scope.activeComponent.id;
    }
  };
  $scope.updateRank = function(teamId, rank) {
    var obj;
    obj = _.find($scope.teams, function(obj) {
      return obj.teamId === teamId;
    });
    if (obj != null) {
      return obj.teamRank = rank;
    }
  };
  $scope.updateNumSolvedAndTotalTime = function(teamId, numSolved, totalTime) {
    var obj;
    obj = _.find($scope.teams, function(obj) {
      return obj.teamId === teamId;
    });
    if (obj != null) {
      obj.nSolved = numSolved;
      return obj.totalTime = totalTime;
    }
  };
  $scope.updateTeamProblem = function(teamId, problemId, judged, solved, attempts, time, first) {
    var team;
    team = _.find($scope.teams, function(obj) {
      return obj.teamId === teamId;
    });
    if (typeof team !== "undefined" && typeof team.teamProblems !== "undefined") {
      if (typeof team.teamProblems[problemId] === "undefined") {
        team.teamProblems[problemId] = {};
      }
      team.teamProblems[problemId].judged = judged;
      team.teamProblems[problemId].solved = solved;
      team.teamProblems[problemId].attempts = attempts;
      team.teamProblems[problemId].time = time;
      return team.teamProblems[problemId].first = first;
    }
  };
  $scope.pickSuitableConfiguration = function(configurations, width) {
    var i, _i, _ref;
    for (i = _i = _ref = configurations.length - 1; _i >= 0; i = _i += -1) {
      if (width > configurations[i].width) {
        return configurations[i];
      }
    }
  };
  $scope.updateAllTeams = function() {
    var team, _i, _len, _ref, _results;
    _ref = $scope.teams;
    _results = [];
    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
      team = _ref[_i];
      _results.push($scope.updateTeamOnMap(team));
    }
    return _results;
  };
  $scope.updateTeamOnMap = function(team, solved) {
    var coor;
    if (solved == null) {
      solved = false;
    }
    coor = $scope.config.teamCoorData[team["teamId"]];
    if (coor) {
      team.longtitude = coor.longtitude;
      team.latitude = coor.latitude;
    } else {
      team.longtitude = 0;
      team.latitude = 0;
    }
    d3.select("#map-team-" + team["teamId"]).data([team]).style("position", "absolute").style("top", $scope._coorTop).style("left", $scope._coorLeft);
    if (solved) {
      $scope._showTeamPopoverById(team["teamId"]);
      $("#mapContainer svg").append($("#map-circle-" + team.teamId));
      return d3.select("#map-circle-" + team["teamId"]).transition().duration(500).style("fill", $scope._textRankColor(team.teamRank)).attr("r", 2 * $scope.config.circleSize).transition().duration(2500).transition().duration(500).attr("r", $scope.config.circleSize).each("end", function() {
        return $scope._hideTeamPopover();
      });
    } else {
      return d3.select("#map-circle-" + team["teamId"]).transition().duration(500).style("fill", $scope._textRankColor(team.teamRank)).attr("r", $scope.config.circleSize);
    }
  };
  return $scope.renderMap = function(appPath, config, teamCoordinates) {
    var currentScale, currentTranslate, path, svg;
    $scope.config = $.extend($.extend({
      width: 1000,
      ratio: 0.65,
      scale: 150,
      translate: [490, 380],
      circleSize: 6
    }, config), $scope.config);
    $scope.config.teamCount = $scope.teams.length;
    $scope.config.teamCoorData = teamCoordinates;
    $scope.config.legendWidth = $scope.config.width - 20;
    $scope.config.height = Math.round($scope.config.width * $scope.config.ratio);
    $scope.config.legendYTextBottomCorner = $scope.config.height - 40;
    currentScale = $scope.config.scale;
    currentTranslate = $scope.config.translate;
    svg = d3.select("#mapContainer").append("svg:svg").attr("width", $scope.config.width).attr("height", $scope.config.height);
    $scope.config.projection = d3.geo.mercator().scale(currentScale).translate(currentTranslate);
    path = d3.geo.path().projection($scope.config.projection);
    d3.json(appPath + "/maps/world-countries.json", function(collection) {
      var coor, teamId, universities, _ref;
      $scope.config.feature = svg.selectAll("path").data(collection.features).enter().append("svg:path").attr("d", function(d) {
        return path(d);
      }).attr("id", function(d) {
        return d.id;
      }).on("click", $scope._onCountryClick);
      $scope.config.feature.append("svg:title").text(function(d) {
        return d.properties.name;
      });
      _ref = $scope.config.teamCoorData;
      for (teamId in _ref) {
        coor = _ref[teamId];
        $scope.config.circleData.push({
          "id": teamId,
          "x": coor.longtitude,
          "y": coor.latitude,
          "country": coor.country
        });
      }
      universities = svg.selectAll("circle").data($scope.config.circleData);
      universities.enter().append("circle").attr("cx", function(d) {
        return $scope._computeCoord(d.x, d.y)[0];
      }).attr("cy", function(d) {
        return $scope._computeCoord(d.x, d.y)[1];
      }).attr("id", function(d) {
        return "map-circle-" + d.id;
      }).attr("r", $scope.config.circleSize).on("click", function(d) {
        $scope._clickPointOnMap(d);
        return d3.event.stopPropagation();
      }).on("mouseover", function(d) {
        $scope._showTeamPopoverById(d.id);
        $("#map-circle-" + d.id).attr({
          "r": 2 * $scope.config.circleSize
        });
        return $("#team_" + d.id).addClass("hightlightedTeam");
      }).on("mouseout", function(d) {
        $scope._hideTeamPopover();
        $("#map-circle-" + d.id).attr({
          "r": $scope.config.circleSize
        });
        return $("#team_" + d.id).removeClass("hightlightedTeam");
      });
      $scope.updateAllTeams();
      return $scope.createLegendBar(svg);
    });
    $scope.createLegendBar = function(svg) {
      var barSpace, i, _i, _ref, _results;
      barSpace = $scope.config.legendWidth / $scope.config.teamCount;
      svg.append("text").attr("text-anchor", "start").attr("y", $scope.config.legendYTextBottomCorner).attr("x", $scope.config.legendXCorner).text("Team rank legend");
      _results = [];
      for (i = _i = 0, _ref = $scope.config.teamCount; 0 <= _ref ? _i <= _ref : _i >= _ref; i = 0 <= _ref ? ++_i : --_i) {
        svg.append("rect").attr("x", i * barSpace + $scope.config.legendXCorner).attr("y", $scope.config.legendYTextBottomCorner + $scope.config.legendYSpaceTitleBars).attr("width", barSpace - 1).attr("height", $scope.config.legendBarHeight).style("fill", $scope._textRankColor(i)).style("z-index", 99);
        if ((i + 1) % $scope.config.legendLabelFrequency === 0 || (i + 1) === 1) {
          _results.push(svg.append("text").attr("text-anchor", "middle").attr("y", $scope.config.legendYTextBottomCorner + $scope.config.legendYSpaceTitleBars + $scope.config.legendBarHeight + $scope.config.legendYSpaceBottomTicks).attr("x", i * barSpace + $scope.config.legendXCorner + (barSpace / 2)).text(i + 1));
        } else {
          _results.push(void 0);
        }
      }
      return _results;
    };
    $scope._clickPointOnMap = function(team) {
      var i, _i, _ref;
      for (i = _i = 0, _ref = $scope.config.feature[0].length; 0 <= _ref ? _i < _ref : _i > _ref; i = 0 <= _ref ? ++_i : --_i) {
        if ($scope.config.feature[0][i].id === team.country) {
          $scope._onCountryClick($scope.config.feature[0][i].__data__);
          $("#team_" + team.id).mouseenter();
          return;
        }
      }
    };
    $scope._onCountryClick = function(d) {
      if (d && $scope.config.centered !== d) {
        $scope.config.centered = d;
      } else {
        $scope.config.centered = null;
        return;
      }
      return $scope.$apply(function() {
        if ($scope.config.isCountryBased) {
          svg.selectAll("path").classed("highligtedComponent", $scope.config.centered && function(d) {
            return d.properties.name === $scope.config.centered.properties.name;
          });
          ({
            'name': 'Europe',
            'id': 'Europe'
          });
          return $scope.activeComponent = {
            name: $scope.config.centered.properties.name,
            id: $scope.config.centered.id
          };
        } else {
          svg.selectAll("path").classed("highligtedComponent", $scope.config.centered && function(d) {
            return d.properties.continent === $scope.config.centered.properties.continent;
          });
          return $scope.activeComponent = $scope.continents[$scope.config.centered.properties.continent];
        }
      });
    };
    $scope._showTeamPopoverById = function(teamId) {
      var team;
      team = _.find($scope.teams, function(obj) {
        return obj.teamId === parseInt(teamId);
      });
      if (team != null) {
        return $scope._showTeamPopover(team);
      }
    };
    $scope.highlightTeamOnMap = function(team) {
      $("#map-circle-" + team.teamId).attr({
        "r": 2 * $scope.config.circleSize
      });
      $("#mapContainer svg").append($("#map-circle-" + team.teamId));
      return $scope._showTeamPopover(team);
    };
    $scope._showTeamPopover = function(team) {
      var html, i, tp, _i, _ref;
      $("#mapPopoverPanel").removeClass("hidden");
      $("#mapPopoverPanel").position({
        my: "center top+18",
        at: "center bottom",
        of: "#map-circle-" + team.teamId,
        collision: "fit"
      });
      $("#mapPopoverPanel .panel-heading").html('<strong>' + team.teamRank + '</strong> ' + team.teamName);
      html = '<table style="width:' + $("#mapPopoverPanel .panel-body").width() + 'px">';
      html += '<tbody>';
      html += '<tr>';
      for (i = _i = 0, _ref = $scope.problems.length - 1; 0 <= _ref ? _i <= _ref : _i >= _ref; i = 0 <= _ref ? ++_i : --_i) {
        if (i % 4 === 0) {
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
      $("#mapPopoverPanel .panel-body").html(html);
    };
    $scope.dishighlightTeamOnMap = function(team) {
      $("#map-circle-" + team.teamId).attr({
        "r": $scope.config.circleSize
      });
      return $scope._hideTeamPopover();
    };
    $scope._hideTeamPopover = function() {
      $("#mapPopoverPanel").addClass("hidden");
    };
    $scope._computeCoord = function(x, y) {
      var i;
      i = path({
        "type": "Feature",
        "geometry": {
          "type": "Polygon",
          "coordinates": [[[x, y], [x, y]]]
        }
      }, 1);
      return i.substring(1, i.length - 1).split(",");
    };
    $scope._textRankColor = function(rank) {
      var c;
      c = $scope._rankColor(rank);
      return "rgb(" + c[0] + "," + c[1] + "," + c[2] + ")";
    };
    $scope._rankColor = function(rank) {
      var b, box, boxPos, boxSize, colorChange, colorChange2, g, r, rankInc, secInc;
      boxSize = Math.ceil($scope.config.teamCount / 2);
      box = Math.floor(rank / boxSize);
      boxPos = rank % boxSize;
      rankInc = $scope.config.fullColor / boxSize;
      secInc = ($scope.config.fullColor - $scope.config.darkColor) / boxSize;
      r = 0;
      g = 0;
      b = 0;
      colorChange = Math.round(rankInc * boxPos);
      colorChange2 = Math.round(secInc * boxPos);
      if (box === 0) {
        r = 0 + colorChange;
        g = $scope.config.darkColor + colorChange2;
      }
      if (box === 1) {
        r = $scope.config.fullColor;
        g = $scope.config.fullColor - colorChange;
      }
      return [r, g, b];
    };
    $scope._coorTop = function(d) {
      return (parseFloat(computeCoord(d.longtitude, d.latitude)[1]) - 10) + "px";
    };
    return $scope._coorLeft = function(d) {
      return (parseFloat(computeCoord(d.longtitude, d.latitude)[0]) + 15) + "px";
    };
  };
});

updateWorldMap = function(data, ngController) {
  var colorBg;
  if (ngController == null) {
    ngController = null;
  }
  if (data.type === 'submission') {
    if (ngController !== null) {
      if (data.judged) {
        colorBg = data.solved ? "#66FF33" : "#FF5C33";
        $(".team_" + data.teamId).effect("highlight", {
          color: colorBg
        }, 3000);
      }
      return ngController.$apply(function() {
        var key;
        for (key in data.teams) {
          ngController.updateRank(data.teams[key].teamId, data.teams[key].teamRank);
          ngController.updateTeamOnMap(ngController.getTeamById(data.teams[key].teamId));
        }
        if (data.solved) {
          ngController.updateNumSolvedAndTotalTime(data.teamId, data.numSolved, data.total);
        }
        ngController.updateTeamProblem(data.teamId, data.problemId, data.judged, data.solved, data.attempts, data.time, data.first);
        return ngController.updateTeamOnMap(ngController.getTeamById(data["teamId"]), data.solved);
      });
    }
  }
};