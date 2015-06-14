<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="nav.admin.polls" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="pollAdmin.title" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
        <li class="active"><spring:message code="pollAdmin.title" /></li>
	</jsp:attribute>

    <jsp:attribute name="controls">
        <a href='<spring:url value="/private${contestURL}/poll/create"/>' class="btn btn-default btn-hover"><t:glyphIcon icon="plus" /> <spring:message code="pollAdmin.create" /></a>
    </jsp:attribute>

    <jsp:body>
        <table class="table table-striped">
            <thead>
            <tr>
                <th><spring:message code="poll.question" /></th>
                <th><spring:message code="poll.startDate" /></th>
                <th><spring:message code="poll.endDate" /></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="poll" items="${polls}">
                <tr>
                    <td>${poll.question}</td>
                    <td><fmt:formatDate type="both" value="${poll.localStartDate}" /></td>
                    <td><fmt:formatDate type="both" value="${poll.localEndDate}" /></td>
                    <td class="text-right">
                        <a href='<spring:url value="/private${contestURL}/poll/${poll.id}/resolve" />' class="btn btn-primary btn-xs"><span class="glyphicon glyphicon-ok"></span> <spring:message code="pollAdmin.resolve" /> </a>
                        <t:editButton url="/private${contestURL}/poll/${poll.id}/edit" />
                        <t:deleteButton url="/private${contestURL}/poll/${poll.id}/delete" confirmMessageCode="pollAdmin.delete.delete.confirm" confirmMessageArgument="${poll.question}"></t:deleteButton>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </jsp:body>
</t:templateAdmin>