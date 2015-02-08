<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div ng-init="init('${r.contextPath}', '${contest.code}', '<spring:message code="insight.problems" />')"></div>

<div ng-repeat="problem in problems" ng-cloak>
    <div style="position: relative;" class="panel panel-default panel-insight">
        <h2 class="text-center">
            <a href="#/problem/{{problem.code}}"><spring:message code="problem" /> {{problem.code}}</a>
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