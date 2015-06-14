<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div ng-if="isBarChart(poll)">
  <c:if test="${not sitePreference.mobile}">
    <nvd3-multi-bar-horizontal-chart
            data="sortChartOptions(poll)"
            id="pollchart{{poll.id}}"
            x="xFunctionShort()"
            y="yFunction()"
            objectEquality="true"
            yAxisTickFormat="yAxisTickFormatFunction()"
            showXAxis="false"
            showYAxis="false"
            width="100%"
            height="{{150 + 25 * countPollAnsweredOptions(poll)}}"
            showValues="true"
            valueFormat="valueFormatFunction()"
            interactive="true"
            tooltips="true"
            tooltipcontent="toolTipContentFunction()"
            margin="{top: 10, right: 20, bottom: 30, left: 150}"
            noData="<spring:message code="chart.noData" />">
      <svg></svg>
    </nvd3-multi-bar-horizontal-chart>
  </c:if>

  <c:if test="${sitePreference.mobile}">
    <nvd3-multi-bar-horizontal-chart
            data="sortChartOptions(poll)"
            id="pollchart{{poll.id}}"
            x="xFunction()"
            y="yFunction()"
            objectEquality="true"
            yAxisTickFormat="yAxisTickFormatFunction()"
            showXAxis="false"
            showYAxis="false"
            width="100%"
            height="{{150 + 20 * countPollAnsweredOptions(poll)}}"
            interactive="true"
            tooltips="true"
            tooltipcontent="toolTipContentFunctionMobile()"
            margin="{top: 10, right: 20, bottom: 30, left: 10}"
            noData="<spring:message code="chart.noData" />">
      <svg class="mobile-bar-chart"></svg>
    </nvd3-multi-bar-horizontal-chart>
  </c:if>
</div>

<div ng-if="isPieChart(poll)">
    <nvd3-pie-chart
            data="sortChartOptions(poll)"
            id="pollchart{{poll.id}}"
            x="xFunction()"
            y="yFunction()"
            objectEquality="true"
            width="100%"
            height="${sitePreference.mobile ? 400 : 500}"
            showLegend="${not sitePreference.mobile}"
            showLabels="true"
            pieLabelsOutside="false"
            interactive="true"
            tooltips="true"
            tooltipcontent="pieToolTipContent()"
            showValues="true"
            labelType="percent"
            noData="<spring:message code="chart.noData" />">
        <svg></svg>
    </nvd3-pie-chart>
</div>