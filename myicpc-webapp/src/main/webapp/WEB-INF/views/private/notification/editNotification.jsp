<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdminEdit>
    <jsp:attribute name="title">
		${headlineTitle}
	</jsp:attribute>

	<jsp:attribute name="headline">
		${headlineTitle}
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li><a href="<spring:url value="/private${contestURL}/quest" />"><spring:message code="nav.admin.quest" /></a></li>
        <li class="active">${headlineTitle}</li>
	</jsp:attribute>
	
	<jsp:body>
        
        <t:form action="/private${contestURL}/notifications/icpc/update" entity="notification" resetFormButton="true" cancelFormURL="/private${contestURL}/notifications/icpc">
            <jsp:attribute name="controls">
                <button type="submit" class="btn btn-primary">
                    <spring:message code="save" />
                </button>
            </jsp:attribute>
            <jsp:body>
                <t:springInput labelCode="adminNotification.title" path="title" required="true" id="notificationTitle" previewWiki="true" />
                <t:springInput labelCode="adminNotification.startDate" path="localStartDate" id="startDate" required="true" />
                <t:springInput labelCode="adminNotification.endDate" path="localEndDate" id="endDate" required="true" />
                <t:springTextarea labelCode="adminNotification.body" path="body" size="30" id="notificationBody" required="true" previewWiki="true"></t:springTextarea>
                <t:springInput labelCode="adminNotification.imageUrl" path="imageUrl" />
                <t:springInput labelCode="adminNotification.videoUrl" path="videoUrl" />
            </jsp:body>
        </t:form>


        <script type="text/javascript">
            $(function() {
                $('#startDate').datetimepicker(datePickerOptions);
                $('#endDate').datetimepicker(datePickerOptions);
            });
        </script>
    </jsp:body>
</t:templateAdminEdit>