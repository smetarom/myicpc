<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:springInput labelCode="globalSettings.adminEmail" path="adminEmail" hintCode="globalSettings.adminEmail.hint" />
<t:springInput labelCode="globalSettings.callbackURL" path="callbackUrl" hintCode="globalSettings.callbackURL.hint" />
<t:springInput labelCode="globalSettings.cmURL" path="contestManagementSystemUrl" hintCode="globalSettings.cmURL.hint" />
<t:springInput labelCode="globalSettings.teamPicturesUrl" path="teamPicturesUrl" hintCode="globalSettings.teamPicturesUrl.hint"/>
<fieldset>
  <legend><spring:message code="globalSettings.socialSettings" /></legend>
  <t:springInput labelCode="globalSettings.fbAPIKey" path="fbAPIKey" hintCode="globalSettings.fbAPIKey.hint" />
  <t:springInput labelCode="globalSettings.googleNonAuthenticatedKey"
                 path="googleNonAuthenticatedKey" hintCode="globalSettings.googleNonAuthenticatedKey.hint"/>
  <t:springInput labelCode="globalSettings.googleAnalyticsKey" path="googleAnalyticsKey" hintCode="globalSettings.googleAnalyticsKey.hint"/>
</fieldset>
<fieldset>
  <legend><spring:message code="globalSettings.smtpSettings" /></legend>
  <p><spring:message code="globalSettings.smtpSettings.hint" /></p>
  <t:springInput labelCode="globalSettings.smtpHost" path="smtpHost" />
  <t:springInput labelCode="globalSettings.smtpPort" path="smtpPort" />
  <t:springInput labelCode="globalSettings.smtpUsername" path="smtpUsername" />
  <t:springInput type="password" labelCode="globalSettings.smtpPassword" path="smtpPassword" />
</fieldset>