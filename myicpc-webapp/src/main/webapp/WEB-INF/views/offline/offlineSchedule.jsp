<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateOffline>
    <jsp:body>
        <div id="scheduleWrapper" class="table-responsive desktop" ng-app ng-controller="OfflineScheduleController">
            <div ng-if="schedule">
                <div class="page-header">
                    <h2>
                        <spring:message code="schedule.title"/>
                    </h2>
                </div>

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
            </div>
            <br class="clear"/>
        </div>

        <div id="noTeamResult" class="container" style="border-width: 0; margin-top: 2em;">
            <div class="jumbotron">
                <h2 style="font-size: 1.9em;">
                    <spring:message code="offline.schedule.notLoaded"/>
                </h2>

                <p>
                    <spring:message code="offline.schedule.notLoaded.hint"/>
                </p>

                <p>
                    <a href="<c:url value="/" />" class="btn btn-primary btn-lg" role="button"><spring:message
                            code="offline.goHome"/></a>
                </p>
            </div>
        </div>


        <script type="text/javascript">
            function OfflineScheduleController($scope) {
                $scope.schedule = null;

                $scope.setSchedule = function (schedule) {
                    $scope.schedule = schedule;
                    $scope.$apply();
                };
            }

            $(function () {
                var ngController = angular.element($("#scheduleWrapper")).scope();
                if (Modernizr.localstorage) {
                    var schedule = localStorage["schedule"];
                    if (schedule !== undefined) {
                        ngController.setSchedule(JSON.parse(schedule));
                        $("#noTeamResult").css("display", "none");
                    }
                }
            });
        </script>
    </jsp:body>
</t:templateOffline>