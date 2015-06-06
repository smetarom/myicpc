<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div ng-init="init('${r.contextPath}', '${contest.code}', '${problem.code}')"></div>

<div ng-hide="dataLoaded" class="no-items-available">
    <t:spinnerIcon id="teamSubmissionSpinner" /> <spring:message code="loadingCtn" />
</div>

<div ng-show="dataLoaded">
    <div ng-show="!submissions.length" class="no-items-available">
        No events
    </div>

    <table class="table table-condensed problem-table striped-body">
        <thead>
            <tr>
                <th class="text-center"><spring:message code="problem.time" /></th>
                <th><spring:message code="team" /></th>
                <th class="text-center" colspan="2"><spring:message code="problem.solved" /></th>
            </tr>
        </thead>
        <tbody ng-repeat="submission in submissions">
            <tr>
                <td>{{formatTime(submission.time)}}</td>
                <td><a href="<spring:url value="${contestURL}/team/"/>{{submission.teamExternalId}}">{{submission.teamName}}</a></td>
                <td>
                    <span><triple-tick ng-model="submission"/></span>
                </td>
                <c:if test="${not sitePreference.mobile}">
                    <td>
                        <div ng-if="submission.judged && !submission.solved" class="pull-right" style="width: 10em"><solution-bar ng-model="submission"/></div>
                    </td>
                </c:if>
            </tr>
            <c:if test="${sitePreference.mobile}">
                <tr>
                    <td colspan="3" class="problem-bar" style="padding-top: 0">
                        <solution-bar ng-model="submission"/>
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>