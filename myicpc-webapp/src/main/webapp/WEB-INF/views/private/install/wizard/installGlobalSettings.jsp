<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<p><spring:message code="installAdmin.wizard.globalSettings.hint"/></p>

<t:springInput labelCode="installAdmin.wizard.globalSettings.adminEmail" path="adminEmail" hintCode="installAdmin.wizard.globalSettings.adminEmail.hint" />
<t:springInput labelCode="installAdmin.wizard.globalSettings.fbAPIKey" path="fbAPIKey" hintCode="installAdmin.wizard.globalSettings.fbAPIKey.hint" />
<t:springInput labelCode="installAdmin.wizard.globalSettings.googleNonAuthenticatedKey"
               path="googleNonAuthenticatedKey" hintCode="installAdmin.wizard.globalSettings.googleNonAuthenticatedKey.hint"/>
<t:springInput labelCode="installAdmin.wizard.globalSettings.googleAnalyticsKey" path="googleAnalyticsKey" hintCode="installAdmin.wizard.globalSettings.googleAnalyticsKey.hint"/>

<div class="form-group text-right">
    <button type="submit" class="btn btn-primary">
        <spring:message code="next"/>
    </button>
</div>