currentTile = null

Gallery =
  galleryTemplate: compileHandlebarsTemplate('gallery-item-template')
  loadMoreUrl: null

  setLoadMoreUrl: (url) ->
    Gallery.loadMoreUrl = url

  showGalleryModal: (tile) ->
    modalTemplate = compileHandlebarsTemplate('gallery-modal-template')
    context = {}
    context['notificationId'] = $(tile).data('id')
    context['originalUrl'] = $(tile).data('url')
    context['imageUrl'] = $(tile).data('image-url')
    context['videoUrl'] = $(tile).data('video-url')
    context['thumbnailUrl'] = $(tile).data('thumbnail-url')
    context['authorName'] = $(tile).data('author-name')
    context['avatarUrl'] = $(tile).data('author-avatar')
    context['text'] = $(tile).data('text')
    $('#galleryPopupContent').html(modalTemplate(context))
    currentTile = tile

  previousTile: ->
    if currentTile != null
      $currentTile = $(currentTile)
      previousTile = $currentTile.prev('a.gallery-thumbnail')
      if previousTile.length
        Gallery.showGalleryModal(previousTile)
        currentTile = previousTile
      else
        $('#galleryPopup').modal('hide')

  nextTile: ->
    if currentTile != null
      nextTile = $(currentTile).next('a.gallery-thumbnail')
      if nextTile.length
        Gallery.showGalleryModal(nextTile)
        currentTile = nextTile
      else
        Gallery.loadMore(Gallery.loadMoreUrl,
          () ->
            nextTile = $(currentTile).next('a.gallery-thumbnail')
            if nextTile.length
              Gallery.showGalleryModal(nextTile)
              currentTile = nextTile
          , () ->
            $('#galleryPopup').modal('hide')
        )
    $('#gallery-content').empty()

  loadingTile: ->
    $('#gallery-submenu').empty()

  loadMore: (url, onSuccess = null, onEmpty = null) ->
    $.get url, {
      'since-notification-id': lastNotificationId
      'media': currentMedia
    }, (data) ->
      if data.trim() == ''
        $('.load-more-btn').addClass('hidden')
        if onEmpty?
          onEmpty()
        return false
      $('#galleryTiles').append(data)
      if onSuccess?
        onSuccess()
      return true

  acceptGalleryPost: (notification) ->
    return true

  updateGallery: (data, ngController) ->
    if Gallery.acceptGalleryPost(data)
      data.isVideo = data.videoUrl != null
      elem = Gallery.galleryTemplate(data)
      $('#galleryTiles').prepend(elem)