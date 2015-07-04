problemApp = angular.module('problem', ['ngRoute', 'nvd3ChartDirectives']);

# routing config on the insight page
problemApp.config(['$routeProvider',
  ($routeProvider) ->
    $routeProvider.when('/attempts', {
      templateUrl: 'attemps-template',
      controller: 'problemAttempsCtrl'
    }).when('/teams', {
      templateUrl: 'teams-template',
      controller: 'problemTeamsCtrl'
    }).when('/overview', {
      templateUrl: 'overview-template',
      controller: 'problemOverviewCtrl'
    }).otherwise({
      redirectTo: '/attempts'
    });
]);

problemApp.directive('doubleTick', () ->
  {
  restrict: 'E',
  scope: {
    condition: '=ngModel'
  },
  template: '<span class="glyphicon glyphicon-ok" ng-if="condition"></span>' +
    '<span class="glyphicon glyphicon-remove" ng-if="!condition"></span>'
  }
)

problemApp.directive('tripleTick', () ->
  {
    restrict: 'E',
    scope: {
      submission: '=ngModel'
    },
    template: '<span class="glyphicon glyphicon-ok" ng-if="submission.judged && submission.solved"></span>' +
      '<span class="glyphicon glyphicon-remove" ng-if="submission.judged && !submission.solved"></span>' +
      '<span class="glyphicon glyphicon-time"ng-if="!submission.judged"></span>'
  }
)

problemApp.directive('solutionBar', () ->
  {
    restrict: 'E',
    scope: {
      submission: '=ngModel'
    },
    template: '<div class="progress" title="Passed {{submission.passed}} out of {{submission.testcases}}">
                    <div class="progress-bar progress-bar-success" role="progressbar" style="width: {{calcProgressPercentage(submission)}}%;" style="min-width: 2em; width: 2%;">
                        <span>{{submission.passed}} Passed</span>
                    </div>
                    <div class="progress-bar progress-bar-danger" role="progressbar" style="width: {{100 - calcProgressPercentage(submission)}}%;" style="min-width: 2em; width: 2%;">
                        <span>{{submission.testcases - submission.passed}} Failed</span>
                    </div>
                </div>',
    controller: ($scope) ->
      $scope.calcProgressPercentage = (submission) ->
        if submission.testcases? && submission.testcases > 0
          return Math.round(100 * submission.passed / submission.testcases)
        return 0
  }
)

problemApp.factory('problemService', ($rootScope) ->
  problemService = {}

  problemService.addTeamSubmission = (submission) ->
    $rootScope.$broadcast("addSubmission", submission);

  problemService.getSubmissionIndex = (submissions, submission) ->
    index = -1
    if submissions? && submissions.length > 0
      for i in [0..submissions.length-1] by 1
        if (submissions[i].id == submission.id)
          index = i
          break
    return index

  return problemService
)

problemApp.controller('problemCtrl', ($scope, $rootScope, problemService) ->
  $scope.addTeamSubmission = problemService.addTeamSubmission

  $scope.setJudgements = (judgements) ->
    $rootScope.judgements = judgements
)

problemApp.controller('problemAttempsCtrl', ($scope, $rootScope, $http, problemService) ->
  $scope.dataLoaded = false
  $scope.submissions = []

  $scope.init = (contextPath, contestCode, problemCode) ->
    $rootScope.activeTab = 'attempts'
    url = "#{contextPath}/#{contestCode}/problem/#{problemCode}/attempts-data"
    $http.get(url).success((data) ->
      $scope.submissions = data
    ).error(() ->
      # TODO
    ).finally(() ->
      $scope.dataLoaded = true
    )

  $scope.$on('addSubmission', (event, submission) ->
    $scope.$apply(() ->
      if submission.judged
        index = problemService.getSubmissionIndex($scope.submissions, submission)
        if index == -1
          $scope.submissions.unshift(submission)
        else
          $scope.submissions[index] = submission
      else
        $scope.submissions.unshift(submission)
    )
  )
  
  $scope.formatTime = formatContestTime

  $scope.calcProgressPercentage = (submission) ->
    if submission.testcases? && submission.testcases > 0
      return Math.round(100 * submission.passed / submission.testcases)
    return 0
)

