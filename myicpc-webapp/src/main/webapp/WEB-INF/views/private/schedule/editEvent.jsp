<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdminEdit>
	<jsp:attribute name="title">
		${headlineTitle}
	</jsp:attribute>

	<jsp:attribute name="headline">
		${headlineTitle}
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li><a href="<spring:url value="/private${contestURL}/schedule" />"><spring:message code="nav.admin.schedule" /></a></li>
		  <li class="active">${headlineTitle}</li>
	</jsp:attribute>
	
	<jsp:body>
		<spring:url var="formAction" value="/private${contestURL}/schedule/updateEvent" />
		<form:form class="form-horizontal" role="form" action="${formAction}" commandName="event">
		  	<t:springInput labelCode="event.name" path="name" required="true" />
		  	<t:springInput labelCode="event.code" path="code" required="true" />
		  	<t:springInput labelCode="event.hashtag" path="hashtag" required="true" hintCode="event.hashtag.hint" />
		  	<t:springInput labelCode="event.picasaTag" path="picasaTag" />
		  	<t:springInput labelCode="event.startDate" path="localStartDate" id="startDate" required="true" />
		  	<t:springInput labelCode="event.endDate" path="localEndDate" id="endDate" required="true" />
		  	<t:springSelect options="${scheduleDays}" labelCode="event.scheduleDay" itemLabel="name" itemValue="id" path="scheduleDay" required="true" />
		  	<t:springSelect options="${locations}" labelCode="event.location" itemLabel="name" itemValue="id" path="location" />
		  	<t:springMultiSelect options="${eventRoles}" labelCode="event.roles" optionMap="${rolesMap}" path="roles"></t:springMultiSelect>
		  	<t:springTextarea labelCode="event.description" path="description" id="description" previewWiki="true"></t:springTextarea>
		  	<t:springTextarea labelCode="event.thingsToBring" path="thingsToBring" id="thingsToBring"></t:springTextarea>
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
				$('#startDate').datetimepicker(datePickerOptions);

				$('#endDate').datetimepicker(datePickerOptions);
			});
		</script>
    </jsp:body>
</t:templateAdminEdit>