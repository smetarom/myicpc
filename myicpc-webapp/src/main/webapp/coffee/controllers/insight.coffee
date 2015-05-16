insightApp = angular.module('insight', ['ngRoute', 'nvd3ChartDirectives']);

# routing config on the insight page
insightApp.config(['$routeProvider',
  ($routeProvider) ->
    $routeProvider.when('/problems', {
        templateUrl: 'insight/template/all-problems',
        controller: 'allProblemsCtrl'
    }).when('/team-problems', {
      templateUrl: 'insight/template/team-problems',
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

# create a common service for all insight pages
insightApp.factory('insightService', ($http, $interval) ->
  insightService = {};
  promise = null;

  ###
    refresh rate for polling new insight data
  ###
  insightService.refreshRate = 15000

  ###
    loads data at the beginning and initiate insight data polling
    @param url server resource URL
    @param title temporary title before the title is loaded
    @param successFn initial success load function
    @param pollingFn function, which process periodically polled data
  ###
  insightService.init = (url, title, successFn, pollingFn) ->
    if promise?
      $interval.cancel(promise)
    console.log(promise);
    $("#insightHeadline").html("#{title}")
    $http.get(url).success(successFn)
    .error(() ->
      # TODO
    )
    promise = $interval(() ->
      $http.get(url).success(pollingFn)
    , insightService.refreshRate);

  ###
      get x value for pie chart
    ###
  insightService.xFunction = ->
    (d) ->
      d.key
  ###
    get y value for pie chart
  ###
  insightService.yFunction =  ->
    (d) ->
      d.value

  ###
    get area color for pie chart
  ###
  insightService.areaColor =  ->
    (d, i) ->
      d.data.color

  ###
    construct a label for a pie chart
  ###
  insightService.toolTipContentFunction = () ->
    return (key, x, y, e, graph) ->
      return '<p><strong>' + key + '</strong>' + ' - ' + Math.round(x) + ' submissions</p>'

  return insightService;
)

###
  controller for view with all problems
###
insightApp.controller('allProblemsCtrl', ($scope, $http, $interval, insightService) ->
  # insight data about all problems
  $scope.data = {}
  # all contest problems
  $scope.problems = null

  ###
    loads data at the beginning and initiate insight data polling
    @param contextPath app context path
    @param contestCode contest code
    @param title temporary title before the title is loaded
    @see insightService#init
  ###
  $scope.init = (contextPath, contestCode, title) ->
    url = "#{contextPath}/#{contestCode}/insight/ajax/all-problems"
    $scope._init(url, contextPath, contestCode, title)

  $scope.initForTeam = (teamId, contextPath, contestCode, title) ->
    url = "#{contextPath}/#{contestCode}/insight/ajax/team-problems/#{teamId}"
    console.log(url)
    $scope._init(url, contextPath, contestCode, title)

  $scope._init = (url, contextPath, contestCode, title) ->
    successFn = (data) ->
      for element in data.data
        $scope.data[element.code] = element.data
      $scope.problems = data.problems
    pollingFn = (data) ->
      for element in data.data
        $scope.data[element.code] = element.data
    insightService.init(url, title, successFn, pollingFn)

  ###
    get insight data for a given problem
    @param problemCode code of the problem
    @return problem insight data if exists
  ###
  $scope.getChartData = (problemCode) ->
    if ($scope.data[problemCode]?)
      return $scope.data[problemCode]

  ###
    count the total number of submissions per problem
    @param code of the problem
    @return total number of submissions
  ###
  $scope.submissionsPerProblem = (problemCode) ->
    total = 0
    if ($scope.data[problemCode]?)
      for elem in $scope.data[problemCode]
        total += elem.value
    return total

  ###
    @see insightService#xFunction
  ###
  $scope.xFunction = insightService.xFunction

  ###
    @see insightService#yFunction
  ###
  $scope.yFunction =  insightService.yFunction

  ###
    @see insightService#areaColor
  ###
  $scope.areaColor =  insightService.areaColor

  ###
    @see insightService#toolTipContentFunction
  ###
  $scope.toolTipContentFunction = insightService.toolTipContentFunction
)

###
  controller for a detailed view of a single problem
###
insightApp.controller('problemDetailCtrl', ($scope, $http, $interval, $routeParams, insightService) ->
  # problem insight data
  $scope.data = null

  ###
    loads data at the beginning and initiate insight data polling
    @param contextPath app context path
    @param contestCode contest code
    @param title temporary title before the title is loaded
    @see insightService#init
  ###
  $scope.init = (contextPath, contestCode, title) ->
    $("#insightHeadline").html("#{title} #{$routeParams.problemCode}")
    title = "#{title} #{$routeParams.problemCode}"
    url = "#{contextPath}/#{contestCode}/insight/ajax/problem/#{$routeParams.problemCode}"
    successFn = (data) ->
      $scope.data = data.data
      $("#insightHeadline").html(data.title)
    pollingFn = (data) ->
      $scope.data = data.data
    insightService.init(url, title, successFn, pollingFn)

  ###
    @see insightService#xFunction
  ###
  $scope.xFunction = insightService.xFunction

  ###
    @see insightService#yFunction
  ###
  $scope.yFunction =  insightService.yFunction

  ###
    @see insightService#areaColor
  ###
  $scope.areaColor =  insightService.areaColor

  ###
    @see insightService#toolTipContentFunction
  ###
  $scope.toolTipContentFunction = insightService.toolTipContentFunction

)

###
  controller for view with all languages
###
insightApp.controller('allLanguagesCtrl', ($scope, $http, $interval, insightService) ->
  # insight data about all languages
  $scope.data = null

  ###
    loads data at the beginning and initiate insight data polling
    @param contextPath app context path
    @param contestCode contest code
    @param title temporary title before the title is loaded
    @see insightService#init
  ###
  $scope.init = (contextPath, contestCode, title) ->
    url = "#{contextPath}/#{contestCode}/insight/ajax/all-languages"
    successFn = (data) ->
      $scope.data = data.data
      $("#insightHeadline").html(data.title)
    pollingFn = (data) ->
      $scope.data = data.data
    insightService.init(url, title, successFn, pollingFn)
)

###
  controller for a detailed view of a single language
###
insightApp.controller('languageDetailCtrl', ($scope, $http, $interval, $routeParams, insightService) ->
  # language insight data
  $scope.data = null
  # language name
  $scope.languageName = $routeParams.languageName

  ###
    loads data at the beginning and initiate insight data polling
    @param contextPath app context path
    @param contestCode contest code
    @param title temporary title before the title is loaded
    @see insightService#init
  ###
  $scope.init = (contextPath, contestCode, title) ->
    title = "#{title} #{$routeParams.languageName}"
    url = "#{contextPath}/#{contestCode}/insight/ajax/language/#{$routeParams.languageName}"
    successFn = (data) ->
      $scope.data = data.data
      $("#insightHeadline").html(data.title)
    pollingFn = (data) ->
      $scope.data = data.data
    insightService.init(url, title, successFn, pollingFn)

  ###
    @see insightService#xFunction
  ###
  $scope.xFunction = insightService.xFunction

  ###
    @see insightService#yFunction
  ###
  $scope.yFunction =  insightService.yFunction

  ###
    @see insightService#areaColor
  ###
  $scope.areaColor =  insightService.areaColor

  ###
    @see insightService#toolTipContentFunction
  ###
  $scope.toolTipContentFunction = insightService.toolTipContentFunction
)

###
  controller for a vie with code insight
###
insightApp.controller('codeInsightCtrl', ($scope, $http, $interval, insightService) ->
  # insight data about all problems
  $scope.data = {}
  # all contest problems
  $scope.problems = null
  $scope.mode = 'DIFF'

  ###
    loads data at the beginning and initiate insight data polling
    @param contextPath app context path
    @param contestCode contest code
    @param title temporary title before the title is loaded
    @see insightService#init
  ###
  $scope.init = (contextPath, contestCode, title) ->
    url = "#{contextPath}/#{contestCode}/insight/ajax/codeInsight"
    $scope._init(url, contextPath, contestCode, title)

  $scope._init = (url, contextPath, contestCode, title) ->
    successFn = (data) ->
      $scope.data = data.data
      $scope.problems = data.problems
    pollingFn = (data) ->
      $scope.data = data.data
    insightService.init(url, title, successFn, pollingFn)

  ###
    get insight data for a given problem
    @param problemCode code of the problem
    @return problem insight data if exists
  ###
  $scope.getChartData = (problemCode) ->
    if ($scope.data[problemCode]?)
      return $scope.data[problemCode]

  $scope.toolTipContentFunction = () ->
    (key, x, y, e, graph) ->
      text = if $scope.mode == 'DIFF' then 'Has changed ' else 'Has total '
      return '<p><strong>' + x + '</strong></p>' +
          "<p>" + text + Math.round(y) + ' lines in ' + key + ' min</p>'
)