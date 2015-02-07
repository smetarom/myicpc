<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div ng-init="init('${r.contextPath}', '${contest.code}', '<spring:message code="language" />')"></div>

<div class="col-sm-6">
    <table class="table" ng-hide="!data">
        <tbody>
        <tr>
            <th><spring:message code="insight.language.numProblemSolved" arguments="${language.name}" /> {{languageName}}:</th>
            <td>{{data.numProblemSolved}}</td>
        </tr>
        <tr>
            <th><spring:message code="insight.language.numProblemSubmitted" arguments="${language.name}" /> {{languageName}}:</th>
            <td>{{data.numProblemSubmitted}}</td>
        </tr>
        <tr>
            <th><spring:message code="insight.language.usedByNumTeams" arguments="${language.name}" /> {{languageName}}:</th>
            <td>{{data.usedByNumTeams}}</td>
        </tr>
        <tr>
            <th><spring:message code="insight.language.totalSubmissions" arguments="${language.name}" /> {{languageName}}:</th>
            <td>{{data.totalSubmissions}} ({{data.usagePercentage | number: 2}}%)</td>
        </tr>
        <tr ng-hide="!data.firstSubmission">
            <th><spring:message code="insight.language.firstSubmission" />:</th>
            <td>{{data.firstSubmission.time}}<br /><a href="<spring:url value="${contestURL}/team/{{data.firstSolution.teamId}}" />">{{data.firstSubmission.teamName}}</a></td>
        </tr>
        <tr ng-hide="!data.firstSolution">
            <th><spring:message code="insight.language.firstSolution" />:</th>
            <td>{{data.firstSolution.time}}<br /><a href="<spring:url value="${contestURL}/team/{{data.firstSolution.teamId}}" />">{{data.firstSolution.teamName}}</a></td>
        </tr>
        </tbody>
    </table>
</div>

<div class="col-sm-6">
    <nvd3-pie-chart data="data.data"
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