<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div ng-init="init('${r.contextPath}', '${contest.code}', '<spring:message code="insight.problems" />')"></div>

<div ng-repeat="chartData in data" ng-cloak>
    <div style="position: relative;" class="panel panel-default panel-insight">
        <h2 class="text-center">
            <a href="#/problem/{{chartData.code}}"><spring:message code="problem" /> {{chartData.code}}</a>

            <nvd3-pie-chart data="chartData.data"
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
                            tooltipcontent="toolTipContentFunction()"
                    >
                <svg></svg>
            </nvd3-pie-chart>
        </h2>
    </div>
</div>