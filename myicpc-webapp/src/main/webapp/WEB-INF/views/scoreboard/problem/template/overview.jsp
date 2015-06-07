<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div ng-init="init('${r.contextPath}', '${contest.code}', '${problem.code}')"></div>

<div ng-hide="dataLoaded" class="no-items-available">
  <t:spinnerIcon id="teamSubmissionSpinner" /> <spring:message code="loadingCtn" />
</div>

<nvd3-multi-bar-horizontal-chart
        data="data"
        x="xFunction()"
        y="yFunction()"
        yAxisTickFormat="valueFormatFunction()"
        valueFormat="valueFormatFunction()"
        id="problemOverview"
        width="100%"
        height="1700"
        margin="{left:150,top:0,bottom:0,right:50}"
        showValues="true"
        forcey="[0,50]"
        objectEquality="true"
        showLegend="true"
        interactive="true"
        tooltips="true"
        tooltipcontent="toolTipContentFunction()">
  <svg></svg>
</nvd3-multi-bar-horizontal-chart>

