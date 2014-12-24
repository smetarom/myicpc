<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
        <%@ include file="/WEB-INF/views/includes/nvd3Dependencies.jsp" %>
        <script src="<c:url value='/js/myicpc/controllers/insight.js'/>" defer></script>
    </jsp:attribute>

  <jsp:attribute name="title">
      ${team.name}
  </jsp:attribute>

  <jsp:attribute name="headline">
      <span id="team_${team.id}_rank" class="label label-info">${team.rank }</span> ${team.name}
  </jsp:attribute>

    <jsp:body>
        <%@ include file="/WEB-INF/views/scoreboard/fragment/teamHomeMenu.jsp"%>
        <br class="clear"/>
        <br />
        <div ng-app="insight" class="col-sm-12">
            <div id="insightProblems" ng-controller="problemInsightCtrl">
                <div ng-repeat="problem in problems" ng-hide="!problem.data.length" class="col-sm-6 col-md-4">
                    <t:panelWithHeading>
                        <jsp:attribute name="heading">
                            <spring:message code="problem" /> {{problem.code}}
                        </jsp:attribute>
                        <jsp:body>
                            <nvd3-pie-chart
                                    data="problem.data"
                                    width="100%" height="400"
                                    x="xFunction()"
                                    y="yFunction()"
                                    color="areaColor()"
                                    showLabels="true"
                                    labelType="percent"
                                    noData="<spring:message code="chart.noData" />"
                                    showLegend="true"
                                    >
                                <svg></svg>
                            </nvd3-pie-chart>

                        </jsp:body>
                    </t:panelWithHeading>
                </div>

            </div>
        </div>

        <script type="application/javascript">
            $(function() {
               <c:if test="${not empty insightProblemModel}">
                var ngController = angular.element($("#insightProblems")).scope();
                ngController.init(${insightProblemModel});
               </c:if>
            });
        </script>
    </jsp:body>
</t:template>
