scoreboard = angular.module('scoreboard', []);

scoreboard.controller('scoreboardCtrl', ($scope) ->
  $scope.teams = {}
  $scope.message = "dasdsa"

  $scope.init = (teams) ->
    $scope.$apply(() ->
      $scope.teams = teams
    )

  $scope.isFirstSolvedSubmission = (team, problemId) ->
    return false if not team.teamProblems[problemId]?
    team.teamProblems[problemId].judged is true and team.teamProblems[problemId].solved is true and team.teamProblems[problemId].first is true

  $scope.isSolvedSubmission = (team, problemId) ->
    return false if not team.teamProblems[problemId]?
    team.teamProblems[problemId].judged is true and team.teamProblems[problemId].solved is true and !team.teamProblems[problemId].first

  $scope.isFailedSubmission = (team, problemId) ->
    return false if not team.teamProblems[problemId]?
    team.teamProblems[problemId].judged is true and team.teamProblems[problemId].solved is false

  $scope.isPendingSubmission = (team, problemId) ->
    return false if not team.teamProblems[problemId]?
    team.teamProblems[problemId].judged is false and !team.teamProblems[problemId].solved

)