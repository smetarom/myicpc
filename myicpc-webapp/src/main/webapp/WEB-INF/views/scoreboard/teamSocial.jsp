<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
        <%@ include file="/WEB-INF/views/includes/nvd3Dependencies.jsp" %>
        <script src="<c:url value='/js/myicpc/controllers/insight.js'/>" defer></script>
    </jsp:attribute>

  <jsp:attribute name="title">
      ${teamInfo.name}
  </jsp:attribute>

  <jsp:attribute name="headline">
      <span id="team_${team.id}_rank" class="label label-info">${team.rank }</span> ${teamInfo.name}
  </jsp:attribute>

    <jsp:body>
        <%@ include file="/WEB-INF/views/scoreboard/fragment/teamHomeMenu.jsp"%>

    </jsp:body>
</t:template>