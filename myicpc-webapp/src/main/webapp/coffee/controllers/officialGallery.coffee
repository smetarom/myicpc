officialGalleryApp = angular.module('officialGallery', []);

OfficialGalleryConstants = {
  delimiter: ', ',
  eventPrefix: "event",
  teamPrefix: "team",
  personPrefix: "person",
}

CommonConfig = {
  year: 2013,
  maxResult: 30,
  thumbsize: 288,
  imgmax: 512,
  picasaUser: 'hq.icpc',
  albumPrefix: 'Album'
}

officialGalleryApp.factory('officialGalleryService', ($http, $interval) ->
  officialGalleryAppService = {};

  officialGalleryAppService.buildSearchUrl = (config, tag, start = 1) ->
    if start < 1 then start = 1
    return "http://picasaweb.google.com/data/feed/api/user/#{config.picasaUser}/?kind=photo&q=Album#{config.year}%20\"#{tag}\"&" +
        "start-index=#{start}&max-results=#{config.maxResult}&thumbsize=#{config.thumbsize}c&imgmax=512&alt=json";

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
        if officialGalleryAppService.tagPrefix(token) == OfficialGalleryConstants.eventPrefix
          entity.events.push(sanitizeTag(token, OfficialGalleryConstants.eventPrefix))
        else if officialGalleryAppService.tagPrefix(token) == OfficialGalleryConstants.teamPrefix
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
  officialGalleryAppService.tagPrefix = (tag) ->
    tag = tag.trim()
    if tag.substr(0, OfficialGalleryConstants.eventPrefix.length) == OfficialGalleryConstants.eventPrefix
      OfficialGalleryConstants.eventPrefix
    else if tag.substr(0, OfficialGalleryConstants.teamPrefix.length) == OfficialGalleryConstants.teamPrefix
      OfficialGalleryConstants.teamPrefix
    else
      OfficialGalleryConstants.personPrefix

  return officialGalleryAppService;
)

officialGalleryApp.controller('eventGalleryCtrl', ($scope, $http, $location, officialGalleryService) ->
  $scope.photos = []
  $scope.config = CommonConfig
  $scope.config.maxResult = 5

  $scope.init = (eventTag, year) ->
    $scope.config.year = year
    if (eventTag != '')
      $http.get(officialGalleryService.buildSearchUrl($scope.config, eventTag, 1))
        .success(appendResultToPhotos)

  appendResultToPhotos = (data) ->
    $scope.photos = officialGalleryService.transformToGalleryEntities(data)

)

officialGalleryApp.controller('teamGalleryCtrl', ($scope, $http, $location, officialGalleryService) ->
  $scope.photos = []
  $scope.config = CommonConfig
  $scope.config.maxResult = 5

  $scope.init = (teamTag, year) ->
    $scope.config.year = year
    console.log($scope.config)
    if (teamTag != '')
      $http.get(officialGalleryService.buildSearchUrl($scope.config, teamTag, 1))
      .success(appendResultToPhotos)

  appendResultToPhotos = (data) ->
    $scope.photos = officialGalleryService.transformToGalleryEntities(data)

)

officialGalleryApp.controller('officialGalleryCtrl', ($scope, $http, $location, officialGalleryService) ->
  $scope.photos = []
  $scope.currentPhoto = {}
  $scope.currentIndex = 0
  $scope.maxResult = 0
  $scope.currentTag = OfficialGalleryConstants.eventPrefix+"$Photo Tour"
  $scope.currentEvent = "Photo Tour"
  $scope.currentTeam = ""
  $scope.currentPerson = ""
  $scope.config = CommonConfig

  $scope.init = (year) ->
    $scope.config.year = year
    processHashtag()

  $scope.eventFilterChanged = () ->
    $scope.searchEvent($scope.currentEvent)

  $scope.searchEvent = (eventName) ->
    $scope.currentEvent = eventName
    $scope.currentTeam = ""
    $scope.currentPerson = ""
    if eventName.length > 0
      tag = OfficialGalleryConstants.eventPrefix + "$" + eventName
      $scope.currentTag = tag
      getInitPhotos(tag)

  $scope.teamFilterChanged = () ->
    $scope.searchTeam($scope.currentTeam)

  $scope.searchTeam = (teamName) ->
    $scope.currentEvent = ""
    $scope.currentTeam = teamName
    $scope.currentPerson = ""
    if teamName.length > 0
      tag = $scope.currentTag = OfficialGalleryConstants.teamPrefix + "$" + teamName
      getInitPhotos(tag)

  $scope.peopleFilterChanged = () ->
    $scope.searchPeople($scope.currentPerson)

  $scope.searchPeople = (personName) ->
    $scope.currentEvent = ""
    $scope.currentTeam = ""
    if personName.length > 0
      tag = $scope.currentTag = personName
      getInitPhotos(tag)

  $scope.loadMore = (onSuccess) ->
    if $scope.photos.length < $scope.maxResult
      getPhotos($scope.currentTag, $scope.photos.length + 1, onSuccess)
    else
      $(".load-more-btn").addClass('hidden')
    return

  getInitPhotos = (tag) ->
    $scope.photos.length = 0
    $('#galleryPopup').modal('hide')
    $(".load-more-btn").removeClass('hidden')
    $location.path(tag)
    getPhotos(tag)

  getPhotos = (tag, start = 1, onSuccess = null) ->
      console.log($scope.config)
      $http.get(officialGalleryService.buildSearchUrl($scope.config, tag, start))
        .success((data) ->
          appendResultToPhotos(data)
          if onSuccess?
            onSuccess()
        )

  appendResultToPhotos = (data) ->
    $scope.photos = $scope.photos.concat(officialGalleryService.transformToGalleryEntities(data))
    $scope.maxResult = data.feed['openSearch$totalResults']['$t']
    if $scope.photos.length == $scope.maxResult
      $(".load-more-btn").addClass('hidden')
    return


  $scope.showPhotoDetail = (index) ->
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
    if $scope.currentIndex + 1 >= $scope.maxResult
      $('#galleryPopup').modal('hide')
    else if $scope.currentIndex + 1 >= $scope.photos.length
      $scope.loadMore(() ->
        $scope.showPhotoDetail($scope.currentIndex + 1)
      )
    else
      $scope.showPhotoDetail($scope.currentIndex + 1)
    return null

  processHashtag = () ->
    hash = $location.path();
    console.log(hash)
    tagType = null
    tag = null
    if hash? && hash.length > 0
      hash = hash.substr(1)
      tagType = officialGalleryService.tagPrefix(hash)
      if (tagType == OfficialGalleryConstants.eventPrefix || tagType == OfficialGalleryConstants.teamPrefix)
        tag = hash.substr(tagType.length + 1)
      else
        tag = hash
      if tag?
        if (tagType == OfficialGalleryConstants.eventPrefix)
          $scope.currentEvent = $scope.currentTag = tag
          $scope.searchEvent($scope.currentEvent)
        else if (tagType == OfficialGalleryConstants.teamPrefix)
          $scope.currentTeam = $scope.currentTag = tag
          $scope.searchTeam($scope.currentTeam)
        else
          $scope.currentPerson = $scope.currentTag = tag
          $scope.searchPeople($scope.currentPerson)
    else
        $scope.searchEvent($scope.currentEvent)
)