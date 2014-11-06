<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateOffline>
    <jsp:body>
        <script src="<c:url value='/static/js/myicpc/controllers.js'/>" defer></script>

        <div id="mainScoreboard" class="table-responsive desktop" ng-app ng-controller="CustomScoreboardController">

            <div class="noSelectedBig" ng-hide="teams.length">
                <spring:message code="nav.scoreboard.empty"/>
            </div>

            <table class="table striped-rows scoreboard invisible">
                <thead>
                <tr>
                    <th><spring:message code="scoreboard.rankShort"/></th>
                    <th><spring:message code="scoreboard.teamName"/></th>
                    <th class="hide-region"><spring:message code="scoreboard.region"/></th>
                    <th class="text-center"><spring:message code="scoreboard.solved"/></th>
                    <th class="text-center"><spring:message code="scoreboard.totalTime"/></th>
                    <th class="text-center" style="min-width: 50px" ng-repeat="problem in problems">
                        <a href="<spring:url value="/problem/"/>{{problem.code}}">{{problem.code}}</a>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr class="team_{{team.teamId}}" ng-repeat="team in teams  | orderBy:'rank'">
                    <td>{{team.rank}}</td>
                    <td class="nowrap team-name" title="{{team.teamName}}"><a
                            href="<spring:url value="/team/{{team.teamId}}"/>">{{team.teamShortName}}</a></td>
                    <td class="nowrap hide-region"><a href="<spring:url value="/region/{{team.regionId}}"/>">{{team.regionShortName}}</a>
                    </td>
                    <td class="text-center">{{team.nSolved}}</td>
                    <td class="text-center">{{team.totalTime}}</td>
                    <td class="text-center problem-cell" ng-repeat="problem in problems">
                        <div ng-if="team.teamProblems[problem.id].judged === true && team.teamProblems[problem.id].solved === true && team.teamProblems[problem.id].first === true"
                             class="label label-info">
                            <span class="hide-attempt">{{team.teamProblems[problem.id].attempts}} - </span>{{formatTime(team.teamProblems[problem.id].time)}}
                        </div>
                        <div ng-if="team.teamProblems[problem.id].judged === true && team.teamProblems[problem.id].solved === true && !team.teamProblems[problem.id].first"
                             class="label label-success">
                            <span class="hide-attempt">{{team.teamProblems[problem.id].attempts}} - </span>
                            {{formatTime(team.teamProblems[problem.id].time)}}
                        </div>
                        <div ng-if="team.teamProblems[problem.id].judged === true && team.teamProblems[problem.id].solved === false"
                             class="label label-danger">
                            {{team.teamProblems[problem.id].attempts}}
                        </div>
                        <div ng-if="team.teamProblems[problem.id].judged === false && !team.teamProblems[problem.id].solved"
                             class="label label-warning">
                            ?
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <%@ include file="/WEB-INF/views/fragment/scoreboard/scoreboardDesktopLegend.jsp" %>

        <script type="text/javascript">
            $(function () {
                if (Modernizr.localstorage) {
                    var scoreboard = localStorage["scoreboard"];
                    if (scoreboard !== undefined) {

                        var ngController = angular.element($("#mainScoreboard")).scope();
                        var teams = JSON.parse(scoreboard);
                        var pinnedTeams = [];
                        var p = localStorage["scoreboardProblems"];
                        if (p !== undefined) {
                            var problems = JSON.parse(p);
                            ngController.setProblems(problems);
                        }
                        ngController.setAllTeams(teams, pinnedTeams);
                        $("#mainScoreboard > table").removeClass("invisible");
                    }
                }
            });
        </script>
    </jsp:body>
</t:templateOffline>