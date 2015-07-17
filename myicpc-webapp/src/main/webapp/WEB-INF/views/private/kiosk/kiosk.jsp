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
        <a href='<spring:url value="/private${contestURL}/kiosk/content/create" />' class="btn btn-default btn-hover"><t:glyphIcon icon="plus"/> <spring:message code="kioskContentAdmin.create" /></a>
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