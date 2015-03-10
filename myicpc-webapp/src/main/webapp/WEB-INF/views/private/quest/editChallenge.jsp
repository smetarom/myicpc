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
        
        <t:form action="/private${contestURL}/quest/challenge/update" entity="challenge" resetFormButton="true" cancelFormURL="/private${contestURL}/quest/challenges">
            <jsp:attribute name="controls">
                <button type="submit" class="btn btn-primary">
                    <spring:message code="save" />
                </button>
            </jsp:attribute>
            <jsp:body>
                <t:springInput labelCode="quest.challange.name" path="name" required="true" />
                <div class="form-group">
                    <form:label path="hashtagSuffix" class="col-sm-3 control-label">
                        <spring:message code="quest.challange.hashtag.suffix" />:* </form:label>
                    <div class="col-sm-9">
                        <div class="input-group">
                            <span class="input-group-addon">#${questHashtagPrefix}</span>
                            <form:input path="hashtagSuffix" required="reguired" class="form-control" />
                        </div>
                    </div>
                    <div class="col-sm-offset-3 col-sm-9">
                        <form:errors path="hashtagSuffix" cssClass="formError" />
                    </div>
                </div>
                <t:springInput labelCode="quest.challange.defaultPoints" path="defaultPoints" required="true" type="number" />
                <t:springInput labelCode="quest.challange.imageURL" path="imageURL" />
                <t:springInput labelCode="quest.challange.startDate" path="localStartDate" id="startDate" required="true" />
                <t:springInput labelCode="quest.challange.endDate" path="localEndDate" id="endDate" required="true" />
                <t:springTextarea labelCode="quest.challange.description" path="description" rows="3" />
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