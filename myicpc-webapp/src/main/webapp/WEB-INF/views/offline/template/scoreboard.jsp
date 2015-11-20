<table ng-hide="scoreboard == null" class="table striped-rows scoreboard">
    <thead>
    <tr>
        <th><spring:message code="scoreboard.rankShort"/></th>
        <th><spring:message code="scoreboard.teamName"/></th>
        <th class="text-center"><spring:message code="scoreboard.solved"/></th>
        <th class="text-center"><spring:message code="scoreboard.totalTime"/></th>
        <th class="text-center" style="min-width: 50px" ng-repeat="problem in problems">
            <a href="<spring:url value="/problem/"/>{{problem.code}}">{{problem.code}}</a>
        </th>
    </tr>
    </thead>
    <tbody>
    <tr class="team_{{team.teamId}}" ng-repeat="team in scoreboard | orderBy:'teamRank'">
        <td>{{team.teamRank}}</td>
        <td class="nowrap team-name" title="{{team.teamName}}">{{team.teamName}}</td>
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

<div ng-show="scoreboard == null" class="container">
    <br />
    <div class="jumbotron">
        <h2 style="font-size: 1.9em;">
            <spring:message code="offline.scoreboard.notLoaded"/>
        </h2>

        <p>
            <spring:message code="offline.scoreboard.notLoaded.hint"/>
        </p>

        <p>
            <a href="<c:url value="/" />{{currentContest}}" class="btn btn-primary" role="button">
                <spring:message code="offline.goHome"/>
            </a>
        </p>
    </div>
</div>