<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateGeneralAdmin>
    <jsp:attribute name="title">
		<spring:message code="nav.admin.errors" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="nav.admin.errors" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
        <li class="active"><spring:message code="nav.admin.errors" /></li>
	</jsp:attribute>

    <jsp:body>
        <%@ include file="fragment/errorTable.jsp" %>
    </jsp:body>
</t:templateGeneralAdmin>
