<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div ng-init="init('${r.contextPath}', '${contest.code}', '<spring:message code="problem" />')"></div>

<div class="col-sm-6">
    <table class="table" ng-cloak ng-hide="!data">
        <tbody>
        <tr>
            <th><spring:message code="insight.problem.numSolvedTeams" />:</th>
            <td>{{data.numSolvedTeams}}</td>
        </tr>
        <tr>
            <th><spring:message code="insight.problem.numSubmittedTeams" />:</th>
            <td>{{data.numSubmittedTeams}}</td>
        </tr>
        <tr>
            <th><spring:message code="insight.problem.totalSubmissions" />:</th>
            <td>{{data.totalSubmissions}}</td>
        </tr>
        <tr ng-hide="data.averageSolutionTime == 0">
            <th><spring:message code="insight.problem.averageSolutionTime" />:</th>
            <td>{{data.averageSolutionTime}} min</td>
        </tr>
        <tr ng-hide="!data.solvedLanguages.length">
            <th><spring:message code="insight.problem.solvedLanguages" />:</th>
            <td>
                <span ng-repeat="lang in data.solvedLanguages">
                    {{lang.name}} - {{lang.count}} <br />
                </span>
            </td>
        </tr>
        <tr ng-hide="!data.languages.length">
            <th><spring:message code="insight.problem.languages" />:</th>
            <td>
                <span ng-repeat="lang in data.languages">
                    {{lang.name}} - {{lang.count}} <br />
                </span>
            </td>
        </tr>
        <tr ng-hide="!data.firstSubmission">
            <th><spring:message code="insight.problem.firstSubmission" />:</th>
            <td>{{data.firstSubmission.time}}<br /><a href="<spring:url value="${contestURL}/team/" />{{data.firstSubmission.teamId}}">{{data.firstSubmission.teamName}}</a></td>
        </tr>
        <tr ng-hide="!data.firstSolution">
            <th><spring:message code="insight.problem.firstSolution" />:</th>
            <td>{{data.firstSolution.time}}<br /><a href="<spring:url value="${contestURL}/team/" />{{data.firstSolution.teamId}}">{{data.firstSolution.teamName}}</a></td>
        </tr>
        </tbody>
    </table>
</div>

<div class="col-sm-6" style="display: block; min-height: 500px;"  ng-cloak>
    <nvd3-pie-chart
            data="data.data"
            id="problemChart"
            width="100%"
            height="500"
            y="yFunction()"
            x="xFunction()"
            color="areaColor()"
            showLabels="true"
            labelType="percent"
            noData="<spring:message code="chart.noData" />"
            showLegend="true"
            tooltips="true"
            tooltipcontent="toolTipContentFunction()">
        <svg></svg>
    </nvd3-pie-chart>
    <%@ include file="/WEB-INF/views/scoreboard/insight/fragment/insightLegend.jsp"%>
    <br />
    <br />
</div>