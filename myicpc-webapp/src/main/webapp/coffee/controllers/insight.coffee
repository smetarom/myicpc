insightApp = angular.module('insight', ['nvd3ChartDirectives']);

insightApp.controller('problemInsightCtrl', ($scope) ->
  $scope.problems = []

  $scope.init = (data) ->
    $scope.$apply(() ->
      $scope.problems = data
    )

  $scope.xFunction = ->
    (d) ->
      d.key

  $scope.yFunction =  ->
    (d) ->
      d.value

  $scope.areaColor =  ->
    (d, i) ->
      d.data.color
)


