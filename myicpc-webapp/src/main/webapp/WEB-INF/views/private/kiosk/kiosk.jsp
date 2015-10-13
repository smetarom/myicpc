<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="nav.admin.kiosk" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="kioskContentAdmin" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="nav.admin.kiosk" /></li>
	</jsp:attribute>

    <jsp:attribute name="controls">
        <t:button href="/private${contestURL}/kiosk/change-mode" styleClass="btn-hover"><t:faIcon icon="exchange"/> <spring:message code="kioskContentAdmin.switchMode" /></t:button>
        <t:button href="/private${contestURL}/kiosk/content/create" styleClass="btn-hover"><t:glyphIcon icon="plus"/> <spring:message code="kioskContentAdmin.create" /></t:button>
        <t:button href="${contestURL}/kiosk/custom" styleClass="btn-hover"><t:faIcon icon="eye"/> <spring:message code="kioskContentAdmin.seePage" /></t:button>
    </jsp:attribute>

  <jsp:body>
    <table class="table">
      <thead>
      <tr>
        <th><spring:message code="kiosk.content" /></th>
        <th class="text-center"><spring:message code="kiosk.content.active" /></th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="content" items="${contentList}">
        <tr>
          <td>${content.name}</td>
          <td class="text-center"><t:tick condition="${content.active}" /></td>
          <td class="text-right">
            <t:editButton url="/private${contestURL}/kiosk/content/${content.id}/edit" />
            <t:deleteButton url="/private${contestURL}/kiosk/content/${content.id}/delete" confirmMessageCode="kioskContentAdmin.delete.confirm" confirmMessageArgument="${content.name}" />
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </jsp:body>
</t:templateAdmin>