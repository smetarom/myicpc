// Atmosphere heplers
/**
 * Wrapper for default Atmosphere Web socket settings
 *
 * @param channelName web socket channel URL
 * @param onMessage implementation of onMessage method
 */
function AtmRequest(channelName, onMessage) {
    this.url = channelName;
    this.contentType = "application/json";
    //this.shared = true;
    this.trackMessageLength = true;
    this.transport = "websocket";
    this.fallbackTransport = "long-polling";

    this.onOpen = function (response) {
        console.log('Atmosphere onOpen: Atmosphere connected using ' + response.transport);
    };

    this.onReconnect = function (request, response) {
        console.log(response.transport);
        if (response.transport === 'long-polling') {
            request.requestCount = 0;
            console.log(request.requestCount);
        }
        console.log("Atmosphere onReconnect: Reconnecting -" + response.transport);
    };

    this.onClose = function (response) {
        //alert('There\'s been an error. Try to refresh your page.');
        console.log('Atmosphere onClose executed');
    };

    this.onError = function (response) {
        console.log('Atmosphere onError: Sorry, but there is some problem with your socket or the server is down!');
        $("body").append('<div class="modal fade" id="websocketErrorDialog" tabindex="-1" role="dialog"><div class="modal-dialog"><div class="modal-content"><div class="modal-header"><button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><h4 class="modal-title">Connection error</h4></div><div class="modal-body">Connection problem with server! Do you want to reconnect?</div><div class="modal-footer"><button type="button" class="btn btn-default" data-dismiss="modal">Stay on the page</button><button type="button" class="btn btn-primary" onclick="location.reload();">Reconnect</button></div></div></div></div>');
        $('#websocketErrorDialog').modal('show');
        if (typeof refreshNotificationSocketInterval !== "undefined") {
            clearInterval(refreshNotificationSocketInterval);
        }
        if (typeof refreshScoreboardSocketInterval !== "undefined") {
            clearInterval(refreshScoreboardSocketInterval);
        }
        if (typeof refreshPollSocketInterval !== "undefined") {
            clearInterval(refreshPollSocketInterval);
        }
        if (typeof refreshQuestSocketInterval !== "undefined") {
            clearInterval(refreshQuestSocketInterval);
        }
    };

    this.onMessage = onMessage;
}

/**
 * Returns full URL for Atmosphere channel based on context path
 * @param app context path
 * @returns {String} full URL for Atmosphere channel
 */
function getCorrectSubscribeAddress(app) {
    var s = "";
    if (app !== "") {
        s += app + '/';
    } else {
        s += "/";
    }
    if (document.location.pathname.indexOf('/m/') != -1) {
        s += 'm/';
    } else if (document.location.pathname.indexOf('/t/') != -1) {
        s += 't/';
    }
    if (window.location.protocol === 'https:') {
        return window.location.protocol + "//" + securedWebsocketURL + s + "subscribe/";
    }
    return window.location.protocol + "//" + websocketURL + s + "subscribe/";
}
// End of Atmosphere heplers

function compileHandlebarsTemplate(id) {
    return Handlebars.compile($("#" + id).html())
}

/**
 * If the notification type is scoreboard related
 */
function isScoreboard(type) {
    if (type === 's') {
        return true;
    }
    return false;
}

/**
 * If the notification type is social related
 */
function isSocial(type) {
    if (type === 't' || type === 'a') {
        return true;
    }
    return false;
}

/**
 * If the notification type is gallery related
 */
function isGallery(type) {
    if (type === 'ga' || type == 'ph' || type == 'im' || type === 'vi' || type === 'iv') {
        return true;
    }
    return false;
}

/**
 * Convert seconds into minutes
 * @param seconds number of seconds
 * @returns number of minutes
 */
function convertSecondsToMinutes(seconds) {
    return Math.floor(seconds / 60);
}

/**
 * Convert seconds into hours and minutes
 * @param seconds number of seconds
 * @returns time in format Xh XXmin
 */
function convertSecondsToHHMM(seconds) {
    var hours, minutes;
    hours = Math.floor(seconds / 3600);
    seconds %= 3600;
    minutes = Math.floor(seconds / 60);
    s = "";
    if (hours) {
        s = hours + "h";
    }
    return s + minutes + "min";
}

