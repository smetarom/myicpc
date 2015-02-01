scorebar = angular.module('worldMap', []);

scorebar.controller('worldMapCtrl', ($scope) ->
  $scope.teams = []
  $scope.config = {
    centered: null,
    config: null,
    projection: null,
    feature: null,
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

  $scope.renderMap = (appPath, config) ->
    $scope.config = $.extend({
      width: 1000,
      ratio: 0.65,
      scale: 150,
      translate: [ 490, 380 ],
      circleSize: 6
    }, config)

    $scope.config.teamCount = $scope.teams.length
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
        ).on("click", onCountryClick);
      $scope.config.feature.append("svg:title").text((d) ->
        return d.properties.name
      )
    )

)