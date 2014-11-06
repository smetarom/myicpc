var ScorebarConfig = {
        barSize: 6, // thinckness of smaller bars
        bigBarSize: 15, // thickness of bigger bars
        bigBarCount: 12, // count of bigger bars
        // bars start. Must match the template <g
        // class="axis-canvas" transform="translate(120,0)">
        infoBoxOffset: 10, // horizontal space after a bar before info box; note,
        // that the info box can NEVER overlap the highlighted
        // bar, otherwise, the hover does not work properly
        titleSpace: 5, // space after text (on the left) before gray bar

        // Problem count axis settings
        tickSize: 7, // size of ticks
        tickPad: 15, // vertical tick padding
        legendTickYOffset: 10, // vertical offset for tick labels

        // Bar color legend settings
        barlegendSize: 30, // size (width) of bar in legend
        barLegendY: 7, // vertical position of bar in legend
        barLegendYText: 15, // vertical position of bar text in legend
        barLegendBeforText: 10, // space before text, after bar in legend
        barLegendTextWidth: 90, // max text width after space before next bar

        // Animation settings
        animationTime: 1000, // animation length (ms)
        textStayAfterUp: 3000 // how long emphasized colors and text stays after
        // moving up (ms)
    },
/*
 * Helper function - computes y coordinate of a team in the chart based on team
 * rank Params: d - team, d.rank must contain the team's rank
 */
    gety = function (d, i) {
        if (d.rank <= ScorebarConfig.bigBarCount) {
            return d.rank * (ScorebarConfig.bigBarSize + 1);
        } else {
            return (ScorebarConfig.bigBarCount * (ScorebarConfig.bigBarSize + 1)) + ((d.rank - ScorebarConfig.bigBarCount + 1) * (ScorebarConfig.barSize + 1) + 2);
        }
    }, getx = function (d, i) {
        return zeroBar + (d.solvedNum + d.failedNum) * problembarSize + teamNameOffset + ScorebarConfig.infoBoxOffset;
    };

drawChart = function (width, numProblems) {
    problemCount = numProblems;

    // Bar graph settings
    teamNameOffset = 120; // The space for team names on the left, before the
    if (width > 650) {
        useLongNames = true;
        teamNameOffset = 270;
    } else {
        useLongNames = false;
    }

    chWidth = width - teamNameOffset; // width of chart (excluding team name on the left)
    space = 1; // space between bars
    zeroBar = 5; // size (width) of zero bar (showing team position - small gray square)
    problembarSize = (chWidth - space - 2 * zeroBar) / problemCount;
};

// Draw problem count legend
drawLegend = function () {

    var ticks = [], i,
        axis = d3.select("#d3chart svg g.axis-canvas");
    for (i = 0; i <= problemCount; i++) {
        ticks[i] = i;
    }
    // Draw ticks
    axis.selectAll(".axis-tick").data(ticks).attr("y1", ScorebarConfig.tickPad).attr("y2", ScorebarConfig.tickSize + ScorebarConfig.tickPad).attr("x1", function (d) {
        return zeroBar + d * problembarSize;
    }).attr("x2", function (d) {
        return zeroBar + d * problembarSize;
    }).attr("stroke", "#000").attr("class", "axis-tick").enter().append("line").attr("y1", ScorebarConfig.tickPad).attr("y2", ScorebarConfig.tickSize + ScorebarConfig.tickPad).attr("x1",
        function (d) {
            return zeroBar + d * problembarSize;
        }).attr("x2", function (d) {
            return zeroBar + d * problembarSize;
        }).attr("stroke", "#000").attr("class", "axis-tick");

    // Draw tick lables
    axis.selectAll(".tick-label").data(ticks).attr("x", function (d) {
        return zeroBar + d * problembarSize;
    }).attr("y", ScorebarConfig.legendTickYOffset).attr("text-anchor", "middle").attr("class", "tick-label").text(String).enter().append("text").attr("x",
        function (d) {
            return zeroBar + d * problembarSize;
        }).attr("y", ScorebarConfig.legendTickYOffset).attr("text-anchor", "middle").attr("class", "tick-label").text(String);

};

