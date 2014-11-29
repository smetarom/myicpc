<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdminWithSubmenu>
    <jsp:attribute name="title">
		<spring:message code="teams" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="teams" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
	  <c:if test="${active == 'List' }">
        <li class="active"><spring:message code="teams" /></li>
      </c:if>
	  <c:if test="${active ne 'List' }">
        <li><a href="<spring:url value="/private/teams" />"><spring:message code="teams" /></a></li>
      </c:if>
	  <c:if test="${not empty breadcrumb}">
        <li class="active">${breadcrumb}</li>
      </c:if>
	</jsp:attribute>

	<jsp:attribute name="submenu">
		<div class="well sidebar-nav">
          <ul class="nav nav-pills nav-stacked">
            <t:menuItem url="/private${contestURL}/teams" active="${active}" activeItem="List"><spring:message code="teamAdmin.list" /></t:menuItem>
            <t:menuItem url="/private${contestURL}/teams/synchronize" active="${active}" activeItem="Sync"><spring:message code="teamAdmin.sync" /></t:menuItem>
            <t:menuItem url="/private${contestURL}/teams/abbreviation" active="${active}" activeItem="Abbreviation"><spring:message code="teamAdmin.abbr" /></t:menuItem>
            <t:menuItem url="/private${contestURL}/teams/hashtags" active="${active}" activeItem="Hashtags"><spring:message code="teamAdmin.hashtag" /></t:menuItem>
          </ul>
        </div>
	</jsp:attribute>

<jsp:body>
  <jsp:include page="/WEB-INF/views/private//teams/fragment/team${active}.jsp" />
</jsp:body>
</t:templateAdminWithSubmenu>