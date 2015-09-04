<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateError>
    <jsp:attribute name="head">
        <style type="text/css">
          body {
            background-color: #FFFF7E;
          }
        </style>
    </jsp:attribute>

  <jsp:body>
    <div class="error-container center-block">
      <t:panelWithHeading>
        <div style="font-size: 4em;">
          <t:faIcon icon="question" />
        </div>

        <h1><spring:message code="error.contestNotFound" arguments="${exception.contestCode}" /></h1>

        <p style="font-size: 1.2em;">
          <spring:message code="error.contestNotFound.text" />
        </p>

        <a href="<spring:url value="/" />" class="btn btn-primary">
          <spring:message code="error.btn.dashboard" />
        </a>
      </t:panelWithHeading>
    </div>
  </jsp:body>
</t:templateError>
