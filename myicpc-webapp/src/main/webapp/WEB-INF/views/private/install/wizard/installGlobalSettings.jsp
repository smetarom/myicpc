<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<p><spring:message code="installAdmin.wizard.setupAdmin.hint"/></p>

<t:springInput labelCode="installAdmin.wizard.globalSettings.adminEmail" path="adminEmail"/>
<t:springInput labelCode="installAdmin.wizard.globalSettings.fbAPIKey" path="fbAPIKey"/>
<t:springInput labelCode="installAdmin.wizard.globalSettings.googleNonAuthenticatedKey"
               path="googleNonAuthenticatedKey"/>
<t:springInput labelCode="installAdmin.wizard.globalSettings.googleAnalyticsKey" path="googleAnalyticsKey"/>