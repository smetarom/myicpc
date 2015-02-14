<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<tr class="scoreboardRow" ng-click="toogleTeamProblems(team)">
    <td class="pin">
        <a href="#" ng-click="pinTeam(team);" onclick=" return false;">
            <span class="glyphicon" ng-class="{true:'glyphicon-star', 'undefined':'glyphicon-star-empty', false:'glyphicon-star-empty'}[team.followed]"></span>
        </a>
    </td>
    <td class="team-rank"><strong>{{team.teamRank}}</strong></td>
    <td class="nowrap team-name"><strong><a href="<spring:url value="/team/{{team.teamId}}"/>">{{team.teamName}}</a></strong></td>
    <td class="text-center total-solved"><strong>{{team.nSolved}}</strong></td>
    <td class="text-center total-time"><strong>{{team.totalTime}}</strong></td>
</tr>
<tr id="team_{{team.teamId}}_problems" class="problemRow" ng-show="team.showProblems">
    <td colspan="5">
        <table style="width: 100%; margin-bottom: 5px;" style="font-size: 0.5em;">
            <tr>
                <c:forEach var="problem" items="${problems}">
                    <td class="text-center mobile-problem-cell">
                        <div ng-if="isFirstSolvedSubmission(team, ${problem.id})" class="label label-firstSolved">
                            {{formatTime(team.teamProblems[${problem.id}].time)}}</div>
                        <div ng-if="isSolvedSubmission(team, ${problem.id})" class="label label-success">
                            {{formatTime(team.teamProblems[${problem.id}].time)}}</div>
                        <div ng-if="isFailedSubmission(team, ${problem.id})" class="label label-danger">{{team.teamProblems[${problem.id}].attempts}}</div>
                        <div ng-if="isPendingSubmission(team, ${problem.id})" class="label label-warning">?</div>
                        <div ng-if="!team.teamProblems[${problem.id}]" class="label label-shadow">${problem.code}</div>
                    </td>
                </c:forEach>
            </tr>
        </table>
    </td>
</tr>