drawOnce = function () {
    var height = ScorebarConfig.bigBarCount * (ScorebarConfig.bigBarSize + space) + (teamCount - ScorebarConfig.bigBarCount) * (ScorebarConfig.barSize + space) + 60,
        axis = d3.select("#d3chart svg g.axis-canvas").attr("transform", "translate(" + teamNameOffset + ",0)"),
        legend = d3.select("#d3chartlegend g.canvas");

    d3.select("#d3chart svg").attr("width", width).attr("height", height);

    // draw problem count legend line
    axis.append("line").attr("x1", zeroBar).attr("x2", chWidth - zeroBar).attr("y1", ScorebarConfig.tickPad).attr("y2", ScorebarConfig.tickPad).attr("stroke", "#000");

    // draw rectangles in the graph problem color legend
    legend.append("rect").attr("y", ScorebarConfig.barLegendY).attr("width", ScorebarConfig.barlegendSize).attr("height", ScorebarConfig.barSize).attr("class", "passed");

    legend.append("rect").attr("y", ScorebarConfig.barLegendY).attr("width", ScorebarConfig.barlegendSize).attr("x", ScorebarConfig.barlegendSize + ScorebarConfig.barLegendBeforText + ScorebarConfig.barLegendTextWidth).attr(
        "height", ScorebarConfig.barSize).attr("class", "failed");

    // draw texts in the graph problem color legend
    legend.append("text").attr("x", ScorebarConfig.barlegendSize + ScorebarConfig.barLegendBeforText).attr("y", ScorebarConfig.barLegendYText).attr("text-anchor", "start").attr("class",
        "legend-label").text("# solved");

    legend.append("text").attr("x", 2 * ScorebarConfig.barlegendSize + 2 * ScorebarConfig.barLegendBeforText + ScorebarConfig.barLegendTextWidth).attr("y", ScorebarConfig.barLegendYText).attr("text-anchor",
        "start").attr("class", "legend-label").text("# failed");
};

