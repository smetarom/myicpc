<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdminEdit>
    <jsp:attribute name="title">
        <spring:message code="participantAdmin.edit.title"/>: ${participant.fullname}
    </jsp:attribute>

	<jsp:attribute name="headline">
	    <spring:message code="participantAdmin.edit.title"/>: ${participant.fullname}
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
        <li><a href="<spring:url value="/private${contestURL}/participants" />"><spring:message code="participantAdmin.title"/></a></li>
		<li class="active"><spring:message code="participantAdmin.edit.title"/></li>
	</jsp:attribute>

  <jsp:body>
      <t:form action="/private${contestURL}/participant/${participant.id}/edit" entity="participant"
              resetFormButton="true" cancelFormURL="/private${contestURL}/participants">
		  	<jsp:attribute name="controls">
				<t:button type="submit" context="primary"><spring:message code="save"/></t:button>
            </jsp:attribute>

            <jsp:body>
                <t:springInput labelCode="participant.twitter" path="twitterUsername" />
                <t:springInput labelCode="participant.vine" path="vineUsername" />
                <t:springInput labelCode="participant.instagram" path="instagramUsername" />
            </jsp:body>
      </t:form>
  </jsp:body>
</t:templateAdminEdit>