    <%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
        <%@ include file="/WEB-INF/views/includes/nvd3Dependencies.jsp" %>
        <script src="<c:url value='/js/myicpc/controllers/insight.js'/>" defer></script>
        <script>
            window.location.hash = '#/team-problems';
        </script>
    </jsp:attribute>

  <jsp:attribute name="title">
      ${team.name}
  </jsp:attribute>

  <jsp:attribute name="headline">
      <span id="team_${team.id}_rank" class="label label-info">${team.rank }</span> ${team.name}
  </jsp:attribute>

    <jsp:body>
        <c:set var="teamContestId" value="${team.externalId}" />
        <c:set var="teamPresentationId" value="${teamInfo.externalId}" />
        <%@ include file="/WEB-INF/views/scoreboard/fragment/teamHomeMenu.jsp"%>
        <br class="clear"/>
        <br />
        <div ng-app="insight" class="col-sm-12">
            <div ng-view></div>
        </div>
    </jsp:body>
</t:template>
