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
		<spring:url var="formAction" value="/private${contestURL}/schedule/updateScheduleDay" />
		<form:form class="form-horizontal" role="form" action="${formAction}" commandName="scheduleDay">
		  	<t:springInput labelCode="scheduleDay.name" path="name" required="true" />
		  	<t:springInput labelCode="scheduleDay.dayOrder" path="dayOrder" type="number" required="true" />
		  	<t:springTextarea labelCode="scheduleDay.description" path="description" size="30" />
		  	<t:springInput labelCode="scheduleDay.date" path="localDate" required="true" id="scheduleDayDate" />
		  
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
		
		<script type="text/javascript">
			$(function() {				
				$( "#scheduleDayDate" ).datepicker({
					dateFormat: "yy-mm-dd"
				});
				$( "#scheduleDayOrder" ).spinner();
			});
		</script>
    </jsp:body>
</t:templateAdminEdit>