<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateGeneralAdmin>
  <jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="settingsAdmin.title"/></li>
  </jsp:attribute>
  <jsp:attribute name="headline">
		<spring:message code="settingsAdmin.title"/>
  </jsp:attribute>

  <jsp:body>
    <div class="col-sm-12">
      <spring:url var="formAction" value="/private/settings"/>
      <form:form class="form-horizontal" role="form" action="${formAction}" commandName="globalSettings">
        <t:springInput labelCode="globalSettings.adminEmail" path="adminEmail" hintCode="globalSettings.adminEmail.hint" />
        <t:springInput labelCode="globalSettings.fbAPIKey" path="fbAPIKey" hintCode="globalSettings.fbAPIKey.hint" />
        <t:springInput labelCode="globalSettings.googleNonAuthenticatedKey"
                       path="googleNonAuthenticatedKey" hintCode="globalSettings.googleNonAuthenticatedKey.hint"/>
        <t:springInput labelCode="globalSettings.googleAnalyticsKey" path="googleAnalyticsKey" hintCode="globalSettings.googleAnalyticsKey.hint"/>
        <div class="form-group text-right">
          <button type="submit" class="btn btn-primary"><spring:message code="save"/></button>
        </div>
      </form:form>
    </div>
  </jsp:body>

</t:templateGeneralAdmin>