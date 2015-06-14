pollApp = angular.module('poll', ['ngRoute', 'nvd3ChartDirectives']);

pieChartType = 'PIE_CHART'
barChartType = 'BAR_CHART'

# routing config on the poll page
pollApp.config(['$routeProvider',
  ($routeProvider) ->
    $routeProvider.when('/', {
      templateUrl: 'poll/overview-template',
      controller: 'allPollsCtrl'
    }).when('/:pollId', {
      templateUrl: 'poll/detail-template',
      controller: 'pollDetailCtrl'
    }).otherwise({
      redirectTo: '/'
    });
]);

# create a common service for all poll pages
pollApp.factory('pollService', ($http) ->
  pollService = {};

  pollService.countPollAnsweredOptions = (poll) ->
    if pollService.isPieChart(poll)
      return 0
    else if pollService.isBarChart(poll)
      if poll.chart.length > 0
        return poll.chart[0].values.length

  pollService.isPieChart = (poll) ->
    poll.type == pieChartType

  pollService.isBarChart = (poll) ->
    poll.type == barChartType

  pollService.xFunction = ->
    (d) ->
      d.name

  pollService.xFunctionShort = ->
    (d) ->
      d.name.substring(0, 25)

  pollService.yFunction = ->
    (d) ->
      d.value

  pollService.yAxisTickFormatFunction = ->
    (d) ->
      if d % 1 == 0 then d else ''

  pollService.pieToolTipContent = ->
    (key, x, y, e, graph) ->
      '<strong>' + key + ' (' + y.value + ')</strong>'

  format = d3.format(',f')
  # Format value for chart
  pollService.valueFormatFunction = ->
    (d) ->
      format(d)

  # Set tooltip for chart
  pollService.toolTipContentFunction = ->
    (key, x, y, e, graph) ->
      '<strong>' + e.point.name + '</strong>'

  # Set tooltip for chart on the phone view
  pollService.toolTipContentFunctionMobile = ->
    (key, x, y, e, graph) ->
      '<strong>' + x + ' (' + format(y) + ')</strong>'

  return pollService;
)

pollApp.controller('pollsCtrl', ($scope, $rootScope, $http, pollService) ->
  $rootScope.polls = []

  $scope.init = (contextPath, contestCode, data) ->
    $rootScope.$apply(() ->
      $rootScope.polls = data
      $rootScope.contextPath = contextPath
      $rootScope.contestCode = contestCode
    )

  $scope.update = (data) ->
    $rootScope.$apply(() ->
      poll = _.find($rootScope.polls, (p) -> p.id == data.pollId)
      console.log(poll)
      if poll?
        if pollService.isPieChart(poll)
          option = _.find(poll.chart, (o) -> o.key == data.optionId)
          if option?
            option.value = data.votes
          else
            poll.chart.push({key:data.optionId, name:data.optionName, value:data.votes})
        else if pollService.isBarChart(poll)
          option = _.find(poll.chart[0].values, (o) -> o.key == data.optionId)
          if option?
            option.value = data.votes
          else
            poll.chart[0].values.push({key:data.optionId, name:data.optionName, value:data.votes})
    )

  $rootScope.submitAnswer = (pollId, optionId) ->
    $http.post("#{$rootScope.contextPath}/#{$rootScope.contestCode}/poll/submit-answer", {}, {
      params: {pollId: pollId, optionId: optionId}
    }).success(() ->
      poll = _.find($rootScope.polls, (p) -> p.id == pollId)
      if poll? then poll.answered = true
    );
)

pollApp.controller('allPollsCtrl', ($scope, $rootScope, pollService) ->

  initCommonFunctions($scope, pollService)

)


pollApp.controller('pollDetailCtrl', ($scope, $rootScope, $routeParams, pollService) ->
  $scope.poll = {}
  $scope.pollIndex = -1

  $scope.init = ->
    if $rootScope.polls.length > 0
      pollId = parseInt($routeParams.pollId)
      for i in [0..$rootScope.polls.length-1]
        if $rootScope.polls[i].id == pollId
          $scope.pollIndex = i
          $scope.poll = $rootScope.polls[$scope.pollIndex]
          break

  initCommonFunctions($scope, pollService)

  $scope.selectOptions = (poll) ->
    if pollService.isPieChart(poll)
      return poll.chart
    else if pollService.isBarChart(poll)
      if poll.chart.length > 0
        return poll.chart[0].values
    return []
)

initCommonFunctions = ($scope, pollService) ->
  $scope.xFunction = pollService.xFunction
  $scope.xFunctionShort = pollService.xFunctionShort
  $scope.yFunction = pollService.yFunction
  $scope.yAxisTickFormatFunction = pollService.yAxisTickFormatFunction
  $scope.pieToolTipContent = pollService.pieToolTipContent
  $scope.valueFormatFunction = pollService.valueFormatFunction
  $scope.toolTipContentFunction = pollService.toolTipContentFunction
  $scope.toolTipContentFunctionMobile = pollService.toolTipContentFunctionMobile
  $scope.countPollAnsweredOptions = pollService.countPollAnsweredOptions
  $scope.isPieChart = pollService.isPieChart
  $scope.isBarChart = pollService.isBarChart

updatePollView = (data, ngController) ->
  ngController.update(data)