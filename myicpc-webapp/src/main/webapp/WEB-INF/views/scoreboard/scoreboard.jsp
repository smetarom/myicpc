<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
        <script src="<c:url value='/js/myicpc/controllers/scoreboard.js'/>" defer></script>
    </jsp:attribute>
    <jsp:attribute name="title">
        <spring:message code="nav.scoreboard" />
    </jsp:attribute>


    <jsp:attribute name="javascript">
        <script type="text/javascript">

            $(function() {
                <c:if test="${scoreboardAvailable}">
                var ngController = angular.element($("#mainScoreboard")).scope();
                var teams = ${not empty teamJSON ? teamJSON : '[]'};
                ngController.init(teams);

                startSubscribe('${r.contextPath}', '${contest.code}', 'scoreboard', updateScoreboard, ngController);

                if (Modernizr.localstorage) {
                    <%-- TODO --%>
                    <%--localStorage.setItem("scoreboard", JSON.stringify(teams));--%>
                    <%--localStorage.setItem("scoreboardProblems", JSON.stringify(${problemJSON}));--%>
                }
                </c:if>


                <%--<%@ include file="/WEB-INF/views/scoreboard/fragment/atmosphereHandler.jsp"%>--%>

                $("#mainScoreboard").click(function() {
                    $("#scorebord-notification-bar").hide("slide", { direction: 'right'});
                });
            });

        </script>
    </jsp:attribute>

    <jsp:body>
        <t:downloadContestProblems />

        <c:if test="${not scoreboardAvailable}">
            <div class="centered-notification">
                <h2>
                    <spring:message code="scoreboard.notAvailable" />
                </h2>
            </div>
        </c:if>
        <c:if test="${scoreboardAvailable}">
            <div ng-app="scoreboard">
            <div id="mainScoreboard" class="table-responsive desktop" ng-controller="scoreboardCtrl">
                <div ng-show="filterBy" ng-cloak>
                    <strong><spring:message code="filtredBy" />:</strong>
                    <a href="javascript:void(0)" ng-click="clearFilter()" ng-show="isFilteredBy('regionId')">
                        <span class="label label-default">&times; <spring:message code="scoreboard.region" /></span>
                    </a>
                    <a href="javascript:void(0)" ng-click="clearFilter()" ng-show="isFilteredBy('universityName')">
                        <span class="label label-default">&times; <spring:message code="scoreboard.university" /></span>
                    </a>
                    <a href="javascript:void(0)" ng-click="clearFilter()" ng-show="isFilteredBy('nationality')">
                        <span class="label label-default">&times; <spring:message code="scoreboard.country" /></span>
                    </a>
                </div>
                <table class="table striped-rows scoreboard" ng-cloak>
                    <thead>
                    <tr>
                        <th></th>
                        <th><spring:message code="scoreboard.rankShort" /></th>
                        <th><spring:message code="scoreboard.teamName" /></th>
                        <c:if test="${contest.contestSettings.showRegion}"><th class="hide-region"><spring:message code="scoreboard.region" /></th></c:if>
                        <c:if test="${contest.contestSettings.showUniversity}"><th class="text-center"><spring:message code="scoreboard.university" /></th></c:if>
                        <c:if test="${contest.contestSettings.showCountry}"><th class="text-center"><spring:message code="scoreboard.country" /></th></c:if>
                        <th class="text-center"><spring:message code="scoreboard.solved" /></th>
                        <th class="text-center"><spring:message code="scoreboard.totalTime" /></th>
                        <c:forEach var="problem" items="${problems}">
                            <th style="min-width: 50px" class="text-center"><a href="<spring:url value="${contestURL}/problem/${problem.code}"/>">${problem.code}</a></th>
                        </c:forEach>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="team_{{team.teamId}}" ng-repeat="team in teams | filter: filterTeam | filter: {followed: true} | orderBy:['teamRank','teamName']">
                        <%@ include file="/WEB-INF/views/scoreboard/fragment/scoreboardRow.jsp"%>
                    </tr>
                    <tr style="background-color: black;" ng-if="pinnedTeams.length > 0">
                        <td colspan="20"></td>
                    </tr>
                    <tr class="team_{{team.teamId}}" ng-repeat="team in teams | filter: filterTeam | orderBy:['teamRank','teamName']">
                        <%@ include file="/WEB-INF/views/scoreboard/fragment/scoreboardRow.jsp"%>
                    </tr>
                    </tbody>
                </table>
            </div>
            </div>
        </c:if>


    </jsp:body>

</t:template>
