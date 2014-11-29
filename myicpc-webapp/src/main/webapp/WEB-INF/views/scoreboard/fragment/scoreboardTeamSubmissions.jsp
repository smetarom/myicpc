<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:forEach var="problem" items="${problems}">
	<td class="text-center problem-cell">
		<div ng-if="isFirstSolvedSubmission(team, ${problem.id})" class="label label-info">
			<span class="hide-attempt">{{team.teamProblems[${problem.id}].attempts}} - </span>{{formatTime(team.teamProblems[${problem.id}].time)}}
		</div>
		<div ng-if="isSolvedSubmission(team, ${problem.id})" class="label label-success">
			<span class="hide-attempt">{{team.teamProblems[${problem.id}].attempts}} - </span>{{formatTime(team.teamProblems[${problem.id}].time)}}
		</div>
		<div ng-if="isFailedSubmission(team, ${problem.id})" class="label label-danger">{{team.teamProblems[${problem.id}].attempts}}</div>
		<div ng-if="isPendingSubmission(team, ${problem.id})" class="label label-warning">?</div>
	</td>
</c:forEach>