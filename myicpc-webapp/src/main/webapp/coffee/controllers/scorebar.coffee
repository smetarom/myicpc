scorebar = angular.module('scorebar', []);

scorebar.controller('scorebarCtrl', ($scope) ->
  $scope.teams = {}
  $scope.problemCount = 0
  $scope.teamCount = 0
  $scope.config = {
  # thinckness of smaller bars
    barSize: 6,
  # thickness of bigger bars
    bigBarSize: 15,
  # count of bigger bars
    bigBarCount: 12,
  # horizontal space after a bar before info box
    infoBoxOffset: 10,
  # space after text (on the left) before gray bar
    titleSpace: 5,

  # Problem count axis settings
    tickSize: 7, # size of ticks
    tickPad: 15, # vertical tick padding
    legendTickYOffset: 10, # vertical offset for tick labels

  # Bar color legend settings size (width) of bar in legend
    barlegendSize: 30,
  # vertical position of bar in legend
    barLegendY: 7,
  # vertical position of bar text in legend
    barLegendYText: 15,
  # space before text, after bar in legend
    barLegendBeforText: 10,
  # max text width after space before next bar
    barLegendTextWidth: 90,

  # animation length (ms)
    animationTime: 1000,
  # how long emphasized colors and text stays after moving up (ms)
    textStayAfterUp: 3000,

  # space for team names on the left, before the bar
    teamNameOffset: 270,
  # use short names (if true) or abbreviations (if false)
    useLongNames: true,
    problemBarSize: 0,
  # width of zero bar (showing team position - small gray square)
    zeroBar: 5,
  # space between bars
    space: 1
  }

  $scope.init = (teams, teamCount, problemCount) ->
    $scope.$apply(() ->
      $scope.teams = teams
      $scope.problemCount = problemCount
      $scope.teamCount = teamCount
    )

  ###
    Render the whole scorebar chart

    @param width the width of chart for responsive purposes
  ###
  $scope.render = (width) ->
    if (width > 650)
      $scope.config.useLongNames = true
      $scope.config.teamNameOffset = 270
    else
      $scope.config.useLongNames = false
      $scope.config.teamNameOffset = 120

    chartWidth = width - $scope.config.teamNameOffset; # width of chart (excluding team name on the left)
    $scope.config.problemBarSize = (chartWidth - $scope.config.space - 2 * $scope.config.zeroBar) / $scope.problemCount
    height = $scope.config.bigBarCount * ($scope.config.bigBarSize + $scope.config.space) + ($scope.teamCount - $scope.config.bigBarCount) * ($scope.config.barSize + $scope.config.space) + 60

    d3.select("#scorebar-chart svg").attr("width", width).attr("height", height).style("width", width).style("height", height)

    $scope.drawLegend(width)
    $scope.drawXAxis(chartWidth)

    $scope.drawTeamBar(team) for team in $scope.teams

  ###
    Draw scorebar legend
  ###
  $scope.drawLegend = (width) ->
    legendHeight = 18
    legend =d3.select("#scorebar-legend g.canvas").attr("width", width).attr("height", legendHeight).style("width", width).style("height", legendHeight)

    # draw color rectangles
    legend.append("rect").attr("y", $scope.config.barLegendY).attr("width", $scope.config.barlegendSize).attr("height", $scope.config.barSize).attr("class", "passed");

    legend.append("rect").attr("y", $scope.config.barLegendY).attr("width", $scope.config.barlegendSize).attr("x", $scope.config.barlegendSize + $scope.config.barLegendBeforText + $scope.config.barLegendTextWidth).attr(
      "height", $scope.config.barSize).attr("class", "failed");

    # draw legend labels
    legend.append("text").attr("x", $scope.config.barlegendSize + $scope.config.barLegendBeforText).attr("y", $scope.config.barLegendYText).attr("text-anchor", "start").attr("class",
      "legend-label").text("# solved");

    legend.append("text").attr("x", 2 * $scope.config.barlegendSize + 2 * $scope.config.barLegendBeforText + $scope.config.barLegendTextWidth).attr("y", $scope.config.barLegendYText).attr("text-anchor",
      "start").attr("class", "legend-label").text("# failed");


  ###
    Render the x-axis with number of problems
  ###
  $scope.drawXAxis = (chartWidth) ->
    axis = d3.select("#scorebar-chart svg g.axis-canvas")
    axis.attr("transform", "translate(#{$scope.config.teamNameOffset},0)")
    ticks = []

    for i in [0..$scope.problemCount] by 1
      ticks[i] = i

    # Draw X-axis
    axis.append("line")
      .attr("x1", $scope.config.zeroBar).attr("x2", chartWidth - $scope.config.zeroBar)
      .attr("y1", $scope.config.tickPad).attr("y2", $scope.config.tickPad)
      .attr("stroke", "#000");

    # Draw ticks
    axis.selectAll(".axis-tick").data(ticks).attr("y1", $scope.config.tickPad)
      .attr("y2", $scope.config.tickSize + $scope.config.tickPad)
      .attr("x1", $scope._placeAxisLabel)
      .attr("x2", $scope._placeAxisLabel)
      .attr("stroke", "#000").attr("class", "axis-tick")
      .enter().append("line")
      .attr("y1", $scope.config.tickPad)
      .attr("y2", $scope.config.tickSize + $scope.config.tickPad)
      .attr("x1", $scope._placeAxisLabel)
      .attr("x2", $scope._placeAxisLabel)
      .attr("stroke", "#000").attr("class", "axis-tick")

    # Draw tick labels
    axis.selectAll(".tick-label").data(ticks)
      .attr("x", $scope._placeAxisLabel)
      .attr("y", $scope.config.legendTickYOffset).attr("text-anchor", "middle")
      .attr("class", "tick-label").text(String)
      .enter().append("text")
      .attr("x", $scope._placeAxisLabel)
      .attr("y", $scope.config.legendTickYOffset)
      .attr("text-anchor", "middle")
      .attr("class", "tick-label").text(String)

  $scope._placeAxisLabel = (d) ->
    return $scope.config.zeroBar + d * $scope.config.problemBarSize

  $scope._getTeamY = (team) ->
    if (team.rank <= $scope.config.bigBarCount)
      return team.rank * ($scope.config.bigBarSize + 1)
    else
      return ($scope.config.bigBarCount * ($scope.config.bigBarSize + 1)) + ((team.rank - $scope.config.bigBarCount + 1) * ($scope.config.barSize + 1) + 2)

  $scope._getTeamX = (team) ->
    return $scope.config.zeroBar + (team.solvedNum + team.failedNum) * $scope.config.problemBarSize + $scope.config.teamNameOffset + $scope.config.infoBoxOffset;

  $scope.updateTeamInfo = (team) ->
      if ($("#info-" + team.teamId).length)
        $("#info-" + team.teamId).css({top : $scope._getTeamY(team) + "px", left : $scope._getTeamX(team) + "px"})

      if ($("#info-" + team.teamId + "-rank").length)
        $("#info-" + team.teamId + "-rank").html(team.rank)

      formatProblemArray = (letters) ->
        str = letters.join(", ")
        if (str.length > 0)
          str = "(#{str})"
        return str

      if ($("#info-" + team.teamId + "-solved").length)
        $("#info-" + team.teamId + "-solved").html(team.solvedNum + " " + formatProblemArray(team.solved));

      if ($("#info-" + team.teamId + "-failed").length)
        $("#info-" + team.teamId + "-failed").html(team.failedNum + " " + formatProblemArray(team.failed));

  $scope.drawTeamBar = (team) ->
    chart = d3.select("#scorebar-chart g.canvas")
    n = chart.selectAll("#neutrl-bar" + team["teamId"]).data([ team, ])

    # compute bar thickness (based on rank)
    getHeight = (team) ->
      if team.rank <= $scope.config.bigBarCount then $scope.config.bigBarSize else $scope.config.barSize

    previousY = $("#passed-bar" + team["teamId"]).attr("y")
    # team new Y position
    nextY = $scope._getTeamY(team, 0)
    movedUp = false

    # set true if team moved up in rank
    if (previousY > nextY && team.solvedNum > 0)
      movedUp = true

    # draw the gray team mini bar
    trn = n.transition().delay($scope.config.animationTime).duration($scope.config.animationTime).attr("height", getHeight).attr("y", $scope._getTeamY);
    n.enter().append("rect").attr("y", $scope._getTeamY).attr("width", (d) ->
        return $scope.config.zeroBar
      ).attr("height", getHeight).attr("class", "neutral").attr("id", (d, i) ->
        return "neutrl-bar" + d["teamId"];
      )
      # set hover - display info and highlight
      .attr("onmouseover", "scorebarDisplayText(this);")
      # set hover - hide info
      .attr("onmouseout", "scorebarHideTextHideText(this);");

    # Draw bar for solved problems
    p = chart.selectAll("#passed-bar" + team["teamId"]).data([ team, ])
    trp = p.transition().duration($scope.config.animationTime).attr("width", (d) ->
      return d["solvedNum"] * $scope.config.problemBarSize;
    )

    trp = trp.style("fill", "palegreen") if movedUp
    trp = trp.transition().delay($scope.config.animationTime).duration($scope.config.animationTime).attr("height", getHeight).attr("y", $scope._getTeamY)
    # emphasised animation if team moved up
    if (movedUp)
      trp = trp.transition().delay(2 * $scope.config.animationTime + $scope.config.textStayAfterUp);
    else
      trp = trp.transition().delay(2 * $scope.config.animationTime);

    trp.style("fill", "").attr("width", (d) ->
      return d["solvedNum"] * $scope.config.problemBarSize
    ).attr("height", getHeight).attr("y", $scope._getTeamY)

    p.enter().append("rect").attr("y", $scope._getTeamY).attr("width", (d) ->
      return d["solvedNum"] * $scope.config.problemBarSize;
    ).attr("x", $scope.config.zeroBar + $scope.config.space)
    .attr("height", getHeight).attr("class", "passed").attr("id", (d, i) ->
      return "passed-bar" + d["teamId"];
    )
    .attr("onmouseover", "scorebarDisplayText(this);")
    .attr("onmouseout", "scorebarHideText(this);")

    # Draw bar for failed problems
    failedSpace = 2 * $scope.config.space
    fshorter = 0

    if (team.solvedNum == 0)
      failedSpace = $scope.config.space
    else if (team.failedNum > 0)
      fshorter = 1

    f = chart.selectAll("#failed-bar" + team["teamId"]).data([ team, ])
    trf = f.transition().duration($scope.config.animationTime).attr("x", (d, i) ->
      return $scope.config.zeroBar + d["solvedNum"] * $scope.config.problemBarSize + failedSpace;
    ).attr("width", (d) ->
      return d["failedNum"] * $scope.config.problemBarSize - fshorter;
    )

    trf = trf.style("fill", "salmon") if (movedUp)
    trf = trf.transition().delay($scope.config.animationTime).duration($scope.config.animationTime).attr("y", $scope._getTeamY).attr("height", getHeight);

    if (movedUp)
      trf = trf.transition().delay(2 * $scope.config.animationTime + $scope.config.textStayAfterUp)
    else
      trf = trf.transition().delay(2 * $scope.config.animationTime)

    trf.style("fill", "").attr("x", (d, i) ->
      return $scope.config.zeroBar + d["solvedNum"] * $scope.config.problemBarSize + failedSpace
    ).attr("width", (d) ->
      return d["failedNum"] * $scope.config.problemBarSize - fshorter
    ).attr("y", $scope._getTeamY).attr("height", getHeight)

    f.enter().append("rect").attr("y", $scope._getTeamY).attr("x", (d, i) ->
      return $scope.config.zeroBar + d["solvedNum"] * $scope.config.problemBarSize + failedSpace
    ).attr("width", (d) ->
      return d["failedNum"] * $scope.config.problemBarSize - fshorter
    ).attr("height", getHeight).attr("id", (d, i) ->
      return "failed-bar" + d["teamId"]
    ).attr("class", "failed")
    .on("mouseover", (team) ->
      alert(team["teamId"])
      #chart.selectAll("#bar-Ntitle" + team["teamId"]).attr("class", "bar-title-Visible")
    )
    .attr("onmouseout", "scorebarHideText(this);")

    # Draw team text on the left
    getTextClass = (team, i) ->
      if (team.rank <= $scope.config.bigBarCount)
        return "bar-title-Visible"
      else
        return "bar-title-Hidden"
    getyText = (team, i) ->
      return $scope._getTeamY(team, i) + $scope.config.bigBarSize - 3
    tt = chart.selectAll("#bar-Ntitle" + team["teamId"]).data([ team, ])
    t1 = tt.transition().duration($scope.config.animationTime)
    t2 = t1.transition().delay($scope.config.animationTime).duration($scope.config.animationTime).attr("y", getyText)

    # make text visible if team moved up
    t1 = t1.attr("class", "bar-title-Visible") if (movedUp)

    if (movedUp && (team.rank > $scope.config.bigBarCount))
      t2 = t2.transition().delay($scope.config.textStayAfterUp + 2 * $scope.config.animationTime);
    else
      t2 = t2.transition().delay(2 * $scope.config.animationTime);
    t2.attr("class", getTextClass);

    tt.enter().append("text").attr("text-anchor", "end").attr("y", getyText)
    .attr("x", -$scope.config.titleSpace).attr("id", (team, i) ->
      return "bar-Ntitle" + team["teamId"]
    ).attr("class", getTextClass).text((team) ->
      return if $scope.config.useLongNames then team.teamShortName else team.teamAbbreviation
    )
    .attr("onmouseover", "scorebarDisplayText(this);")
    .attr("onmouseout", "scorebarHideText(this);")

  $scope.findById = (teamId) ->
    _.find($scope.teams, (obj) ->
      obj.teamId == teamId
    )
)

