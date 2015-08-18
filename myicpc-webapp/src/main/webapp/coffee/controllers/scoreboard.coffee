scoreboard = angular.module('scoreboard', []);

scoreboard.controller('scoreboardCtrl', ($scope) ->
  $scope.teams = []
  $scope.filterBy = null
  $scope.filterValue = null
  $scope.followedTeamIds = getCookieAsIntArray('followedTeams')

  $scope.init = (teams) ->
    $scope.$apply(() ->
      for i in [0..teams.length-1] by 1
        teams[i].followed = teams[i].teamId in $scope.followedTeamIds
      $scope.teams = teams
    )

  $scope.updateRank = (teamId, rank) ->
    obj = _.find($scope.teams, (obj) ->
      obj.teamId == teamId
    )
    obj.teamRank = rank if obj?

  $scope.updateNumSolvedAndTotalTime = (teamId, numSolved, totalTime) ->
    obj = _.find($scope.teams, (obj) ->
      obj.teamId == teamId
    )
    if obj?
      obj.nSolved = numSolved;
      obj.totalTime = totalTime;

  $scope.updateTeamProblem = (teamId, problemId, judged, solved, attempts, time, first) ->
    team = _.find($scope.teams, (obj) ->
      obj.teamId == teamId
    )
    if typeof team != "undefined" and typeof team.teamProblems != "undefined"
      if typeof team.teamProblems[problemId] == "undefined"
        team.teamProblems[problemId] = {}
      team.teamProblems[problemId].judged = judged
      team.teamProblems[problemId].solved = solved
      team.teamProblems[problemId].attempts = attempts
      team.teamProblems[problemId].time = time
      team.teamProblems[problemId].first = first

  $scope.filterTeam = (team) ->
    return true if not $scope.filterBy?;
    team[$scope.filterBy] is $scope.filterValue

  $scope.toogleTeamProblems = (team) ->
    team.showProblems = false if !team.showProblems?
    team.showProblems = team.showProblems == false ? true : false

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

  $scope.formatTime = (time) ->
    convertSecondsToMinutes(time)

  $scope.toggleFollowTeam = (team, cookiePath) ->
    teamId = team.teamId
    if teamId in $scope.followedTeamIds
      removeIdFromCookieArray('followedTeams', teamId, cookiePath)
      $scope.followedTeamIds = _.filter($scope.followedTeamIds, (id) ->
        return id != teamId
      )
      team.followed = false
    else
      appendIdToCookieArray('followedTeams', teamId, cookiePath)
      $scope.followedTeamIds.push(teamId)
      team.followed = true
    return false
)

updateScoreboard = (data, ngController = null) ->
  if data.type == 'submission'
    if ngController != null
      colorBg = "#ffff99"
      if data.judged
        colorBg = if data.solved then "#66FF33" else "#FF5C33"

      $(".team_" + data.teamId).effect("highlight", {color: colorBg}, 3000)

      ngController.$apply(() ->
        if data.teams.length > 0
          for i in [0..data.teams.length-1] by 1
            ngController.updateRank(data.teams[i][0], data.teams[i][1])

        if data.solved
          ngController.updateNumSolvedAndTotalTime(data.teamId, data.numSolved, data.total)

        ngController.updateTeamProblem(data.teamId, data.problemId, data.judged, data.solved, data.attempts, data.time, data.first)
      )


