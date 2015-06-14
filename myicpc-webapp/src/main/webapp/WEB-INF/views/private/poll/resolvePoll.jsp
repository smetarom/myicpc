<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdminEdit>
	<jsp:attribute name="headline">
		<spring:message code="pollAdmin.resolve.title" />: ${poll.question}
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li><a href="<spring:url value="/private${contestURL}/polls" />"><spring:message code="nav.admin.polls" /></a></li>
		<li class="active"><spring:message code="pollAdmin.resolve.title" /></li>
	</jsp:attribute>
	
	<jsp:body>	
		<t:form action="/private${contestURL}/poll/resolve" entity="poll" cancelFormURL="/private${contestURL}/polls">
			<jsp:attribute name="controls">
				<t:button type="submit" context="primary"><spring:message code="save" /></t:button>
			</jsp:attribute>
			<jsp:body>
				<t:springSelect labelCode="pollAdmin.resolve.correctAnswer" path="correctAnswer" options="${poll.options}" itemValue="id" itemLabel="name" defaultValue="true" />
				<t:springTextarea labelCode="poll.conclusionMessage" path="conclusionMessage" rows="4"></t:springTextarea>
			</jsp:body>
		</t:form>
    </jsp:body>
</t:templateAdminEdit>

