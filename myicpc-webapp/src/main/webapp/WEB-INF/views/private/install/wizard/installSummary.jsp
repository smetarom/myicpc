<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<table class="table table-striped">
    <tbody>
        <t:labelTableRow label="installAdmin.wizard.setupAdmin.username">${adminUser.username}</t:labelTableRow>
        <t:labelTableRow label="globalSettings.adminEmail" rendered="${not empty globalSettings.adminEmail}">${globalSettings.adminEmail}</t:labelTableRow>
        <t:labelTableRow label="globalSettings.callbackURL" rendered="${not empty globalSettings.callbackUrl}">${globalSettings.callbackUrl}</t:labelTableRow>
        <t:labelTableRow label="globalSettings.cmURL" rendered="${not empty globalSettings.contestManagementSystemUrl}">${globalSettings.contestManagementSystemUrl}</t:labelTableRow>
        <t:labelTableRow label="globalSettings.teamPicturesUrl" rendered="${not empty globalSettings.teamPicturesUrl}">${globalSettings.teamPicturesUrl}</t:labelTableRow>
        <t:labelTableRow label="globalSettings.fbAPIKey" rendered="${not empty globalSettings.fbAPIKey}">${globalSettings.fbAPIKey}</t:labelTableRow>
        <t:labelTableRow label="globalSettings.googleNonAuthenticatedKey" rendered="${not empty globalSettings.googleNonAuthenticatedKey}">${globalSettings.googleNonAuthenticatedKey}</t:labelTableRow>
        <t:labelTableRow label="globalSettings.googleAnalyticsKey" rendered="${not empty globalSettings.googleAnalyticsKey}">${globalSettings.googleAnalyticsKey}</t:labelTableRow>
        <t:labelTableRow label="globalSettings.smtpHost" rendered="${not empty globalSettings.smtpHost}">${globalSettings.smtpHost}</t:labelTableRow>
        <t:labelTableRow label="globalSettings.smtpPort" rendered="${not empty globalSettings.smtpPort}">${globalSettings.smtpPort}</t:labelTableRow>
        <t:labelTableRow label="globalSettings.smtpUsername" rendered="${not empty globalSettings.smtpUsername}">${globalSettings.smtpUsername}</t:labelTableRow>
    </tbody>
</table>
<hr />


<div class="form-group text-right">
    <button type="submit" class="btn btn-primary">
        <spring:message code="installAdmin.wizard.summary.btn"/>
    </button>
</div>