displayTeamLine = function (team) {
    var chart = d3.select("#d3chart g.canvas"),
    // Helper function - compute text y coordinate
        getyText = function (d, i) {
            return gety(d, i) + ScorebarConfig.bigBarSize - 3;
        },
    // Helper function - compute bar thickness (based on rank)
        getHeight = function (d) {
            if (d.rank <= ScorebarConfig.bigBarCount) {
                return ScorebarConfig.bigBarSize;
            } else {
                return ScorebarConfig.barSize;
            }
        },
    // team previous Y position
        previousY = $("#passed-bar" + team["teamId"]).attr("y"),
    // team new Y position
        nextY = gety(team, 0),
        movedUp = false,
        n = chart.selectAll("#neutrl-bar" + team["teamId"]).data([ team, ]),
        trn = n.transition().duration(ScorebarConfig.animationTime),
        p = chart.selectAll("#passed-bar" + team["teamId"]).data([ team, ]),
        trp = p.transition().duration(ScorebarConfig.animationTime).attr("width", function (d) {
            return d["solvedNum"] * problembarSize;
        }),
        failedSpace = 2 * space,
        fshorter = 0,
        f = chart.selectAll("#failed-bar" + team["teamId"]).data([ team, ]),
        trf = f.transition().duration(ScorebarConfig.animationTime).attr("x", function (d, i) {
            return zeroBar + d["solvedNum"] * problembarSize + failedSpace;
        }) // failed bar is after the solved bar
            .attr("width", function (d) {
                return d["failedNum"] * problembarSize - fshorter;
            }),
        getTextClass = function (d, i) {
            if (d.rank <= ScorebarConfig.bigBarCount) {
                return "bar-title-Visible";
            } else {
                return "bar-title-Hidden";
            }
        },
        tt = chart.selectAll("#bar-Ntitle" + team["teamId"]).data([ team, ]),
        t1 = tt.transition().duration(ScorebarConfig.animationTime),
        t2 = t1.transition().delay(ScorebarConfig.animationTime).duration(ScorebarConfig.animationTime).attr("y", getyText);

    if (previousY > nextY && team.solvedNum > 0) {
        // set true if team moved up (actually interested if moved up in rank,
        // but we have only the computed Y position, but effect is the same)
        movedUp = true;
    }

    // Draw the gray team mini bar (always displayed even if no problems solved,
    // failed, working on)
    trn = trn.transition().delay(ScorebarConfig.animationTime).duration(ScorebarConfig.animationTime).attr("height", getHeight).attr("y", gety);
    n.enter().append("rect").attr("y", gety).attr("width", function (d) {
        return zeroBar;
    }).attr("height", getHeight).attr("class", "neutral").attr("id", function (d, i) {
        return "neutrl-bar" + d["teamId"];
    })
        // set hover - display info and highlight
        .attr("onmouseover", "displayText(this);")
        // set hover - hide info
        .attr("onmouseout", "hideText(this);");
    // ----------------------

    // Draw bar for solved problems
    if (movedUp) {
        // emphasised animation if team moved up
        trp = trp.style("fill", "palegreen");
    }
    trp = trp.transition().delay(ScorebarConfig.animationTime).duration(ScorebarConfig.animationTime).attr("height", getHeight).attr("y", gety);
    if (movedUp) {
        // emphasised animation if team moved up
        trp = trp.transition().delay(2 * ScorebarConfig.animationTime + ScorebarConfig.textStayAfterUp);
    } else {
        trp = trp.transition().delay(2 * ScorebarConfig.animationTime);
    }
    trp.style("fill", "").attr("width", function (d) {
        return d["solvedNum"] * problembarSize;
    }).attr("height", getHeight).attr("y", gety);

    p.enter().append("rect").attr("y", gety).attr("width", function (d) {
        return d["solvedNum"] * problembarSize;
    }).attr("x", zeroBar + space) // solved bar is displayed as the first one
        .attr("height", getHeight).attr("class", "passed").attr("id", function (d, i) {
            return "passed-bar" + d["teamId"];
        }).attr("onmouseover", "displayText(this);") // set hover - display info
        // and highlight
        .attr("onmouseout", "hideText(this);"); // set hover - hide info
    // -----------------------

    // Draw bar for failed problems

    // Figure out bar alignment (solved bar exists or not)
    if (team.solvedNum == 0) {
        failedSpace = space;
    } else {
        if (team.failedNum > 0) {
            fshorter = 1;
        }
    }

    if (movedUp) {
        trf = trf.style("fill", "salmon"); // emphasised animation if team
        // moved up
    }

    trf = trf.transition().delay(ScorebarConfig.animationTime).duration(ScorebarConfig.animationTime).attr("y", gety).attr("height", getHeight);

    if (movedUp) {
        // emphasised animation if team moved up
        trf = trf.transition().delay(2 * ScorebarConfig.animationTime + ScorebarConfig.textStayAfterUp);
    } else {
        trf = trf.transition().delay(2 * ScorebarConfig.animationTime);
    }
    trf.style("fill", "").attr("x", function (d, i) {
        return zeroBar + d["solvedNum"] * problembarSize + failedSpace;
    }) // failed bar is after the solved bar
        .attr("width", function (d) {
            return d["failedNum"] * problembarSize - fshorter;
        }).attr("y", gety).attr("height", getHeight);

    f.enter().append("rect").attr("y", gety).attr("x", function (d, i) {
        return zeroBar + d["solvedNum"] * problembarSize + failedSpace;
    }) // failed bar is after the solved bar
        .attr("width", function (d) {
            return d["failedNum"] * problembarSize - fshorter;
        }).attr("height", getHeight).attr("id", function (d, i) {
            return "failed-bar" + d["teamId"];
            // set hover - display info and highlight
        }).attr("class", "failed").attr("onmouseover", "displayText(this);")
        // set hover - hide info
        .attr("onmouseout", "hideText(this);");
    // -----------------------

    // Draw team text on the left
    // Helper function - to set the text visible or invisible
    // Note, that we are moving the text around to proper positions even if
    // invisible
    if (movedUp) {
        // emphasised animation if team moved up (make text visible for short
        // time)
        t1 = t1.attr("class", "bar-title-Visible");
    }
    if (movedUp && (team.rank > ScorebarConfig.bigBarCount)) {
        // emphasised animation if team moved up
        t2 = t2.transition().delay(ScorebarConfig.textStayAfterUp + 2 * ScorebarConfig.animationTime);
    } else {
        t2 = t2.transition().delay(2 * ScorebarConfig.animationTime);
    }
    t2.attr("class", getTextClass);

    tt.enter().append("text").attr("text-anchor", "end").attr("y", getyText)
        // negative because of the translated coordinate system
        .attr("x", -ScorebarConfig.titleSpace).attr("id", function (d, i) {
            return "bar-Ntitle" + d["teamId"];
        }).attr("class", getTextClass).text(function (d) {
            return useLongNames ? d.teamShortName : d.teamAbbreviation;
        })
        // set hover - display info and highlight
        .attr("onmouseover", "displayText(this);")
        // set hover - hide info
        .attr("onmouseout", "hideText(this);");

    // -----------------------

};

