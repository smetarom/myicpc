scoreboard = angular.module('quest', []);

scoreboard.controller('leaderboardCtrl', ($scope) ->
  # Participants
  $scope.participants = []
  # Challenges
  $scope.challenges = []

  $scope.init = (challenges, participants) ->
    $scope.$apply(() ->
      $scope.challenges = challenges
      $scope.participants = participants
    )


)