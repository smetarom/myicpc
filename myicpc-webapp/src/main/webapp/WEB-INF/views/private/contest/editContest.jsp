<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateGeneralAdminWithSubmenu>
	<jsp:attribute name="headline">
		${headline}
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
        <c:choose>
            <c:when test="${editMode}">
                <li><a href="<spring:url value="/private${contestURL}"/>">${contest.shortName}</a></li>
                <li class="active"><spring:message code="contestAdmin.edit.breadcrumb" /></li>
            </c:when>
            <c:otherwise>
	            <li class="active">${headline}</li>
            </c:otherwise>
        </c:choose>
	</jsp:attribute>
	
	<jsp:attribute name="submenu">
        <c:if test="${not editMode}">
            <div class="progress">
                <div class="progress-bar" role="progressbar" aria-valuenow="${currentStep}" aria-valuemin="0"
                     aria-valuemax="${steps.size() + 1}" style="width: ${(currentStep *100) / steps.size()}%;">
                    <spring:message code="ofPage" arguments="${currentStep},${steps.size()}"></spring:message>
                </div>
            </div>
        </c:if>
		<ul class="nav nav-pills nav-stacked">
            <c:forEach var="step" items="${steps}" varStatus="status">
                <t:menuItem url="${stepURL}${status.index+1}" active="${status.index + 1}"
                            activeItem="${currentStep}"
                            disabled="${not editMode and status.index >= currentStep}">
                    ${status.index+1}. ${step.value} <span class="glyphicon glyphicon-chevron-right pull-right"></span>
                </t:menuItem>
            </c:forEach>
        </ul>
	</jsp:attribute>

    <jsp:body>
        <script type="application/javascript">
            function toogleModuleInputs(checkbox) {
                var form = $(checkbox).closest('form');
                form.find("input").not(".moduleSwitch,:hidden").attr('disabled', !$(checkbox).prop('checked'));
            }

            $(function() {
                $("input.moduleSwitch").bootstrapSwitch();
            })
        </script>

        <t:form action="${formAction}" entity="contest" cancelFormURL="${cancelAction}">
            <jsp:attribute name="controls">
                <c:choose>
                    <c:when test="${editMode or currentStep == steps.size()}">
                        <t:button type="submit" context="primary"><spring:message code="save"/></t:button>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${currentStep > 3}">
                            <t:button href="${stepURL}${steps.size()}"><spring:message code="contestAdmin.wizard.sskipToSummary" /></t:button>
                        </c:if>
                        <t:button type="submit" context="primary"><spring:message code="next"/></t:button>
                    </c:otherwise>
                </c:choose>
            </jsp:attribute>
            <jsp:body>
                <jsp:include page="/WEB-INF/views/private/contest/wizard/step${steps[currentStep-1].key}.jsp"/>

                <input type="hidden" name="currentStep" value="${currentStep}"/>
            </jsp:body>
        </t:form>
    </jsp:body>
</t:templateGeneralAdminWithSubmenu>