updateTeamInfo = function (team) {
    if ($("#info-" + team.teamId).length) {
        $("#info-" + team.teamId).css({
            top: gety(team, 0) + "px",
            left: getx(team, 0) + "px"
        });
    }

    if ($("#info-" + team.teamId + "-rank").length) {
        $("#info-" + team.teamId + "-rank").html(team.rank);
    }

    formatProblemArray = function (letters) {
        var str = letters.join(", ");
        if (str.length > 0) {
            str = "(" + str + ")";
        }
        return str;
    };
    if ($("#info-" + team.teamId + "-solved").length) {
        $("#info-" + team.teamId + "-solved").html(team.solvedNum + " " + formatProblemArray(team.solved));
    }
    if ($("#info-" + team.teamId + "-failed").length) {
        $("#info-" + team.teamId + "-failed").html(team.failedNum + " " + formatProblemArray(team.failed));
    }
};

// Called on team hover start - displays team info box and highlights team
// including team name
displayText = function (element) {

    var s = element.id.substring(10);
    // do not change substring size - all IDs are made to follow the 10
    // character format
    // e.g. "#passed-bar", "#bar-Ntitle", etc.
    d3.select("#passed-bar" + s).attr("class", "barhover");
    d3.select("#failed-bar" + s).attr("class", "barhover");
    d3.select("#neutrl-bar" + s).attr("class", "barhover");
    d3.select("#workon-bar" + s).attr("class", "barhover");
    d3.select("#bar-Ntitle" + s).style("fill", "blue");
    d3.select("#info-" + s).attr("class", "teamInfoVisible");
};

// Called on team hover end - hides all team info boxes and cancels highlights
hideText = function (element) {

    var s = element.id.substring(10);
    // do not change substring size - all IDs are made to follow the 10
    // character format
    // e.g. "#passed-bar", "#bar-Ntitle", etc.
    d3.select("#passed-bar" + s).attr("class", "passed");
    d3.select("#failed-bar" + s).attr("class", "failed");
    d3.select("#neutrl-bar" + s).attr("class", "neutral");
    d3.select("#workon-bar" + s).attr("class", "workon");
    d3.select("#bar-Ntitle" + s).style("fill", "black");
    d3.select(".teamInfoVisible").attr("class", "teamInfoHidden");
};