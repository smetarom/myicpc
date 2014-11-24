<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateGeneralAdmin>
  <jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="installAdmin.welcome.title"/></li>
  </jsp:attribute>
  <jsp:attribute name="headline">
		<spring:message code="installAdmin.welcome.title"/>
  </jsp:attribute>

  <jsp:body>
    <t:alert><spring:message code="installAdmin.welcome.intro"/></t:alert>

    <div class="col-sm-4">
      <t:panelWithHeading>
          <jsp:attribute name="heading"><spring:message code="installAdmin.welcome.globalSettings.title"/></jsp:attribute>
          <jsp:body>
            <p><spring:message code="installAdmin.welcome.globalSettings.text"/></p>
            <div class="text-center"><a href="/private/contest/create" class="btn btn-primary"><spring:message code="installAdmin.welcome.globalSettings.btn"/> </a></div>
          </jsp:body>
      </t:panelWithHeading>
    </div>
    <div class="col-sm-4">
      <t:panelWithHeading>
          <jsp:attribute name="heading"><spring:message code="installAdmin.welcome.users.title"/></jsp:attribute>
          <jsp:body>
            <p><spring:message code="installAdmin.welcome.users.text"/></p>
            <div class="text-center"><a href="/private/contest/create" class="btn btn-primary"><spring:message code="installAdmin.welcome.users.btn"/> </a></div>
          </jsp:body>
      </t:panelWithHeading>
    </div>
    <div class="col-sm-4">
      <t:panelWithHeading>
          <jsp:attribute name="heading"><spring:message code="installAdmin.welcome.contests.title"/></jsp:attribute>
          <jsp:body>
            <p><spring:message code="installAdmin.welcome.contests.text"/></p>
            <div class="text-center"><a href="/private/contest/create" class="btn btn-primary"><spring:message code="installAdmin.welcome.contests.btn"/> </a></div>
          </jsp:body>
      </t:panelWithHeading>
    </div>
  </jsp:body>

</t:templateGeneralAdmin>