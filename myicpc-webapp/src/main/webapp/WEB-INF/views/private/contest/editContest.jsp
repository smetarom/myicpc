<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateGeneralAdminWithSubmenu>
	<jsp:attribute name="headline">
		${headline}
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li class="active">${headline}</li>
	</jsp:attribute>
	
	<jsp:attribute name="submenu">
		<div class="progress">
            <div class="progress-bar" role="progressbar" aria-valuenow="${currentStep}" aria-valuemin="0"
                 aria-valuemax="${steps.size() + 1}" style="width: ${(currentStep *100) / steps.size()}%;">
                <spring:message code="ofPage" arguments="${currentStep},${steps.size()}"></spring:message>
            </div>
        </div>
		<ul class="nav nav-pills nav-stacked">
            <c:forEach var="step" items="${steps}" varStatus="status">
                <t:menuItem url="/private/contest/create/${status.index+1}" active="${status.index + 1}"
                            activeItem="${currentStep}"
                            disabled="${status.index >= currentStep}">${status.index+1}. ${step.value} <span
                        class="glyphicon glyphicon-chevron-right pull-right"></span></t:menuItem>
            </c:forEach>
        </ul>
	</jsp:attribute>

    <jsp:body>
        <spring:url var="formAction" value="${formAction}"/>
        <form:form class="form-horizontal" role="form" action="${formAction}" commandName="contest">
            <jsp:include page="/WEB-INF/views/private/contest/wizard/step${steps[currentStep-1].key}.jsp"/>

            <input type="hidden" name="currentStep" value="${currentStep}"/>

            <div class="form-group text-right">
                <a href="<spring:url value="/private/home" />" class="btn btn-default"><spring:message
                        code="cancel"/> </a>
                <button type="submit" class="btn btn-primary">
                    <spring:message code="next"/>
                </button>
            </div>
        </form:form>


    </jsp:body>
</t:templateGeneralAdminWithSubmenu>