timeline = angular.module('timeline', []);

timeline.controller('timelineCtrl', ($scope) ->


)

Timeline = {
  pendingNotificationsCount: 0,
  translations: {
    "timeline.pendingNotifications": "You have ",
    "timeline.pendingNotifications.one": " new notification",
    "timeline.pendingNotifications.other": " new notifications",
  },
  handlerMapping: {},
  ignoreScrolling: false,
  lastTimelineIdLoaded: null

  init: () ->
    timelineScoreboardTemplate = compileHandlebarsTemplate("timeline-SCOREBOARD_SUCCESS")
    timelineAnalystTeamTemplate = compileHandlebarsTemplate("timeline-ANALYST_TEAM_MESSAGE")
    timelineAnalystTemplate = compileHandlebarsTemplate("timeline-ANALYST_MESSAGE")
    timelineTwitterTemplate = compileHandlebarsTemplate("timeline-TWITTER")
    timelineInstagramTemplate = compileHandlebarsTemplate("timeline-INSTAGRAM")
    timelineVineTemplate = compileHandlebarsTemplate("timeline-VINE")
    timelinePicasaTemplate = compileHandlebarsTemplate("timeline-PICASA")
    timelineOfficialGalleryTemplate = compileHandlebarsTemplate("timeline-OFFICIAL_GALLERY")
    timelineQuestChallengeTemplate = compileHandlebarsTemplate("timeline-QUEST_CHALLENGE")
    timelineAdminNotificationTemplate = compileHandlebarsTemplate("timeline-ADMIN_NOTIFICATION")
    timelineEventOpenNotificationTemplate = compileHandlebarsTemplate("timeline-SCHEDULE_EVENT_OPEN")

    this.handlerMapping["submissionSuccess"] = (notification) -> timelineScoreboardTemplate(notification)
    this.handlerMapping["twitter"] = (notification) -> timelineTwitterTemplate(notification)
    this.handlerMapping["analystTeamMsg"] = (notification) -> timelineAnalystTeamTemplate(notification)
    this.handlerMapping["analystMsg"] = (notification) -> timelineAnalystTemplate(notification)
    this.handlerMapping["instagram"] = (notification) -> timelineInstagramTemplate(notification)
    this.handlerMapping["vine"] = (notification) -> timelineVineTemplate(notification)
    this.handlerMapping["picasa"] = (notification) -> timelinePicasaTemplate(notification)
    this.handlerMapping["gallery"] = (notification) -> timelineOfficialGalleryTemplate(notification)
    this.handlerMapping["questChallenge"] = (notification) -> timelineQuestChallengeTemplate(notification)
    this.handlerMapping["adminNotification"] = (notification) -> timelineAdminNotificationTemplate(notification)
    this.handlerMapping["eventOpen"] = (notification) ->
      notification.body = $.parseJSON(notification.body)
      timelineEventOpenNotificationTemplate(notification)

  acceptFunction: (data) ->
    return true

  updateMainFeed: (data) ->
    # ignore unwanted notifications
    if this.acceptFunction(data)
      if (this.ignoreScrolling || $(window).scrollTop() == 0)
        this.addNotificationToTimeline(data)
      else
        this.pendingNotificationsCount += 1
        this.addNotificationToTimeline(data, {
          "hide" : true
        })
        text = "";
        if (this.pendingNotificationsCount == 1)
          text = "#{this.translations['timeline.pendingNotifications']} 1 #{this.translations['timeline.pendingNotifications.one']}"
        else
          text = "#{this.translations['timeline.pendingNotifications']} #{this.pendingNotificationsCount} #{this.translations['timeline.pendingNotifications.other']}"

        $("#timeline-notification").html(text).removeClass("hidden")

  displayPendingNotification : () ->
    if (this.pendingNotificationsCount > 0)
      $("#timeline-body .timelineTile").removeClass("hidden");
      $("#timeline-body .timelineTile:lt(#{this.pendingNotificationsCount})").effect("highlight", {}, 800);
      this.pendingNotificationsCount = 0;
      $("#timeline-notification").addClass("hidden");

  addNotificationToTimeline: (notification, settings) ->
    settings = $.extend({'duration': 1500, 'prepend': true, 'hide': false,  "featured" : true}, settings);
    if (typeof notification.code == 'string')
      notification.code = $.parseJSON(notification.code)

    elem = null
    handler = this.handlerMapping[notification.type]
    if (handler?)
      elem = handler(notification)

    if (elem != null)
      if (settings.hide == true)
        $(elem).prependTo($("#timeline-body")).addClass("hidden");
      else
        if (settings.prepend == true)
          if (settings.duration == 0)
            $(elem).hide().prependTo($("#timeline-body")).effect("highlight", {}, 800)
          else
            $(elem).hide().prependTo($("#timeline-body")).slideDown(settings.duration).effect("highlight", {}, settings.duration)
        else
          $(elem).appendTo($("#timeline-body"))

  loadMorePosts: (url) ->
    $.getJSON(url, {lastTimestamp: Timeline.lastTimelineIdLoaded}, (object) ->
      if $.isEmptyObject(object)
        $('#timeline .timeline-loading').addClass('hidden')
      else
        if object.hasOwnProperty('lastTimelineId')
          Timeline.lastTimelineIdLoaded = object['lastTimelineId']
        data = object.data

        for i in [0..data.length-1] by 1
          Timeline.addNotificationToTimeline(data[i], {'prepend': false, "featured" : false})

        $('#timeline .timeline-loading').addClass('hidden')
        setTimeout(() ->
          $('#loadMoreTimeline').removeClass('hidden')
        , 3000)
    )

}

updateTimeline = (data, acceptFunction, ngController) ->
  Timeline.updateMainFeed(data, acceptFunction)

videoAutoplayOnScroll = () ->
  firstPlaying = false
  $("video").each(() ->
    video = this
    if (isElementVisible(video, 40) && !firstPlaying)
      firstPlaying = true
      video.play()
    else
      video.pause()
  )
  return

$(window).scroll ->
  if $(window).scrollTop() == $(document).height() - $(window).height() and $('#timeline .timeline-loading').hasClass('hidden')
    $('#timeline .timeline-loading').removeClass('hidden')
    $('#loadMoreTimeline').addClass('hidden')
    Timeline.loadMorePosts(timelineLoadMoreUrl)
  else if $(window).scrollTop() == 0
    Timeline.displayPendingNotification()
  return