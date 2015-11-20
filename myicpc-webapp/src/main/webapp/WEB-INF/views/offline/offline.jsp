<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html ng-app="offline">
<head>
    <title>Offline - MyICPC</title>
    <%@ include file="/WEB-INF/views/includes/headOffline.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<div id="bodyContainer" ng-controller="offlineCtrl" ng-cloak>
    <jsp:include page="/WEB-INF/views/includes/header_offline.jsp"/>
    <div class="alert alert-danger" style="margin-bottom: 0;">
        <spring:message code="offline.warning"/>
        <a href="javascript: location.reload();" class="alert-link"><spring:message
                code="offline.warning.link"/></a>
    </div>
    <div id="body">
        <div ng-show="selectedTab=='quest'">
            <%@ include file="/WEB-INF/views/offline/template/quest.jsp"%>
        </div>
        <div ng-show="selectedTab=='scoreboard'">
            <%@ include file="/WEB-INF/views/offline/template/scoreboard.jsp"%>
        </div>
        <div ng-show="selectedTab=='schedule'">
            <%@ include file="/WEB-INF/views/offline/template/schedule.jsp"%>
        </div>
        <div ng-show="selectedTab==null" class="container">
            <br/>
            <div class="well ${sitePreference.mobile ? 'mobile' : 'desktop' }">
                <h2 style="margin-top: 0px;">
                    <spring:message code="offline.error.title" />
                </h2>
                <ul>
                    <li><spring:message code="offline.error.reason1" /></li>
                    <li><spring:message code="offline.error.reason2" /></li>
                </ul>
                <br />
                <p>
                    <a href="javascript: location.reload();" class="btn btn-primary"><spring:message code="offline.warning.link" /></a>
                </p>
            </div>

            <div ng-show="currentContest == null">
                <h2><spring:message code="offline.lastVisitedContest.noResult" /></h2>
            </div>
            <div ng-hide="currentContest == null">
                <h2>
                    <spring:message code="offline.lastVisitedContest" /> {{currentContest}}. <spring:message code="offline.meantime" />
                </h2>

                <jsp:include page="/WEB-INF/views/offline/fragment/offlineButton.jsp">
                    <jsp:param name="url" value="scoreboard" />
                    <jsp:param name="icon" value="glyphicon glyphicon-th-list" />
                    <jsp:param name="label" value="nav.scoreboard" />
                </jsp:include>

                <jsp:include page="/WEB-INF/views/offline/fragment/offlineButton.jsp">
                    <jsp:param name="url" value="schedule" />
                    <jsp:param name="icon" value="glyphicon glyphicon-calendar" />
                    <jsp:param name="label" value="nav.schedule" />
                </jsp:include>

                <jsp:include page="/WEB-INF/views/offline/fragment/offlineButton.jsp">
                    <jsp:param name="url" value="quest" />
                    <jsp:param name="icon" value="glyphicon glyphicon-screenshot" />
                    <jsp:param name="label" value="nav.quest" />
                </jsp:include>
            </div>
        </div>

        <script type="text/javascript">
            var offlineApp = angular.module('offline', []);

            offlineApp.controller('offlineCtrl', function($scope) {
                $scope.currentContest = null;
                $scope.scoreboard = null;
                $scope.problems = null;
                $scope.schedule = null;
                $scope.questChallenges = null;
                $scope.selectedTab = null;
                $scope.init = function() {
                    $scope.$apply(function() {
                        $scope.currentContest = localStorage["currentContest"];
                        if ($scope.currentContest === localStorage["scheduleContest"]) {
                            $scope.schedule = $.parseJSON(localStorage["schedule"]);
                        }
                        if ($scope.currentContest === localStorage["contestScoreboard"]) {
                            $scope.scoreboard = $.parseJSON(localStorage["scoreboard"]);
                            $scope.problems = $.parseJSON(localStorage["scoreboardProblems"]);
                        }
                        if ($scope.currentContest === localStorage["contestQuest"]) {
                            return $scope.questChallenges = $.parseJSON(localStorage["questChallenges"]);
                        }
                    });
                };
                return $scope.formatTime = function(time) {
                    return convertSecondsToMinutes(time);
                };
            });

            $(function() {
                var ngController = angular.element($("#bodyContainer")).scope();
                ngController.init();

            });
        </script>
    </div>
</div>
</body>
</html>