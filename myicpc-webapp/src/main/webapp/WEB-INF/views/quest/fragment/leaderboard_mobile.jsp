<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>
<table class="table table-striped table-condensed">
  <thead>
  <tr class="leaderboard-header-row">
    <th><spring:message code="quest.leaderboard.rank" /></th>
    <th ><spring:message code="quest.leaderboard.participant" /></th>
    <th class="text-center"><spring:message code="quest.leaderboard.points" /></th>
    <th class="text-center"><spring:message code="quest.leaderboard.numSolved" /></th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="questParticipant" items="${participants}" varStatus="status">
    <tr style="position: relative">
      <td>${status.index + 1}</td>
      <td><a href="<spring:url value="${contestURL}/person/${questParticipant.contestParticipant.id}" />">${questParticipant.contestParticipant.fullname}</a></td>
      <td class="text-center">${questParticipant.totalPoints}</td>
      <td class="text-center">${questParticipant.acceptedSubmissions}</td>
    </tr>
  </c:forEach>
  </tbody>
</table>