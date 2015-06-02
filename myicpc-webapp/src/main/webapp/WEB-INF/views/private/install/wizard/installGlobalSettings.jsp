<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<p><spring:message code="installAdmin.wizard.globalSettings.hint"/></p>

<t:springInput labelCode="globalSettings.adminEmail" path="adminEmail" hintCode="globalSettings.adminEmail.hint" />
<t:springInput labelCode="globalSettings.fbAPIKey" path="fbAPIKey" hintCode="globalSettings.fbAPIKey.hint" />
<t:springInput labelCode="globalSettings.googleNonAuthenticatedKey"
               path="googleNonAuthenticatedKey" hintCode="globalSettings.googleNonAuthenticatedKey.hint"/>
<t:springInput labelCode="globalSettings.googleAnalyticsKey" path="googleAnalyticsKey" hintCode="globalSettings.googleAnalyticsKey.hint"/>
<t:springInput labelCode="globalSettings.teamPicturesUrl" path="teamPicturesUrl" hintCode="globalSettings.teamPicturesUrl.hint"/>

<div class="form-group text-right">
    <button type="submit" class="btn btn-primary">
        <spring:message code="next"/>
    </button>
</div>