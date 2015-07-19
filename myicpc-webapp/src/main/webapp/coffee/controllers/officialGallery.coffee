officialGalleryApp = angular.module('officialGallery', []);

OfficialGalleryConstants = {
  delimiter: ', ',
  eventPrefix: "event",
  teamPrefix: "team",
  personPrefix: "person",
}

officialGalleryApp.factory('officialGalleryService', ($http, $interval) ->
  officialGalleryAppService = {};

  officialGalleryAppService.buildSearchUrl = (config, tag, start = 1) ->
    if start < 1 then start = 1
    return "http://picasaweb.google.com/data/feed/api/user/#{config.picasaUser}/?kind=photo&q=Album#{config.year}%20\"#{tag}\"&" +
        "start-index=#{start}&max-results=#{config.maxResult}&thumbsize=#{config.thumbsize}c&imgmax=512&access=public&alt=json";

  officialGalleryAppService.buildCountUrl = (config, tag) ->
    return "http://picasaweb.google.com/data/feed/api/user/#{config.picasaUser}/?fields=openSearch:totalResults&" +
        "kind=photo&q=Album#{config.year}%20\"#{tag}\"&start-index=1&max-results=1000&access=public&alt=json";

  officialGalleryAppService.transformToGalleryEntities = (picasaResponse) ->
    if picasaResponse.feed.entry?
      results = []
      entries = picasaResponse.feed.entry
      if entries.length > 0
        for i in [0..entries.length-1] by 1
          entry = entries[i]
          result = {}
          result.imageUrl = entry['media$group']['media$content'][0].url;
          result.thumbnailUrl = entry['media$group']['media$thumbnail'][0].url;
          result.author = entry['media$group']['media$description']['$t'];
          appendTagsToEntity(result, entry)
          results.push(result)
      return results
    return []

  appendTagsToEntity = (entity, entry) ->
    entity.events = [];
    entity.teams = [];
    entity.people = [];
    keywords = entry['media$group']['media$keywords']['$t']
    tokens = keywords.split(OfficialGalleryConstants.delimiter)
    if tokens.length > 0
      for i in [0..tokens.length-1] by 1
        token = tokens[i].trim()
        if tagPrefix(token) == OfficialGalleryConstants.eventPrefix
          entity.events.push(sanitizeTag(token, OfficialGalleryConstants.eventPrefix))
        else if tagPrefix(token) == OfficialGalleryConstants.teamPrefix
          entity.teams.push(sanitizeTag(token, OfficialGalleryConstants.teamPrefix))
        else
          entity.people.push(sanitizeTag(token, OfficialGalleryConstants.personPrefix))

  ###
  # Remove tag prefix and suffix if necessary
  ###
  sanitizeTag = (tag, tagPrefix) ->
    if tagPrefix == OfficialGalleryConstants.eventPrefix || tagPrefix == OfficialGalleryConstants.teamPrefix
      tag = tag.substr(tagPrefix.length + 1, tag.length)
    if tagPrefix == OfficialGalleryConstants.personPrefix && tag.indexOf('(') != -1
      tag = tag.substr(0, tag.indexOf('('));
    return tag


  ###
  # Decide, which prefix tag has if any
  # @return identified tag
  ###
  tagPrefix = (tag) ->
    tag = tag.trim()
    if tag.substr(0, OfficialGalleryConstants.eventPrefix.length) == OfficialGalleryConstants.eventPrefix
      OfficialGalleryConstants.eventPrefix
    else if tag.substr(0, OfficialGalleryConstants.teamPrefix.length) == OfficialGalleryConstants.teamPrefix
      OfficialGalleryConstants.teamPrefix
    else
      OfficialGalleryConstants.personPrefix

  return officialGalleryAppService;
)

officialGalleryApp.controller('officialGalleryCtrl', ($scope, $http, $location, officialGalleryService) ->
  $scope.photos = []
  $scope.currentPhoto = {}
  $scope.currentIndex = 0
  $scope.maxResult = 0
  $scope.currentTag = "Photo Tour"
  $scope.currentEvent = "Photo Tour"
  $scope.currentTeam = ""
  $scope.currentPerson = ""
  $scope.config = {
    year: 2013,
    maxResult: 30,
    thumbsize: 288,
    imgmax: 512,
    picasaUser: 'hq.icpc',
    albumPrefix: 'Album'
  }

  $scope.eventFilterChanged = () ->
    $scope.searchEvent($scope.currentEvent)

  $scope.searchEvent = (eventName) ->
    $scope.currentEvent = $scope.currentTag = eventName
    $scope.currentTeam = ""
    $scope.currentPerson = ""
    if eventName.length > 0
      tag = OfficialGalleryConstants.eventPrefix + "$" + encodeURIComponent(eventName)
      getInitPhotos(tag)

  $scope.teamFilterChanged = () ->
    $scope.searchTeam($scope.currentTeam)

  $scope.searchTeam = (teamName) ->
    $scope.currentEvent = ""
    $scope.currentTeam = $scope.currentTag = teamName
    $scope.currentPerson = ""
    if teamName.length > 0
      tag = OfficialGalleryConstants.teamPrefix + "$" + encodeURIComponent(teamName)
      getInitPhotos(tag)

  $scope.peopleFilterChanged = () ->
    $scope.searchPeople($scope.currentPerson)

  $scope.searchPeople = (personName) ->
    $scope.currentEvent = ""
    $scope.currentTeam = ""
    $scope.currentPerson = $scope.currentTag = personName
    if personName.length > 0
      tag = encodeURIComponent(personName)
      getInitPhotos(tag)

  $scope.loadMore = () ->
    if $scope.photos.length < $scope.maxResult
      getPhotos($scope.currentTag, $scope.photos.length + 1)
    else
      $(".load-more-btn").addClass('hidden')
    return

  getInitPhotos = (tag) ->
    $scope.photos.length = 0
    $('#galleryPopup').modal('hide')
    $(".load-more-btn").removeClass('hidden')
    location.hash = tag
    getPhotos(tag)

  getPhotos = (tag, start = 1) ->
    console.log(tag)
    $http.get(officialGalleryService.buildCountUrl($scope.config, tag))
      .success((data) ->
        $scope.maxResult = data.feed['openSearch$totalResults']['$t'];
        $http.get(officialGalleryService.buildSearchUrl($scope.config, tag, start))
          .success(appendResultToPhotos)
      )

  appendResultToPhotos = (data) ->
    console.log(data)
    console.log(officialGalleryService.transformToGalleryEntities(data))
    $scope.photos = $scope.photos.concat(officialGalleryService.transformToGalleryEntities(data))
    console.log($scope.photos.length)
    console.log($scope.maxResult)
    if $scope.photos.length == $scope.maxResult
      $(".load-more-btn").addClass('hidden')
    return


  $scope.showPhotoDetail = (index) ->
    console.log(index)
    $scope.currentPhoto = $scope.photos[index]
    $scope.currentIndex = index
    console.log($scope.currentPhoto)

  $scope.previousPhoto = () ->
    prevIndex = $scope.currentIndex - 1
    if prevIndex < 0
      $('#galleryPopup').modal('hide')
    else
      $scope.showPhotoDetail(prevIndex)
    return null

  $scope.nextPhoto = () ->
    $scope.showPhotoDetail($scope.currentIndex + 1)

  $scope.searchEvent($scope.currentEvent)
)