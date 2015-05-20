<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
	<jsp:attribute name="title">
		<spring:message code="questAdmin.participants.title" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="questAdmin.participants.title" />
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
        <li><a href="<spring:url value="/private${contestURL}/quest/challenges" />"><spring:message code="nav.admin.quest" /></a></li>
		<li class="active"><spring:message code="questAdmin.participants.title" /></li>
	</jsp:attribute>
	
	<jsp:body>
        <c:if test="${not empty participants}">
            <t:plainForm action="/private${contestURL}/quest/participants">
                <br />
                <button type="submit" class="btn btn-primary pull-right">
                    <spring:message code="saveAll" />
                </button>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th><spring:message code="quest.participant.name" /></th>
                            <th><spring:message code="quest.participant.twitter" /></th>
                            <th><spring:message code="quest.participant.instagram" /></th>
                            <th><spring:message code="quest.participant.vine" /></th>
                            <th><spring:message code="quest.participant.adjustmentPoints" /></th>
                            <th><spring:message code="quest.participant.totalPoints" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="participant" items="${participants}">
                        <tr>
                            <td>${participant.contestParticipant.officialFullname}</td>
                            <td>${participant.contestParticipant.twitterUsername}</td>
                            <td>${participant.contestParticipant.instagramUsername}</td>
                            <td>${participant.contestParticipant.vineUsername}</td>
                            <td style="width: 200px;"><input type="number" name="${participant.id}" value="${participant.pointsAdjustment}" class="form-control" size="4" style="width: 100px;" /></td>
                            <td style="width: 200px;">${participant.points}</td>
                        </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <button type="submit" class="btn btn-primary pull-right">
                    <spring:message code="saveAll" />
                </button>
            </t:plainForm>
        </c:if>
        <c:if test="${empty participants}">

        </c:if>
    </jsp:body>
</t:templateAdmin>