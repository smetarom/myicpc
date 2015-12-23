<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="nav.admin.contestErrors" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="nav.admin.contestErrors" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
        <li class="active"><spring:message code="nav.admin.contestErrors" /></li>
	</jsp:attribute>

    <jsp:body>
        <%@ include file="fragment/errorTable.jsp" %>
    </jsp:body>
</t:templateAdmin>
