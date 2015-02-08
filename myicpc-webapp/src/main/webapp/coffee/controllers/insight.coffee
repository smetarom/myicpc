insightApp = angular.module('insight', ['ngRoute', 'nvd3ChartDirectives']);

# routing config
insightApp.config(['$routeProvider',
  ($routeProvider) ->
    $routeProvider.when('/problems', {
        templateUrl: 'insight/template/all-problems',
        controller: 'allProblemsCtrl'
    }).when('/problem/:problemCode', {
      templateUrl: 'insight/template/problem-detail',
      controller: 'problemDetailCtrl'
    }).when('/languages', {
      templateUrl: 'insight/template/all-languages',
      controller: 'allLanguagesCtrl'
    }).when('/language/:languageName', {
      templateUrl: 'insight/template/language-detail',
      controller: 'languageDetailCtrl'
    }).when('/code', {
      templateUrl: 'insight/template/code-insight',
      controller: 'codeInsightCtrl'
    }).otherwise({
      redirectTo: '/problems'
    });
]);

insightApp.factory('insightService', () ->
  insightService = {};

  insightService.refreshRate = 5000

  insightService.xFunction = ->
    (d) ->
      d.key

  insightService.yFunction =  ->
    (d) ->
      d.value

  insightService.areaColor =  ->
    (d, i) ->
      d.data.color

  insightService.toolTipContentFunction = () ->
    return (key, x, y, e, graph) ->
      return '<p><strong>' + key + '</strong>' + ' - ' + Math.round(x) + ' submissions</p>'

  return insightService;
)

insightApp.controller('allProblemsCtrl', ($scope, $http, $interval, insightService) ->
  $scope.data = {}
  $scope.problems = null

  $scope.init = (contextPath, contestCode, title) ->
    $("#insightHeadline").html("#{title}")
    $http.get("#{contextPath}/#{contestCode}/insight/ajax/all-problems").success((data) ->
      for element in data.data
        $scope.data[element.code] = element.data
      $scope.problems = data.problems
      $("#insightHeadline").html(data.title)
    ).error(() ->
      # TODO
    )
    completed = $interval(() ->
      $http.get("#{contextPath}/#{contestCode}/insight/ajax/all-problems").success((data) ->
        for element in data.data
          $scope.data[element.code] = element.data
      )
    , insightService.refreshRate);

  $scope.getChartData = (problemCode) ->
    if ($scope.data[problemCode]?)
      return $scope.data[problemCode]

  $scope.submissionsPerProblem = (problemCode) ->
    total = 0
    if ($scope.data[problemCode]?)
      for elem in $scope.data[problemCode]
        total += elem.value
    return total

  $scope.xFunction = insightService.xFunction

  $scope.yFunction =  insightService.yFunction

  $scope.areaColor =  insightService.areaColor

  $scope.toolTipContentFunction = insightService.toolTipContentFunction
)

insightApp.controller('problemDetailCtrl', ($scope, $http, $interval, $routeParams, insightService) ->
  $scope.data = null

  $scope.init = (contextPath, contestCode, title) ->
    $("#insightHeadline").html("#{title} #{$routeParams.problemCode}")
    $http.get("#{contextPath}/#{contestCode}/insight/ajax/problem/#{$routeParams.problemCode}").success((data) ->
      $scope.data = data.data
      $("#insightHeadline").html(data.title)
    ).error(() ->
      # TODO
    )
    completed = $interval(() ->
      $http.get("#{contextPath}/#{contestCode}/insight/ajax/problem/#{$routeParams.problemCode}").success((data) ->
        $scope.data = data.data
      )
    , insightService.refreshRate);

  $scope.xFunction = insightService.xFunction

  $scope.yFunction =  insightService.yFunction

  $scope.areaColor =  insightService.areaColor

  $scope.toolTipContentFunction = insightService.toolTipContentFunction

)

insightApp.controller('allLanguagesCtrl', ($scope, $http, $interval, insightService) ->
  $scope.data = null

  $scope.init = (contextPath, contestCode, title) ->
    $("#insightHeadline").html("#{title}")
    $http.get("#{contextPath}/#{contestCode}/insight/ajax/all-languages").success((data) ->
      $scope.data = data.data
      $("#insightHeadline").html(data.title)
    ).error(() ->
      # TODO
    )
    completed = $interval(() ->
      $http.get("#{contextPath}/#{contestCode}/insight/ajax/all-languages").success((data) ->
        $scope.data = data.data
      )
    , insightService.refreshRate);
)

insightApp.controller('languageDetailCtrl', ($scope, $http, $interval, $routeParams, insightService) ->
  $scope.data = null
  $scope.languageName = $routeParams.languageName

  $scope.init = (contextPath, contestCode, title) ->
    $("#insightHeadline").html("#{title} #{$routeParams.languageName}")
    $http.get("#{contextPath}/#{contestCode}/insight/ajax/language/#{$routeParams.languageName}").success((data) ->
      $scope.data = data.data
      $("#insightHeadline").html(data.title)
    ).error(() ->
      # TODO
    )
    completed = $interval(() ->
      $http.get("#{contextPath}/#{contestCode}/insight/ajax/language/#{$routeParams.languageName}").success((data) ->
        $scope.data = data.data
      )
    , insightService.refreshRate);


  $scope.xFunction = insightService.xFunction

  $scope.yFunction =  insightService.yFunction

  $scope.areaColor =  insightService.areaColor

  $scope.toolTipContentFunction = insightService.toolTipContentFunction
)

insightApp.controller('codeInsightCtrl', ($scope, $http) ->
  $scope.data = null

  $scope.init = (contextPath, contestCode, title) ->
    $("#insightHeadline").html("#{title}")

)