<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div ng-init="init('${r.contextPath}', '${contest.code}', '${problem.code}')"></div>

<div ng-hide="dataLoaded" class="no-items-available">
    <t:spinnerIcon id="teamSubmissionSpinner" /> <spring:message code="loadingCtn" />
</div>

<div ng-show="dataLoaded">
    <div ng-show="!teams.length" class="no-items-available">
        No events
    </div>

    <table class="table problem-table striped-body">
        <tbody ng-repeat="team in teams | orderBy : 'teamName'">
            <tr>
                <td><a href="<spring:url value="${contestURL}/team/"/>{{team.teamExternalId}}">{{team.teamName}}</a></td>
                <td><double-tick ng-model="team.solved" /></td>
            </tr>
            <tr>
                <td colspan="2">
                    <table class="table inner-table table-condensed">
                        <tr ng-repeat="submission in team.submissions">
                            <td style="width: 5%"><triple-tick ng-model="submission"/></td>
                            <td style="width: 10%">{{formatTime(submission.time)}}</td>
                            <td style="width: 20%" class="judgment-color {{submission.judgement}}">{{submission.judgementName}}</td>
                            <td style="width: 15%">{{submission.language}}</td>
                            <td><div ng-if="submission.judged && !submission.solved" class="pull-right" style="width: 10em"><solution-bar ng-model="submission"/></div></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </tbody>
    </table>

</div>