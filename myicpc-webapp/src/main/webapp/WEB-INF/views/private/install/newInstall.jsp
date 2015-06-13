<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdminEmpty>

    <jsp:body>
        <div class="page-header">
            <h1>&nbsp;<spring:message code="installAdmin.title"/></h1>
        </div>

        <div class="col-sm-12">
            <p><spring:message code="installAdmin.introduction" /></p>
            <br/>
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
                    <t:menuItem url="/private/install/admin" active="1"
                                activeItem="${currentStep}" disabled="${0 >= currentStep}">
                            1. <spring:message code="installAdmin.wizard.setupAdmin" /> <span class="glyphicon glyphicon-chevron-right pull-right"></span>
                    </t:menuItem>
                    <t:menuItem url="/private/install/settings" active="2"
                                activeItem="${currentStep}" disabled="${1 >= currentStep}">
                            2. <spring:message code="installAdmin.wizard.globalSettings" /> <span class="glyphicon glyphicon-chevron-right pull-right"></span>
                    </t:menuItem>
                    <t:menuItem url="/private/install/summary" active="3"
                                activeItem="${currentStep}" disabled="${2 >= currentStep}">
                            3. <spring:message code="installAdmin.wizard.summary" /> <span class="glyphicon glyphicon-chevron-right pull-right"></span>
                    </t:menuItem>
                </ul>
            </div>
            <div class="col-sm-8">
                <spring:url var="formAction" value="${formAction}"/>
                <form:form class="form-horizontal" role="form" action="${formAction}" commandName="${entity}">
                    <jsp:include page="/WEB-INF/views/private/install/wizard/install${steps[currentStep-1].key}.jsp"/>

                    <input type="hidden" name="currentStep" value="${currentStep}"/>

                </form:form>
            </div>
        </div>
    </jsp:body>

</t:templateAdminEmpty>