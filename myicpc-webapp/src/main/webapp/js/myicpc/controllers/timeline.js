// Generated by CoffeeScript 1.8.0
var Timeline, timeline, updateTimeline, videoAutoplayOnScroll,
  __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

timeline = angular.module('timeline', []);

timeline.controller('timelineCtrl', function($scope) {});

Timeline = {
  pendingNotificationsCount: 0,
  translations: {
    "timeline.pendingNotifications": "You have ",
    "timeline.pendingNotifications.one": " new notification",
    "timeline.pendingNotifications.other": " new notifications"
  },
  handlerMapping: {},
  supportedNotificationTypes: [],
  init: function() {
    var timelineScoreboardTemplate, timelineTwitterTemplate;
    this.supportedNotificationTypes = ["submissionSuccess", "twitter"];
    timelineScoreboardTemplate = compileHandlebarsTemplate("timeline-SCOREBOARD_SUCCESS");
    timelineTwitterTemplate = compileHandlebarsTemplate("timeline-TWITTER");
    this.handlerMapping["submissionSuccess"] = function(notification) {
      return timelineScoreboardTemplate(notification);
    };
    return this.handlerMapping["twitter"] = function(notification) {
      return timelineTwitterTemplate(notification);
    };
  },
  updateMainFeed: function(data) {
    var text, _ref;
    if ((_ref = data.type, __indexOf.call(this.supportedNotificationTypes, _ref) >= 0)) {
      if ($(window).scrollTop() === 0) {
        return this.addNotificationToTimeline(data);
      } else {
        this.pendingNotificationsCount += 1;
        this.addNotificationToTimeline(data, {
          "hide": true
        });
        text = "";
        if (this.pendingNotificationsCount === 1) {
          text = "" + this.translations['timeline.pendingNotifications'] + " 1 " + this.translations['timeline.pendingNotifications.one'];
        } else {
          text = "" + this.translations['timeline.pendingNotifications'] + " " + this.pendingNotificationsCount + " " + this.translations['timeline.pendingNotifications.other'];
        }
        return $("#timeline-notification").html(text).removeClass("hidden");
      }
    }
  },
  displayPendingNotification: function() {
    if (this.pendingNotificationsCount > 0) {
      $("#timeline-body .timelineTile").removeClass("hidden");
      $("#timeline-body .timelineTile:lt(" + this.pendingNotificationsCount + ")").effect("highlight", {}, 800);
      this.pendingNotificationsCount = 0;
      return $("#timeline-notification").addClass("hidden");
    }
  },
  addNotificationToTimeline: function(notification, settings) {
    var elem, handler;
    settings = $.extend({
      'duration': 1500,
      'prepend': true,
      'hide': false,
      "featured": true
    }, settings);
    if (typeof notification.code === 'string') {
      notification.code = $.parseJSON(notification.code);
    }
    elem = null;
    handler = this.handlerMapping[notification.type];
    if ((handler != null)) {
      elem = handler(notification);
    }
    if (elem !== null) {
      if (settings.hide === true) {
        return $(elem).prependTo($("#timeline-body")).addClass("hidden");
      } else {
        if (settings.prepend === true) {
          if (settings.duration === 0) {
            return $(elem).hide().prependTo($("#timeline-body")).effect("highlight", {}, 800);
          } else {
            return $(elem).hide().prependTo($("#timeline-body")).slideDown(settings.duration).effect("highlight", {}, settings.duration);
          }
        } else {
          return $(elem).appendTo($("#timeline-body"));
        }
      }
    }
  }
};

updateTimeline = function(data, ngController) {
  return Timeline.updateMainFeed(data);
};

videoAutoplayOnScroll = function() {
  var firstPlaying;
  firstPlaying = false;
  $("video").each(function() {
    var video;
    video = this;
    if (isElementVisible(video, 40) && !firstPlaying) {
      firstPlaying = true;
      return video.play();
    } else {
      return video.pause();
    }
  });
};