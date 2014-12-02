teamDetail = angular.module('teamDetail', ['nvd3ChartDirectives']);

teamDetail.controller('TeamDeatilCtrl', ($scope) ->
  $scope.rankHistory = [];

  $scope.setRankHistory = (rankHistory) ->
    $scope.$apply(() ->
      $scope.rankHistory = rankHistory
    )
)