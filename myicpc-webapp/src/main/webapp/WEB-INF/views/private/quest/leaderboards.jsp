<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="questAdmin.leaderboards.title" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="questAdmin.leaderboards.title" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
        <li><a href="<spring:url value="/private${contestURL}/quest/challenges" />"><spring:message code="nav.admin.quest" /></a></li>
	    <li class="active"><spring:message code="nav.admin.quest.leaderboards" /></li>
	</jsp:attribute>

    <jsp:attribute name="controls">
        <a href='<spring:url value="/private${contestURL}/quest/leaderboard/create" />' class="btn btn-default btn-hover"><t:glyphIcon icon="plus" /> <spring:message code="questAdmin.leaderboards.createBtn" /></a>
    </jsp:attribute>

    <jsp:body>
        <div class="row">
            <div class="col-sm-12">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th><spring:message code="quest.leaderboard.name" /></th>
                        <th><spring:message code="quest.leaderboard.roles" /></th>
                        <th><spring:message code="quest.leaderboard.published" /></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="leaderboard" items="${leaderboards}">
                        <tr>
                            <td>${leaderboard.name}</td>
                            <td>
                                <c:forEach var="role" items="${leaderboard.contestParticipantRoles}">
                                    <spring:message code="${role.code}" text="${role.label}" />,
                                </c:forEach>
                            </td>
                            <td><t:tick condition="${leaderboard.published}" /></td>
                            <td class="text-right">
                                <t:editButton url="/private${contestURL}/quest/leaderboard/${leaderboard.id}/edit" />
                                <t:deleteButton url="/private${contestURL}/quest/leaderboard/${leaderboard.id}/delete" confirmMessageCode="questAdmin.leaderboards.delete.confirm" confirmMessageArgument="${leaderboard.name}"></t:deleteButton>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </jsp:body>
</t:templateAdmin>