kioskApp = angular.module('kiosk', ['wu.masonry']);

kioskApp.controller('kioskFeedCtrl', ($scope, $sce) ->
  $scope.notifications = {}

  $scope.init = (notifications) ->
    $scope.$apply(() ->
      $scope.notifications = notifications
      for i in [0..$scope.notifications.length-1] by 1
        if $scope.notifications[i].type == 'eventOpen'
          $scope.notifications[i].body = JSON.parse($scope.notifications[i].body)
        console.log($scope.notifications[i])
    )

  $scope.addTile = (data) ->
    $scope.notifications.push(data)

  $scope.refresh = ->
    $scope.$apply()

  $scope.trustedHTML = (html) ->
    return $sce.trustAsHtml(html)

  $scope.trustedResource = (url) ->
    if(url == null)
      return ""
    return $sce.trustAsResourceUrl(url)

  $scope.trustedYoutubeResource = (url) ->
    url += "?autoplay=1&loop=1"
    return $sce.trustAsResourceUrl(url)

)

kioskApp.controller('kioskCalendarCtrl', ($scope) ->
  $scope.days = []

  $scope.init = (days) ->
    $scope.$apply(() ->
      $scope.days = days
    )

  $scope.refresh = () ->
    $scope.$apply();

)

updateKioskFeed = (data, ngController = null) ->
  ngController.addTile(data)

scrollMeUp = ->
  $("html, body").animate({ scrollTop: 0 }, {duration: 5000})
  setTimeout(() ->
    scrollMeDown()
  , 5000)

scrollMeDown = () ->
  $("html, body").animate({ scrollTop: $(document).height() }, {duration: 5000})
  setTimeout(() ->
    scrollMeUp()
  , 60000)

