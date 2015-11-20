<div ng-hide="schedule == null" ng-repeat="day in schedule" class="col-sm-12 clearfix">
    <h3>{{day.dateString}} - {{day.name}}</h3>

    <div ng-repeat="event in day.events">
        <div class="col-sm-12">
            <strong>{{event.name}}</strong>
        </div>
        <div class="col-sm-4">
            <span class="glyphicon glyphicon-time"></span> {{event.startTime}} - {{event.endTime}}
        </div>
        <div class="col-sm-4">
            <span class="glyphicon glyphicon-map-marker" ng-hide="event.locationName == ''"></span>
            {{event.locationName}}
        </div>
        <div class="col-sm-4">
            <span class="glyphicon glyphicon-user" ng-hide="event.roles == ''"></span> {{event.roles}}
        </div>
        <br class="clear"/>
        <hr class="divider"/>
    </div>
</div>

<div ng-show="schedule == null" class="container">
    <br />
    <div class="jumbotron">
        <h2 style="font-size: 1.9em;">
            <spring:message code="offline.schedule.notLoaded"/>
        </h2>

        <p>
            <spring:message code="offline.schedule.notLoaded.hint"/>
        </p>

        <p>
            <a href="<c:url value="/" />{{currentContest}}" class="btn btn-primary" role="button">
                <spring:message code="offline.goHome"/>
            </a>
        </p>
    </div>
</div>