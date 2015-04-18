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


  init: () ->
    timelineScoreboardTemplate = compileHandlebarsTemplate("timeline-SCOREBOARD_SUCCESS")
    timelineTwitterTemplate = compileHandlebarsTemplate("timeline-TWITTER")

    this.handlerMapping["submissionSuccess"] = (notification) ->
      timelineScoreboardTemplate(notification)
    this.handlerMapping["twitter"] = (notification) ->
      timelineTwitterTemplate(notification)

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