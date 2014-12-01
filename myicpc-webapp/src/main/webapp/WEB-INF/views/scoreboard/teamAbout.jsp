<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>

  <jsp:attribute name="title">
      ${team.name}
  </jsp:attribute>
  
  <jsp:attribute name="headline">
      <span id="team_${team.id}_rank" class="label label-info">${team.rank }</span> ${team.name}
  </jsp:attribute>

  <jsp:body>

  </jsp:body>
</t:template>
