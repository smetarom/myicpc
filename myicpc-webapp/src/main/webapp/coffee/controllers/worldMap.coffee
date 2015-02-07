scorebar = angular.module('worldMap', []);

scorebar.controller('worldMapCtrl', ($scope) ->
  $scope.teams = []
  $scope.problems = []
  $scope.config = {
    centered: null,
    config: null,
    projection: null,
    feature: null,
    # country/region flag
    isCountryBased: true,
    # team coordinates
    teamCoorData: null,
    # teams with coordinates
    teamsWithCoor: new Array(),
    circleData: new Array(),
    # circle size
    circleSize: 5,
    teamCount:0,
    # settings for color interpolation
    fullColor: 255,
    darkColor: 100,

    refreshAnimationLength: 750,
    # settings for color legend
    # width of the legend
    legendWidth: 1000,
    legendYTextBottomCorner: 200, # Y coor. of bottom corner of title text
    legendYSpaceTitleBars: 5, # space between title and top corner or bars
    legendYSpaceBottomTicks: 15, # space between bottom corner of bars and
    # bottom corner of labels
    legendXCorner: 10, # X coordinate of legend
    legendBarHeight: 10, # height of bars in legend
    legendLabelFrequency: 10 # how often should the text label appear
  }
  $scope.continents = {'EU' : {'name': 'Europe', 'id': 'Europe'}, 'NA' : {'name': "North America", 'id': "North America"}, 'AS' : {'name': "Asia", 'id': "Asia"}, 'SA' : {'name': "Latin America", 'id': "Latin America"}, 'AF' : {'name': "Africa &  M. East", 'id': "Africa and the Middle East"}, 'AU': {'name': "South Pacific", 'id': "South Pacific"}}

  $scope.init = (teams, problems) ->
    $scope.$apply(() ->
      $scope.teams = teams
      $scope.problems = problems
    )

  $scope.filterTeams = (team) ->
    if $scope.config.isCountryBased
      return team.nationality == $scope.activeComponent.id
    else
      return team.regionName == $scope.activeComponent.id

  $scope.pickSuitableConfiguration = (configurations, width) ->
    for i in [configurations.length-1..0] by -1
      if (width > configurations[i].width)
        return configurations[i]

  $scope.updateAllTeams = () ->
    for team in $scope.teams
      $scope.updateTeamOnMap(team)

  $scope.updateTeamOnMap = (team, solved = false) ->
    coor = $scope.config.teamCoorData[team["teamId"]];
    if (coor)
      team.longtitude = coor.longtitude
      team.latitude = coor.latitude
    else
      team.longtitude = 0
      team.latitude = 0

    d3.select("#map-team-" + team["teamId"]).data([team,])
      .style("position", "absolute")
      .style("top", $scope._coorTop)
      .style("left", $scope._coorLeft)

    # special animation if team moved up in rank
    if (solved)
#      showMapPopover({'id': team["teamId"]}, globalMapController);
      d3.select("#map-circle-" + team["teamId"]).transition().duration(500)
        .style("fill", textRankColor(team.rank))
        .attr("r", 2* $scope.config.circleSize)
        .transition().duration(2500).transition().duration(500)
        .attr("r", $scope.config.circleSize).each("end", () ->
        #    hideMapPopover();
        )
    else
      # otherwise just change color by new rank
      d3.select("#map-circle-" + team["teamId"]).transition().duration(500)
        .style("fill", $scope._textRankColor(team.teamRank)).attr("r", $scope.config.circleSize);

  $scope.renderMap = (appPath, config, teamCoordinates) ->
    $scope.config = $.extend($.extend({
      width: 1000,
      ratio: 0.65,
      scale: 150,
      translate: [ 490, 380 ],
      circleSize: 6
    }, config), $scope.config)

    $scope.config.teamCount = $scope.teams.length
    $scope.config.teamCoorData = teamCoordinates
    $scope.config.legendWidth = $scope.config.width - 20
    $scope.config.height = Math.round($scope.config.width * $scope.config.ratio)
    $scope.config.legendYTextBottomCorner = $scope.config.height - 40

    currentScale = $scope.config.scale
    currentTranslate = $scope.config.translate
    # create map svg
    svg = d3.select("#mapContainer").append("svg:svg").attr("width", $scope.config.width).attr("height", $scope.config.height);

    # create projection
    $scope.config.projection = d3.geo.mercator().scale(currentScale).translate(currentTranslate);

    # create object for GPS coordinate conversions using projection
    path = d3.geo.path().projection($scope.config.projection);

    d3.json(appPath+"/maps/world-countries.json", (collection) ->
      $scope.config.feature = svg.selectAll("path").data(collection.features).enter().append("svg:path").attr("d", (d) -> path(d))
        .attr("id", (d) ->
          return d.id
        )
      .on("click", $scope._onCountryClick);
      $scope.config.feature.append("svg:title").text((d) ->
        return d.properties.name
      )

      for teamId, coor of $scope.config.teamCoorData
        $scope.config.circleData.push({
          "id" : teamId,
          "x" : coor.longtitude,
          "y" : coor.latitude,
          "country" : coor.country,
        });

      universities = svg.selectAll("circle").data($scope.config.circleData)
      universities.enter().append("circle").attr("cx", (d) ->
        return $scope._computeCoord(d.x, d.y)[0]
      ).attr("cy", (d) ->
        return $scope._computeCoord(d.x, d.y)[1]
      ).attr("id", (d) ->
        return "map-circle-" + d.id
      ).attr("r", $scope.config.circleSize)
      .on("click", (d) ->
        $scope._clickPointOnMap(d);
        d3.event.stopPropagation();
      )
      .on("mouseover", (d) ->
        $scope._showTeamPopoverById(d.id);
        $("#map-circle-"+d.id).attr({"r":2 * $scope.config.circleSize});
        $("#team_"+d.id).addClass("hightlightedTeam");
      )
      .on("mouseout", (d) ->
        $scope._hideTeamPopover();
        $("#map-circle-"+d.id).attr({"r": $scope.config.circleSize});
        $("#team_"+d.id).removeClass("hightlightedTeam");
      )

      $scope.updateAllTeams()
      $scope.createLegendBar(svg)
    )

    # Draws color legend of the team rank
    # Uses textRankColor and rankColor for color interpolation
    $scope.createLegendBar = (svg) ->
      barSpace = $scope.config.legendWidth / $scope.config.teamCount
      svg.append("text").attr("text-anchor", "start")
        .attr("y", $scope.config.legendYTextBottomCorner)
        .attr("x", $scope.config.legendXCorner)
        .text("Team rank legend")

      for i in [0..$scope.config.teamCount]
        svg.append("rect")
          .attr("x", i * barSpace + $scope.config.legendXCorner)
          .attr("y", $scope.config.legendYTextBottomCorner + $scope.config.legendYSpaceTitleBars)
          .attr("width", barSpace - 1)
          .attr("height", $scope.config.legendBarHeight)
          .style("fill", $scope._textRankColor(i))
          .style("z-index", 99)

        if ((i + 1) % $scope.config.legendLabelFrequency == 0 || (i + 1) == 1)
          svg.append("text").attr("text-anchor", "middle")
            .attr("y", $scope.config.legendYTextBottomCorner + $scope.config.legendYSpaceTitleBars + $scope.config.legendBarHeight + $scope.config.legendYSpaceBottomTicks)
            .attr("x", i * barSpace + $scope.config.legendXCorner + (barSpace / 2)).text(i + 1);

    $scope._clickPointOnMap = (team) ->
      for i in [0...$scope.config.feature[0].length]
        if $scope.config.feature[0][i].id == team.country
          $scope._onCountryClick($scope.config.feature[0][i].__data__);
          $("#team_"+team.id).mouseenter();
          return

    $scope._onCountryClick = (d)  ->
      if (d && $scope.config.centered != d)
        $scope.config.centered = d;
      else
        $scope.config.centered = null;
        return;

      $scope.$apply( ->
        if $scope.config.isCountryBased
          svg.selectAll("path").classed("highligtedComponent", $scope.config.centered && (d)  -> return d.properties.name == $scope.config.centered.properties.name)
          {'name': 'Europe', 'id': 'Europe'}
          $scope.activeComponent = {name: $scope.config.centered.properties.name, id: $scope.config.centered.id}
        else
          svg.selectAll("path").classed("highligtedComponent", $scope.config.centered && (d)  -> return d.properties.continent == $scope.config.centered.properties.continent)
          $scope.activeComponent = $scope.continents[$scope.config.centered.properties.continent]
      )

    $scope._showTeamPopoverById = (teamId) ->
      team = _.find($scope.teams, (obj) -> return obj.teamId == parseInt(teamId));
      if team?
        $scope._showTeamPopover(team)

    $scope.highlightTeamOnMap = (team) ->
      $("#map-circle-"+team.teamId).attr({"r":2 * $scope.config.circleSize});
      $("#mapContainer svg").append($("#map-circle-"+team.teamId));
      $scope._showTeamPopover(team)

    $scope._showTeamPopover = (team) ->
      $("#mapPopoverPanel").removeClass("hidden");
      $("#mapPopoverPanel").position({
        my: "center top+18",
        at: "center bottom",
        of: "#map-circle-"+team.teamId,
        collision: "fit"
      });

      $("#mapPopoverPanel .panel-heading").html('<strong>' + team.teamRank + '</strong> ' + team.teamName);
      html = '<table style="width:'+$("#mapPopoverPanel .panel-body").width()+'px">';
      html += '<tbody>';
      html += '<tr>';
      for i in [0..$scope.problems.length - 1]
        if ( (i) % 4 == 0)
          html += '</tr><tr>'

        tp = team.teamProblems[$scope.problems[i].id];
        if (typeof tp == "undefined")
          html += '<td class="popoverCell"><span class="label label-shadow">'+$scope.problems[i].code+'</span></td>';
        else if (tp.judged)
          if (tp.solved)
            html += '<td class="popoverCell"><span class="label label-success">'+tp.attempts+' - '+convertSecondsToMinutes(tp.time)+'</span></td>';
          else
            html += '<td class="popoverCell"><span class="label label-danger">'+tp.attempts+'</span></td>';
      html += '</tr>';
      html += '<tbody>';
      html += '</table>';
      $("#mapPopoverPanel .panel-body").html(html);
      return

    $scope.dishighlightTeamOnMap = (team) ->
      $("#map-circle-"+team.teamId).attr({"r":$scope.config.circleSize});
      $scope._hideTeamPopover()

    $scope._hideTeamPopover = () ->
      $("#mapPopoverPanel").addClass("hidden")
      return

    $scope._computeCoord = (x, y) ->
      i = path({
          "type" : "Feature",
          "geometry" : {
            "type" : "Polygon",
            "coordinates" : [ [ [ x, y ], [ x, y ] ] ]
          }
        }, 1)
      return i.substring(1, i.length - 1).split(",")

    # Helper function for color legend drawing
    # Converts rank via rankColor interpolation function into text representation required by CSS
    $scope._textRankColor = (rank) ->
      c = $scope._rankColor(rank)
      return "rgb(" + c[0] + "," + c[1] + "," + c[2] + ")"

    # Color interpolation Used to compute color from team rank From dark green (rank 1), through yellow to red (last rank)
    $scope._rankColor = (rank) ->
      boxSize = Math.ceil($scope.config.teamCount / 2)
      box = Math.floor(rank / boxSize)
      boxPos = (rank) % boxSize
      rankInc = $scope.config.fullColor / boxSize
      secInc = ($scope.config.fullColor - $scope.config.darkColor) / boxSize
      r = 0
      g = 0
      b = 0
      colorChange = Math.round(rankInc * boxPos)
      colorChange2 = Math.round(secInc * boxPos)
      if (box == 0)
        r = 0 + colorChange;
        g = $scope.config.darkColor + colorChange2

      if (box == 1)
        r = $scope.config.fullColor
        g = $scope.config.fullColor - colorChange

      return [ r, g, b ]

    # Helper functions for team drawing
    $scope._coorTop = (d) ->
      return (parseFloat(computeCoord(d.longtitude, d.latitude)[1]) - 10) + "px"
    $scope._coorLeft = (d) ->
      return (parseFloat(computeCoord(d.longtitude, d.latitude)[0]) + 15) + "px"

)