problemApp.controller('problemTeamsCtrl', ($scope, $rootScope, $http, problemService) ->
  $scope.dataLoaded = false
  $scope.teams = []

  $scope.init = (contextPath, contestCode, problemCode) ->
    $rootScope.activeTab = 'teams'
    url = "#{contextPath}/#{contestCode}/problem/#{problemCode}/attempts-data"
    $http.get(url).success((submissions) ->
      if submissions.length > 0
        teamsObject = {}
        for i in [0..submissions.length-1] by 1
          submission = submissions[i]

          createJudgementDetail(submission)

          if !teamsObject[submission.teamExternalId]?
            teamsObject[submission.teamExternalId] = createTeamFromSubmission(submission)
          if !teamsObject[submission.teamExternalId].solved
            teamsObject[submission.teamExternalId].solved = submission.solved
          teamsObject[submission.teamExternalId].submissions.push(submission)
        $scope.teams = Object.keys(teamsObject).map((key) -> teamsObject[key])
    ).error(() ->
      # TODO
    ).finally(() ->
      $scope.dataLoaded = true
    )

  $scope.$on('addSubmission', (event, submission) ->
    $scope.$apply(() ->
      createJudgementDetail(submission)
      teamIndex = -1
      if ($scope.teams.length > 0)
        for i in [0..$scope.teams.length-1] by 1
          if $scope.teams[i].teamExternalId == submission.teamExternalId
            teamIndex = i

      if teamIndex == -1
        team = createTeamFromSubmission(submission)
        team.submissions.push(submission)
        $scope.teams.push(team)
      else
        if !$scope.teams[teamIndex].solved
          $scope.teams[teamIndex].solved = submission.solved

        index = problemService.getSubmissionIndex($scope.teams[teamIndex].submissions, submission)
        if index == -1
          $scope.teams[teamIndex].submissions.unshift(submission)
        else
          $scope.teams[teamIndex].submissions[index] = submission
    )
  )

  $scope.formatTime = formatContestTime

  createJudgementDetail = (submission) ->
    if submission?
      judgement = $rootScope.judgements[submission.judgement]
      if judgement?
        submission.judgementName = judgement.name
        submission.judgementColor = judgement.color

  createTeamFromSubmission = (submission) ->
    return {
        teamExternalId: submission.teamExternalId
        teamName: submission.teamName
        submissions: []
        solved: submission.solved
      }

)

problemApp.controller('problemOverviewCtrl', ($scope, $rootScope, $http, problemService) ->
  $scope.dataLoaded = false
  $scope.data = []

  $scope.init = (contextPath, contestCode, problemCode) ->
    $rootScope.activeTab = 'overview'
    url = "#{contextPath}/#{contestCode}/problem/#{problemCode}/overview-data"
    $http.get(url).success((data) ->
      $scope.data = [{
        "key": "Passed tests",
        "color": '#5cb85c'
        "values": data
      }]
    ).error(() ->
      # TODO
    ).finally(() ->
      $scope.dataLoaded = true
    )

  $scope.$on('addSubmission', (event, submission) ->
    $scope.$apply(() ->
      if submission.judged
        index = -1
        console.log($scope.data[0])
        if $scope.data[0].values.length > 0
          for i in [0..$scope.data[0].values.length-1] by 1
            if $scope.data[0].values[i][0].teamExternalId == submission.teamExternalId
              index = i
              break

        $scope.data[0].values[i][1] = submission
    )
  )

  $scope.xFunction = ->
    (d) ->
      d[0].teamName

  $scope.yFunction = ->
    (d) ->
      d[1].passed

  format = d3.format(',.0f')
  $scope.valueFormatFunction = ->
    (d) ->
      format d

  $scope.toolTipContentFunction = ->
    (key, x, y, e, graph) ->
      d = e.point[1]
      "<strong>#{x}</strong><br/>" +
      "Time: #{formatContestTime(d.time)}<br/>" +
      "Language:&nbsp;#{d.language}<br/>" +
      "Judgement: #{d.judgement}"


)

updateProblemView = (data, ngController) ->
  ngController.addTeamSubmission(data)
