<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>
    <jsp:attribute name="breadcrumb">
		  <li class="active"><spring:message code="contestOverviewAdmin.breadcrumb"/></li>
	</jsp:attribute>

    <jsp:attribute name="headline">
        <h3><spring:message code="contestOverviewAdmin.title" arguments="${contest.name}"/></h3>
    </jsp:attribute>

    <jsp:body>


    </jsp:body>
</t:templateAdmin>