scorebarDisplayText = (element) ->
  s = element.id.substring(10);
  # do not change substring size - all IDs are made to follow the 10
  # character format
  # e.g. "#passed-bar", "#bar-Ntitle", etc.
  d3.select("#passed-bar" + s).attr("class", "barhover");
  d3.select("#failed-bar" + s).attr("class", "barhover");
  d3.select("#neutrl-bar" + s).attr("class", "barhover");
  d3.select("#workon-bar" + s).attr("class", "barhover");
  d3.select("#bar-Ntitle" + s).style("fill", "blue");
  d3.select("#info-" + s).attr("class", "teamInfoVisible");

scorebarHideText = (element) ->
  s = element.id.substring(10)
  # do not change substring size - all IDs are made to follow the 10
  # character format
  # e.g. "#passed-bar", "#bar-Ntitle", etc.
  d3.select("#passed-bar" + s).attr("class", "passed")
  d3.select("#failed-bar" + s).attr("class", "failed")
  d3.select("#neutrl-bar" + s).attr("class", "neutral")
  d3.select("#workon-bar" + s).attr("class", "workon")
  d3.select("#bar-Ntitle" + s).style("fill", "black")
  d3.select(".teamInfoVisible").attr("class", "teamInfoHidden")

updateScorebar = (data, ngController = null) ->
  if data.type == 'submission'
    if ngController != null
      team = ngController.findById(data["teamId"])
      if data.solved
        if team.solved.indexOf(data["problemCode"]) == -1
          team.solvedNum += 1
          team.solved.push(data["problemCode"])
          index = team.failed.indexOf(data["problemCode"]);
          if index != -1
            team.failedNum -= 1
            team.failed.splice(index, 1)
      else if team.failed.indexOf(data["problemCode"]) == -1
        team.failedNum += 1;
        team.failed.push(data["problemCode"]);

      if data.judged && data.solved
        for key of data.teams
          teamId = parseInt(key)
          effectedTeam = ngController.findById(teamId)
          effectedTeam.rank = data.teams[key].teamRank
          ngController.drawTeamBar(effectedTeam)
          ngController.updateTeamInfo(effectedTeam)

      ngController.drawTeamBar(team)
      ngController.updateTeamInfo(team)