/**
 * Convert seconds into hours and minutes and seconds
 * @param seconds number of seconds
 * @returns time in format HH:MM:SS
 */
function convertSecondsToHHMMSS(seconds) {
    hours = Math.floor(seconds / 3600);
    seconds %= 3600;
    minutes = Math.floor(seconds / 60);
    seconds %= 60;
    s = "";
    if (hours) {
        if (hours < 10) {
            houres = "0" + hours;
        }
        s = hours + ":";
    }
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    s += minutes + ":";
    if (seconds < 10) {
        seconds = "0" + seconds;
    }
    return s + seconds;
}

/**
 * Convert seconds into contest time
 * @param seconds number of seconds
 * @returns {String} time in format [minus]HH:MM
 */
function formatContestTime(seconds) {
    var minus = "", hours, divisor_for_minutes, minutes;
    if (seconds < 0) {
        seconds *= -1;
        minus = "-";
    }
    hours = Math.floor(seconds / (60 * 60));

    divisor_for_minutes = seconds % (60 * 60);
    minutes = Math.floor(divisor_for_minutes / 60);

    if (hours < 10) {
        hours = "0" + hours;
    }
    if (minutes < 10) {
        minutes = "0" + minutes;
    }

    return minus + hours + ":" + minutes;
}

/**
 * Default format of date picker
 */
var datePickerOptions = {
    dateFormat: "yy-mm-dd",
    clockType: 24
};

/**
 * Share URL on Facebook
 * @param urlToShare URL to share
 */
function shareFacebookUrl(urlToShare) {
    window.open(
            'https://www.facebook.com/sharer/sharer.php?u=' + encodeURIComponent(urlToShare),
        'facebook-share-dialog',
        'width=626,height=436');
    return false;
}

/**
 * Share URL on Google
 * @param urlToShare URL to share
 */
function shareGoogleUrl(urlToShare) {
    window.open(
            'https://plus.google.com/share?url=' + encodeURIComponent(urlToShare),
        'google-share-dialog',
        'width=626,height=436');
    return false;
}

/**
 * Share URL on Twitter
 * @param urlToShare URL to share
 */
function shareTwitterUrl(urlToShare) {
    window.open(
            'https://twitter.com/intent/tweet?url=' + encodeURIComponent(urlToShare),
        'twitter-share-dialog',
        'width=626,height=436');
    return false;
}

/**
 * Share content on Facebook
 * @param title post title
 * @param text post content
 * @param picture attached picture
 */
function shareFacebook(title, text, picture) {
    picture = picture || '';
    FB.ui({
        method: 'feed',
        link: document.URL,
        picture: picture,
        name: title,
        caption: '',
        description: text
    }, function (response) {
    });
};

/**
 * Share content on Facebook
 * @param title post title
 * @param text post content
 * @param url URL the post is pointing on
 * @param picture attached picture
 */
function shareFacebookWithURL(title, text, url, picture) {
    picture = picture || '';
    FB.ui({
        method: 'feed',
        link: url,
        picture: picture,
        name: title,
        caption: '',
        description: text
    }, function (response) {
    });
};

/**
 * Set cookies
 * @param c_name cookie name
 * @param value cookie value
 * @param exdays expiration date
 * @param path cookie path
 */
function setCookie(c_name, value, exdays, path) {
    var exdate = new Date(), c_value;
    exdate.setDate(exdate.getDate() + exdays);
    if (path === undefined || path === "") {
        path = '/';
    }
    c_value = escape(value) + ";path=" + path + ((exdays == null) ? "" : "; expires=" + exdate.toUTCString());
    document.cookie = c_name + "=" + c_value;
}

/**
 * Get cookie value
 * @param c_name cookie name
 * @returns cookie value
 */
