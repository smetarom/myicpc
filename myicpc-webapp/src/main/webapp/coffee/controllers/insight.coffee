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

insightApp.controller('allProblemsCtrl', ($scope, $http) ->
  $scope.data = null
  $scope.problems = []

  $scope.init = (contextPath, contestCode, title) ->
    $("#insightHeadline").html("#{title}")
    $http.get("#{contextPath}/#{contestCode}/insight/ajax/all-problems").success((data) ->
      $scope.data = data.data
      $scope.problems = data.problems
      $("#insightHeadline").html(data.title)
    ).error(() ->
      # TODO
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

  $scope.toolTipContentFunction = () ->
    return (key, x, y, e, graph) ->
      return '<p><strong>' + key + '</strong>' + ' - ' + Math.round(x) + ' submissions</p>'
)

insightApp.controller('problemDetailCtrl', ($scope, $http, $routeParams) ->
  $scope.data = null

  $scope.init = (contextPath, contestCode, title) ->
    $("#insightHeadline").html("#{title} #{$routeParams.problemCode}")
    $http.get("#{contextPath}/#{contestCode}/insight/ajax/problem/#{$routeParams.problemCode}").success((data) ->
      console.log(data)
      $scope.data = data.data
      $("#insightHeadline").html(data.title)
    ).error(() ->
      # TODO
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

  $scope.toolTipContentFunction = () ->
    return (key, x, y, e, graph) ->
      return '<p><strong>' + key + '</strong>' + ' - ' + Math.round(x) + ' submissions</p>'

)

insightApp.controller('allLanguagesCtrl', ($scope, $http) ->
  $scope.data = null

  $scope.init = (contextPath, contestCode, title) ->
    $("#insightHeadline").html("#{title}")
    $http.get("#{contextPath}/#{contestCode}/insight/ajax/all-languages").success((data) ->
      $scope.data = data.data
      $("#insightHeadline").html(data.title)
    ).error(() ->
      # TODO
    )
)

insightApp.controller('languageDetailCtrl', ($scope, $http, $routeParams) ->
  $scope.data = null
  $scope.languageName = $routeParams.languageName

  $scope.init = (contextPath, contestCode, title) ->
    $("#insightHeadline").html("#{title} #{$routeParams.languageName}")
    $http.get("#{contextPath}/#{contestCode}/insight/ajax/language/#{$routeParams.languageName}").success((data) ->
      console.log(data)
      $scope.data = data.data
      $("#insightHeadline").html(data.title)
    ).error(() ->
      # TODO
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

  $scope.toolTipContentFunction = () ->
    return (key, x, y, e, graph) ->
      return '<p><strong>' + key + '</strong>' + ' - ' + Math.round(x) + ' submissions</p>'
)

insightApp.controller('codeInsightCtrl', ($scope, $http) ->
  $scope.data = null

  $scope.init = (contextPath, contestCode, title) ->
    $("#insightHeadline").html("#{title}")

)