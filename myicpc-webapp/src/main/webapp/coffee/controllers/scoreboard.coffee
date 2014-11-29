scoreboard = angular.module('scoreboard', []);

scoreboard.controller('scoreboardCtrl', ($scope) ->
  $scope.teams = {}
  $scope.filterBy = null
  $scope.filterValue = null

  $scope.init = (teams) ->
    $scope.$apply(() ->
      $scope.teams = teams
    )

  $scope.filterTeam = (team) ->
    return true if not $scope.filterBy?;
    team[$scope.filterBy] is $scope.filterValue

  $scope.clearFilter = () ->
    $scope.filterBy = null
    $scope.filterValue = null

  $scope.isFilteredBy = (filtredBy) ->
    $scope.filterBy is filtredBy

  $scope.filterByNationality = (nationality) ->
    $scope.filterBy = 'nationality'
    $scope.filterValue = nationality

  $scope.filterByUniversity = (university) ->
    $scope.filterBy = 'universityName'
    $scope.filterValue = university

  $scope.filterByRegion = (region) ->
    $scope.filterBy = 'regionId'
    $scope.filterValue = region

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