function getCookie(c_name) {
    var c_value = document.cookie,
        c_start = c_value.indexOf(" " + c_name + "="),
        c_end;
    if (c_start == -1) {
        c_start = c_value.indexOf(c_name + "=");
    }
    if (c_start == -1) {
        c_value = null;
    }
    else {
        c_start = c_value.indexOf("=", c_start) + 1;
        c_end = c_value.indexOf(";", c_start);
        if (c_end == -1) {
            c_end = c_value.length;
        }
        c_value = unescape(c_value.substring(c_start, c_end));
    }
    if (c_value !== null) {
        c_value = c_value.replace(/"/g, '');
    }
    return c_value;
}

/**
 * Gets a cookie and parses it as array of integers
 * @param c_name cookie name
 * @returns array of integers
 */
function getCookieAsIntArray(c_name) {
    return getAsIntArray(c_name);
}

/**
 * Parses string of comma separated integers into array of integers
 * @param str integers separated by comma
 * @param cookie cookie value
 * @returns array of integers
 */
function getAsIntArray(str, cookie) {
    if (cookie === undefined) {
        cookie = true;
    }
    if (cookie) {
        str = getCookie(str);
    }
    if (typeof str === "undefined" || str === null) {
        return [];
    }
    var ids = str.split(","), i;
    for (i = 0; i < ids.length; i++) {
        ids[i] = parseInt(ids[i]);
    }
    return ids;
}

/**
 * Append a value to the cookie
 * @param c_name cookie path
 * @param id appended value
 * @param path cookie path
 */
function appendIdToCookieArray(c_name, id, path) {
    var value = getCookie(c_name);
    if (typeof(value) !== 'undefined' && value !== null) {
        value += "," + id;
    } else {
        value = id;
    }
    setCookie(c_name, value, 7, path);
}
/**
 * Remove integer from cookie
 * @param c_name cookie name
 * @param id removed integer
 * @param path cookie path
 */
function removeIdFromCookieArray(c_name, id, path) {
    var value = "",
        arr = getAsIntArray(c_name),
        i;
    for (i = 0; i < arr.length; i++) {
        if (arr[i] != id) {
            value += arr[i] + ",";
        }
    }
    value = value.substring(0, value.length - 1);
    if (value == '') {
        setCookie(c_name, value, -1, path);
    } else {
        setCookie(c_name, value, 7, path);
    }
}
/**
 * Is cookie value string of integers separated by comma
 * @param c_name cookie name
 * @returns {Boolean} is cookie value string of integers separated by comma
 */
function isCookieValid(c_name) {
    try {
        var c = getCookie(c_name);
        if (/^"?(\d+,)*\d+"?$/.test(c)) {
            return true;
        }
    } catch (e) {
    }
    return false;
}
/**
 * Does URL belongs to Vine
 * @param url URL
 * @returns {Boolean} is Vine URL
 */
function isVineUrl(url) {
    if (url == null || url == undefined) {
        return false;
    }
    return url.indexOf('https://vine.co/v/') == 0;
}
/**
 * Use service to transtate plain text to wiki text
 * @param url service URL
 * @param source plain text source
 * @param target target, where result is inserted
 */
function previewWikiSyntax(url, source, target) {
    var value = source.val();
    $.post(url, {'text': value}, function (data) {
        $(target).html(data);
    });
}
/**
 * Remove tags from HTML code
 * @param HTMLcode HTML code
 * @returns text without HTML tags
 */
function removeHTMLTags(HTMLcode) {
    return HTMLcode.replace(/<\/?[^>]+(>|$)/g, "");
}

/**
 * Replaces the notification, if it is found in the container
 * @param notificationId notification ID
 * @param newElement new notification
 * @param containerID ID of the container
 */
function prependOrReplaceNotification(notificationId, newElement, containerID) {
    if ($("#" + containerID + " .alert.notif-" + notificationId).length) {
        $("#" + containerID + " .alert.notif-" + notificationId).replaceWith(newElement);
    } else {
        $("#" + containerID).prepend(newElement);
    }
}

//Shows dialog with ICPC notification     
function showAdminNotificationModal(adminNotificationId, url) {
    console.log('bla')
    var adminNotificationTemplate = compileHandlebarsTemplate("admin-notification-modal-template");
    $.getJSON(url + adminNotificationId, function (data) {
        $("#adminNotificationModalBody").html(adminNotificationTemplate(data));
    });
}
// Shows poll dialog with form
function showPollModal(pollId, url) {
    var pollModalTemplate = compileHandlebarsTemplate("poll-modal-template");
    $.getJSON(url + pollId + '/showModal', function (data) {
        data.choices = $.parseJSON(data.choices);
        if (data.pollType === 'SELECT') {
            data.select = true;
        } else if (data.pollType === 'CHOICE') {
            data.choice = true;
        }
        $("#pollModalBody").html(pollModalTemplate(data));
    });
}