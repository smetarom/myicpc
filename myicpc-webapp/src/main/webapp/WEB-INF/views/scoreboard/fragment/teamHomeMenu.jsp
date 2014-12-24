<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

  <div class="col-sm-12">
    <ul class="nav nav-pills">
      <t:menuItem activeItem="contest" active="${tab}" url="${contestURL}/team/${team.id}"><spring:message code="teamHome.nav.contest" /></t:menuItem>
      <t:menuItem activeItem="profile" active="${tab}" url="${contestURL}/team/${team.id}/profile"><spring:message code="teamHome.nav.about" /></t:menuItem>
      <t:menuItem activeItem="insight" active="${tab}" url="${contestURL}/team/${team.id}/insight"><spring:message code="teamHome.nav.insight" /></t:menuItem>
      <t:menuItem activeItem="social" active="${tab}" url="${contestURL}/team/${team.id}"><spring:message code="teamHome.nav.social" /></t:menuItem>
    </ul>
  </div>
