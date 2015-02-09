<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdminEdit>
	<jsp:attribute name="headline">
		${headlineTitle}
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li><a href="<spring:url value="/private${contestURL}/schedule" />"><spring:message code="nav.admin.schedule" /></a></li>
		  <li class="active">${headlineTitle}</li>
	</jsp:attribute>
	
	<jsp:body>
		<spring:url var="formAction" value="/private${contestURL}/schedule/updateEventRole" />
		<form:form class="form-horizontal" role="form" action="${formAction}" commandName="eventRole">
		  	<t:springInput labelCode="eventRole.name" path="name" required="true"></t:springInput>
		  
		  <hr />
		  <div class="form-group text-right">
		    	<button type="reset" class="btn btn-default">
					<spring:message code="reset" /> </button> 
				<a href="<spring:url value="/private${contestURL}/schedule" />" class="btn btn-default"><spring:message code="cancel" /> </a>
		    	<button type="submit" class="btn btn-primary">
					<spring:message code="save" />
				</button>
		  </div>
		</form:form>
    </jsp:body>
</t:templateAdminEdit>