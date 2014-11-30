// Generated by CoffeeScript 1.8.0
var AtmosphereRequest, convertSecondsToMinutes, formatContestTime, getSubscribeAddress, startSubscribe;

AtmosphereRequest = (function() {
  function AtmosphereRequest(url, onMessage) {
    this.url = url;
    this.onMessage = onMessage;
    this.contentType = "application/json";
    this.logLevel = 'debug';
    this.transport = 'websocket';
    this.trackMessageLength = true;
    this.reconnectInterval = 5000;
    this.fallbackTransport = 'long-polling';
    this.onOpen = function(response) {
      return console.log("Atmosphere onOpen: Atmosphere connected using " + response.transport);
    };
    this.onReopen = function(response) {
      return console.log("Atmosphere re-connected using " + response.transport);
    };
    this.onClose = function(response) {
      return console.log('Atmosphere onClose executed');
    };
    this.onError = function(response) {
      return console.log('Atmosphere onError: Sorry, but there is some problem with your socket or the server is down!');
    };
  }

  return AtmosphereRequest;

})();

startSubscribe = function(contextPath, contestCode, channel, processMethod, ngController) {
  var connectedSocket, request, socket;
  socket = $.atmosphere;
  request = new AtmosphereRequest(getSubscribeAddress(contextPath) + contestCode + "/" + channel, function(response) {
    var error, result;
    try {
      result = $.parseJSON(response.responseBody);
      return processMethod(result, ngController);
    } catch (_error) {
      error = _error;
      return console.log("An error occurred while parsing the JSON Data: " + response.responseBody + "; Error: " + error);
    }
  });
  return connectedSocket = socket.subscribe(request);
};

getSubscribeAddress = function(contextPath) {
  contextPath = contextPath !== "" ? contextPath + '/' : '/';
  return window.location.protocol + "//" + window.location.hostname + ':' + window.location.port + contextPath + 'pubsub/';
};

convertSecondsToMinutes = function(seconds) {
  return Math.floor(seconds / 60);
};

formatContestTime = function(seconds) {
  var divisor_for_minutes, hours, minus, minutes;
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
};
