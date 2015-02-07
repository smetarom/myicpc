<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div ng-init="init('${r.contextPath}', '${contest.code}', '<spring:message code="insight.languages" />')"></div>

<div class="col-sm-12">
    <%@ include file="/WEB-INF/views/scoreboard/insight/fragment/insightLegend.jsp"%>
</div>

<div class="col-sm-6">
    <nvd3-multi-bar-chart data="data"
                          id="languageChart"
                          width="100%"
                          height="500"
                          stacked="true"
                          color="colorFunction()"
                          showLegend="true"
                          showXAxis="true"
                          showYAxis="true"
                          tooltips="true"
                          tooltipcontent="toolTipContentFunction()"
                          showControls="${sitePreference.mobile ? 'false' : 'true' }"
                          noData="<spring:message code="chart.noData" />">
        <svg></svg>
    </nvd3-multi-bar-chart>
</div>