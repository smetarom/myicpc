<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
        <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/nvd3/1.1.13-beta/nv.d3.min.css" type="text/css">
        <script src="//cdnjs.cloudflare.com/ajax/libs/d3/3.3.3/d3.min.js" defer></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/nvd3/1.1.13-beta/nv.d3.min.js" defer="defer"></script>
        <script src="<c:url value='/js/angular/angularjs-nvd3-directives.min.js'/>"></script>

        <script src="<c:url value='/js/myicpc/controllers/teamDetail.js'/>" defer></script>
    </jsp:attribute>

  <jsp:attribute name="title">
      ${team.name}
  </jsp:attribute>
  
  <jsp:attribute name="headline">
      <span id="team_${team.id}_rank" class="label label-info">${team.rank }</span> ${team.name}
  </jsp:attribute>

  <jsp:body>
      <%@ include file="/WEB-INF/views/scoreboard/fragment/teamHomeMenu.jsp"%>
      <div id="teamDetail" ng-app="teamDetail" ng-controller="TeamDeatilCtrl">
          <div class="col-sm-6">
              <h3><spring:message code="teamHome.contest.timeline"/></h3>
              <table class="table table-condensed team-timeline">
                  <tbody>
                  <c:forEach var="event" items="${timeline}">
                      <tr class="${event.notification.notificationType.scoreboardSuccess ? 'success' : ''} ${event.notification.notificationType.scoreboardFailed ? 'danger' : ''}">
                          <td rowspan="2" class="fit-content time-cell">${util:formatMinutes(event.contestTime)}</td>
                          <td>
                              <strong><spring:message code="problem" /> ${event.teamProblem.problem.code}</strong>
                          </td>
                          <td class="text-right">
                              <c:if test="${event.notification.notificationType.scoreboardSuccess}">
                                  <strong><spring:message code="teamHome.contest.timeline.rank" arguments="${event.teamProblem.oldRank},${event.teamProblem.newRank}" /></strong>
                              </c:if>
                          </td>
                      </tr>
                      <tr class="${event.notification.notificationType.scoreboardSuccess ? 'success' : ''} ${event.notification.notificationType.scoreboardFailed ? 'danger' : ''}">
                          <td colspan="2">${event.notification.body}</td>
                      </tr>
                  </c:forEach>
                  </tbody>
              </table>
          </div>

          <div class="col-sm-6">
              <h3><spring:message code="teamHome.contest.rankHistory" /></h3>
              <nvd3-line-chart
                      data="rankHistory"
                      id="exampleId"
                      xAxisTickFormat="xAxisTickFormatFunction()"
                      yAxisTickFormat="yAxisTickFormatFunction()"
                      width="100%"
                      height="350"
                      isArea="true"
                      forcey="[120]"
                      interactive="true"
                      useInteractiveGuideLine="true"
                      showYAxis="true">
                  <svg></svg>
              </nvd3-line-chart>

              <h3><spring:message code="teamHome.contest.problems" /></h3>

              <table class="table">
                  <thead>
                      <tr>
                          <th>#</th>
                          <th><spring:message code="problem" /></th>
                          <th class="text-center"><spring:message code="problem.solved" /></th>
                      </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="problem" items="${problems}">
                        <tr>
                            <td>${problem.code}</td>
                            <td><a href="<spring:url value="${contestURL}/problem/${problem.code}" />">${problem.name}</a></td>
                            <td class="text-center" id="problem_${problem.id}_for_team_${team.id}">
                                <c:choose>
                                    <c:when test="${not empty team.currentProblems[problem.id] and team.currentProblems[problem.id].solved}">
                                        <span class="glyphicon glyphicon-ok"></span>
                                    </c:when>
                                    <c:when test="${not empty team.currentProblems[problem.id] and not team.currentProblems[problem.id].solved}">
                                        <div class="progress problem-progress" title="<spring:message code="team.submission.passed" arguments="${team.currentProblems[problem.id].numTestPassed},${team.currentProblems[problem.id].totalNumTests}" />">
                                            <div class="progress-bar progress-bar-danger" role="progressbar"
                                                 aria-valuenow="<fmt:formatNumber value="${team.currentProblems[problem.id].numTestPassed*100/team.currentProblems[problem.id].totalNumTests}"  type="number" maxFractionDigits="0"/>" aria-valuemin="0"
                                                 aria-valuemax="100" style="width: <fmt:formatNumber value="${team.currentProblems[problem.id].numTestPassed*100/team.currentProblems[problem.id].totalNumTests}"  type="number" maxFractionDigits="0"/>%"
                                                    ></div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="glyphicon glyphicon-remove"></span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                  </tbody>
              </table>
          </div>
      </div>

      <script type="application/javascript">
          $(function() {
              var ngController = angular.element($("#teamDetail")).scope();
              ngController.setRankHistory(${not empty rankHistoryJSON ? rankHistoryJSON : '[]'});
          });
      </script>
  </jsp:body>
</t:template>
