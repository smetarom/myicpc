<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div ng-init="init('${r.contextPath}', '${contest.code}', '<spring:message code="insight.nav.code" />')"></div>

<div ng-repeat="problem in problems" ng-cloak>
    <div class="col-md-4 col-sm-6">
        <div class="panel panel-default">
            <h2 class="text-center">
                <spring:message code="problem" /> {{problem.code}}
            </h2>

            <div ng-hide="getChartData(problem.code) == null">
                <nvd3-multi-bar-chart
                        data="getChartData(problem.code)"
                        width="100%"
                        height="400"
                        showXAxis="true" showYAxis="true"
                        noData="<spring:message code="chart.noData" />"
                        showLegend="true"
                        tooltips="true"
                        xAxisStaggerLabels="true"
                        yAxisStaggerLabels="true"
                        tooltipcontent="toolTipContentFunction()"
                        >
                    <svg></svg>
                </nvd3-multi-bar-chart>
            </div>

            <div ng-show="getChartData(problem.code) == null" class="no-items-available" style="height: 300px">
                <spring:message code="insight.editActivity.noResult" />
            </div>

        </div>
    </div>
</div>