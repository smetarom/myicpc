<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<p><spring:message code="installAdmin.wizard.globalSettings.hint"/></p>

<%@ include file="/WEB-INF/views/private/settings/fragment/settingsFormFields.jsp"%>

<div class="form-group text-right">
    <button type="submit" class="btn btn-primary">
        <spring:message code="next"/>
    </button>
</div>