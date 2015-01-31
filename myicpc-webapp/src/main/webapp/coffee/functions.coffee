class AtmosphereRequest
  constructor: (@url, @onMessage) ->
    @contentType = "application/json"
    @logLevel = 'debug'
    @transport = 'websocket'
    @trackMessageLength = true
    @reconnectInterval = 5000
    @fallbackTransport = 'long-polling'

    @onOpen = (response) ->
      console.log("Atmosphere onOpen: Atmosphere connected using #{response.transport}");

    @onReopen = (response) ->
      console.log("Atmosphere re-connected using #{response.transport}");

    @onClose = (response) ->
      console.log('Atmosphere onClose executed');

    @onError = (response) ->
      console.log('Atmosphere onError: Sorry, but there is some problem with your socket or the server is down!');

startSubscribe = (contextPath, contestCode, channel, processMethod, ngController) ->
  socket = $.atmosphere
  request = new AtmosphereRequest(getSubscribeAddress(contextPath) + contestCode + "/" + channel, (response) ->
    #try
      result = $.parseJSON(response.responseBody);
      processMethod(result, ngController)
    #catch error
     # console.log("An error occurred while parsing the JSON Data: #{response.responseBody}; Error: #{error}");
  )
  connectedSocket = socket.subscribe(request)

getSubscribeAddress = (contextPath) ->
  contextPath = if contextPath != "" then contextPath + '/' else '/'
  window.location.protocol + "//" + window.location.hostname + ':' + window.location.port + contextPath + 'pubsub/'

convertSecondsToMinutes = (seconds) ->
  seconds // 60

formatContestTime = (seconds) ->
  minus = ""
  if (seconds < 0)
    seconds *= -1
    minus = "-"

  hours = seconds // (60 * 60)
  divisor_for_minutes = seconds % (60 * 60)
  minutes = divisor_for_minutes // 60

  hours = "0" + hours if hours < 10
  minutes = "0" + minutes if minutes < 10

  return minus + hours + ":" + minutes;