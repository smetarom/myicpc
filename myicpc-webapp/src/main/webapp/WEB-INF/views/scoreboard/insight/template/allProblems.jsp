<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<c:if test="${isTeamInsight}">
    <div ng-init="initForTeam('${team.externalId}', '${r.contextPath}', '${contest.code}', '<spring:message code="insight.problems" />')"></div>
</c:if>
<c:if test="${not isTeamInsight}">
    <div ng-init="init('${r.contextPath}', '${contest.code}', '<spring:message code="insight.problems" />')"></div>
</c:if>

<div ng-repeat="problem in problems" ng-cloak>
    <div style="position: relative;" class="panel panel-default panel-insight">
        <h2 class="text-center">
            <c:if test="${not isTeamInsight}">
                <a href="#/problem/{{problem.code}}">
            </c:if>
            <spring:message code="problem" /> {{problem.code}}
            <c:if test="${not isTeamInsight}">
                </a>
            </c:if>
        </h2>

        <div class="text-center">
            {{submissionsPerProblem(problem.code)}} <spring:message code="insight.problems.submissions" />
        </div>

        <nvd3-pie-chart data="getChartData(problem.code)"
                        width="100%"
                        height="400"
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

    </div>
</div>