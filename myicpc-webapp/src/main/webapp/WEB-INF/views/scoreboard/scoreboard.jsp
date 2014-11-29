<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:body>
        <script src="<c:url value='/js/myicpc/controllers/scoreboardController.js'/>" defer></script>
        <t:downloadContestProblems />

        <c:if test="${not scoreboardAvailable}">
            <div class="centered-notification">
                <h2>
                    <spring:message code="scoreboard.notAvailable" />
                </h2>
            </div>
        </c:if>
        <c:if test="${scoreboardAvailable}">
            <div id="mainScoreboard" class="table-responsive desktop" ng-app ng-controller="CustomScoreboardController">
                <table class="table striped-rows scoreboard invisible">
                    <thead>
                    <tr>
                        <th></th>
                        <th><spring:message code="scoreboard.rankShort" /></th>
                        <th><spring:message code="scoreboard.teamName" /></th>
                        <c:if test="${not regionDetail}"><th class="hide-region"><spring:message code="scoreboard.region" /></th></c:if>
                        <th class="text-center"><spring:message code="scoreboard.solved" /></th>
                        <th class="text-center"><spring:message code="scoreboard.totalTime" /></th>
                        <c:forEach var="problem" items="${problems}">
                            <th style="min-width: 50px" class="text-center"><a href="<spring:url value="${contestURL}/problem/${problem.code}"/>">${problem.code}</a></th>
                        </c:forEach>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="team_{{team.teamId}}" ng-repeat="team in pinnedTeams | orderBy:'rank'">
                        <%@ include file="/WEB-INF/views/scoreboard/fragment/scoreboardRow.jsp"%>
                    </tr>
                    <tr style="background-color: black;" ng-if="pinnedTeams.length > 0">
                        <td colspan="20"></td>
                    </tr>
                    <tr class="team_{{team.teamId}}" ng-repeat="team in teams  | orderBy:'rank'">
                        <%@ include file="/WEB-INF/views/scoreboard/fragment/scoreboardRow.jsp"%>
                    </tr>
                    </tbody>
                </table>
            </div>
        </c:if>

        <script type="text/javascript">

            $(function() {
                <c:if test="${scoreboardAvailable}">
                var ngController = angular.element($("#mainScoreboard")).scope();
                var teams = ${not empty teamJSON ? teamJSON : '[]'};
                var pinnedTeams = ${not empty followedTeamJSON ? followedTeamJSON : '[]'};
                var currTime = 0;
                ngController.setAllTeams(teams, pinnedTeams);

                $("#mainScoreboard > table").removeClass("invisible");

                if (Modernizr.localstorage) {
                    localStorage.setItem("scoreboard", JSON.stringify(teams));
                    localStorage.setItem("scoreboardProblems", JSON.stringify(${problemJSON}));
                }
                </c:if>

                <%@ include file="/WEB-INF/views/scoreboard/fragment/atmosphereHandler.jsp"%>

                $("#mainScoreboard").click(function() {
                    $("#scorebord-notification-bar").hide("slide", { direction: 'right'});
                });
            });

        </script>
    </jsp:body>

</t:template>
