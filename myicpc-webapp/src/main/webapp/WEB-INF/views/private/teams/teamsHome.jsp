<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="teams" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="teams" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
        <li class="active"><spring:message code="teams" /></li>
	</jsp:attribute>

    <jsp:attribute name="controls">
        <t:button href="/private${contestURL}/teams/synchronize" styleClass="btn-hover"><t:glyphIcon icon="transfer" /> <spring:message code="teamAdmin.sync" /></t:button>
    </jsp:attribute>

    <jsp:body>
        <table class="table">
            <thead>
            <tr>
                <th><spring:message code="team" /></th>
                <th><spring:message code="university" /></th>
                <th><spring:message code="team.abbreviation" /></th>
                <th><spring:message code="team.hashtag" /></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="teamInfo" items="${teamInfos}">
                <tr>
                    <td><c:out value="${teamInfo.name}" /></td>
                    <td>${teamInfo.university.name}</td>
                    <td><c:out value="${teamInfo.abbreviation}" /></td>
                    <td>#${teamInfo.hashtag}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </jsp:body>
</t:templateAdmin>