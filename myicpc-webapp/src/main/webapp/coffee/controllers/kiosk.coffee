kioskApp = angular.module('kiosk', ['wu.masonry']);

kioskApp.controller('kioskFeedCtrl', ($scope, $sce) ->
  $scope.notifications = {}

  $scope.init = (notifications) ->
    $scope.$apply(() ->
      $scope.notifications = notifications
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

