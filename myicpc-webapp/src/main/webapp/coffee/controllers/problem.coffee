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
    }).otherwise({
      redirectTo: '/attempts'
    });
]);

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

  return problemService
)

problemApp.controller('problemCtrl', ($scope, problemService) ->
  $scope.addTeamSubmission = problemService.addTeamSubmission
)

problemApp.controller('problemAttempsCtrl', ($scope, $rootScope, $http) ->
  $scope.dataLoaded = false
  $scope.submissions = []

  $scope.init = (contextPath, contestCode, problemCode) ->
    $rootScope.activeTab = 'attempts'
    url = "#{contextPath}/#{contestCode}/problem/#{problemCode}/attempts-data"
    $http.get(url).success((data) ->
      $scope.submissions = data
      $scope.dataLoaded = true
    ).error(() ->
      # TODO
    ).finally(() ->
      $scope.dataLoaded = true
    )
#    $scope.$apply()

  $scope.$on('addSubmission', (event, submission) ->
    $scope.$apply(() ->
      if submission.judged
        index = -1
        for i in [0..$scope.submissions.length-1]
          if ($scope.submissions[i].id == submission.id)
            index = i
        if index == -1
          $scope.submissions.unshift(submission)
        else
          $scope.submissions[index] = submission
      else
        $scope.submissions.unshift(submission)
    )
  )
  
  $scope.formatTime = (time) ->
    formatContestTime(time)

  $scope.calcProgressPercentage = (submission) ->
    if submission.testcases? && submission.testcases > 0
      return Math.round(100 * submission.passed / submission.testcases)
    return 0
)

problemApp.controller('problemTeamsCtrl', ($scope, $rootScope) ->

  $scope.init = (contextPath, contestCode, problemCode) ->
    $rootScope.activeTab = 'teams'

  $scope.$on('addSubmission', (event, submission) ->
    $scope.$apply(() ->
      console.log('ahoj')
    )
  )
)

updateProblemView = (data, ngController) ->
  ngController.addTeamSubmission(data)
