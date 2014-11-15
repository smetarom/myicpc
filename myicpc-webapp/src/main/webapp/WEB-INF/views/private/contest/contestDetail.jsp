<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>
    <jsp:attribute name="breadcrumb">
		  <li class="active"><spring:message code="contestOverviewAdmin.breadcrumb"/></li>
	</jsp:attribute>

    <jsp:attribute name="headline">
        <spring:message code="contestOverviewAdmin.title" arguments="${contest.name}"/>
    </jsp:attribute>

    <jsp:body>


    </jsp:body>
</t:templateAdmin>