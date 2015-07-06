<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>
<div class="table-container">
  <table class="table fixed-header">
    <thead>
    <tr class="leaderboard-header-row">
      <th class="col-rank"><spring:message code="quest.leaderboard.rank" /></th>
      <th class="col-participant-name"><spring:message code="quest.leaderboard.participant" /></th>
      <th class="col-role"><spring:message code="quest.leaderboard.role" /></th>
    </tr>
    </thead>
  </table>
  <div class="fixed-rows">
    <table class="table table-striped table-condensed">
      <thead>
      <th colspan="2" class="leaderboard-header-row"></th>
      </thead>
      <tbody>
      <c:forEach var="questParticipant" items="${participants}" varStatus="status">
        <tr style="position: relative">
          <td class="col-rank">${status.index + 1}</td>
          <td class="col-participant-name"><a href="<spring:url value="${contestURL}/person/${questParticipant.contestParticipant.id}" />">${questParticipant.contestParticipant.fullname}</a></td>
          <td class="col-role">
            <c:forEach var="role" items="${questParticipant.contestParticipantRoles}">
              <spring:message code="${role.code}" text="${role.label}" /><br/>
            </c:forEach>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
  <div class="non-fixed-header">
    <table class="fixed-offset table">
      <thead>
      <tr class="leaderboard-header-row">
        <th class="col-role"><spring:message code="quest.leaderboard.role" /></th>
        <th class="col-points"><spring:message code="quest.leaderboard.points" /></th>
        <th class="col-points"><spring:message code="quest.leaderboard.numSolved" /></th>
        <c:forEach var="challenge" items="${challenges}">
          <th class="col-quest-label text-center">
            <div class="vertical-text">
              <a href="<spring:url value="${contestURL}/quest/challenges#" />${challenge.hashtag}">${challenge.hashtagSuffix}</a>
            </div>
          </th>
        </c:forEach>
      </tr>
      </thead>
    </table>
  </div>
  <div class="scroller">
    <table class="fixed-offset table table-striped table-condensed" style="width: auto">
      <thead>
      <th colspan="${challenges.size() + 3}" class="leaderboard-header-row"></th>
      </thead>
      <tbody>
      <c:forEach var="questParticipant" items="${participants}" varStatus="status">
        <tr style="position: relative">
          <td class="col-role">
            <c:forEach var="role" items="${questParticipant.contestParticipantRoles}">
              <spring:message code="${role.code}" text="${role.label}" /><br/>
            </c:forEach>
          </td>
          <td class="col-points">${questParticipant.totalPoints}</td>
          <td class="col-points">${questParticipant.acceptedSubmissions}</td>
          <c:forEach var="challenge" items="${challenges}">
            <td class="col-quest-label text-center">
              <t:questTick submission="${questParticipant.submissionMap[challenge.id]}" />
            </td>
          </c:forEach>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>

<c:if test="${includeJavascript}">
    <script src="<c:url value='/js/myicpc/questLeaderboard.js'/>" defer></script>
</c:if>