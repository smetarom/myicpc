<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateEmpty>

    <jsp:body>
        <div class="page-header">
            <h1>&nbsp;<spring:message code="installAdmin.title"/></h1>
        </div>

        <div class="clearfix" style="margin: 0 15px;">
            <div class="col-sm-4">
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
            </div>
            <div class="col-sm-8">
                <spring:url var="formAction" value="${formAction}"/>
                <form:form class="form-horizontal" role="form" action="${formAction}" commandName="${entity}">
                    <jsp:include page="/WEB-INF/views/private/install/wizard/install${steps[currentStep-1].key}.jsp"/>

                    <input type="hidden" name="currentStep" value="${currentStep}"/>

                    <div class="form-group text-right">
                        <button type="submit" class="btn btn-primary">
                            <spring:message code="next"/>
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </jsp:body>

</t:templateEmpty>