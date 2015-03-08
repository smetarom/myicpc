<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
	<jsp:attribute name="headline">
		<spring:message code="questAdmin.title" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="nav.admin.quest" /></li>
	</jsp:attribute>

    <jsp:body>
        <div class="row">
            <div class="col-sm-12">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th><spring:message code="quest.challange" /></th>
                        <th><spring:message code="quest.challange.hashtag" /></th>
                        <th><spring:message code="quest.challange.startDate" /></th>
                        <th><spring:message code="quest.challange.endDate" /></th>
                        <th><spring:message code="quest.challange.defaultPoints" /></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="challenge" items="${challenges}">
                        <tr>
                            <td>${challenge.name}</td>
                            <td>${challenge.hashtag}</td>
                            <td><fmt:formatDate type="both" value="${challenge.localStartDate}" /></td>
                            <td><fmt:formatDate type="both" value="${challenge.localEndDate}" /></td>
                            <td>${challenge.defaultPoints}</td>
                            <td class="text-right">
                                <t:editButton url="/private${contestURL}/quest/challenge/${challenge.id}/edit" />
                                <t:deleteButton url="/private${contestURL}/quest/challenge/${challenge.id}/delete" confirmMessageCode="questAdmin.challenge.delete.confirm" confirmMessageArgument="${challenge.name}"></t:deleteButton>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <a href='<spring:url value="/private${contestURL}/quest/challenge/create" />' class="btn btn-primary"><spring:message code="questAdmin.createBtn" /></a>
            </div>
        </div>
    </jsp:body>
</t:templateAdmin>