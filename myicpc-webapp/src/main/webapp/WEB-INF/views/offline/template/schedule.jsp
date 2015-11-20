<div ng-repeat="day in schedule" class="col-sm-12 clearfix">
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