<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="notificationAdmin" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="notificationAdmin" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="nav.admin.notifications.icpc" /></li>
	</jsp:attribute>

    <jsp:attribute name="controls">
        <a href='<spring:url value="/private${contestURL}/notifications/icpc/create" />' class="btn btn-default btn-hover"><t:glyphIcon icon="plus"/> <spring:message code="notificationAdmin.createBtn" /></a>
    </jsp:attribute>

  <jsp:body>
    <table class="table">
      <thead>
      <tr>
        <th><spring:message code="adminNotification.title" /></th>
        <th><spring:message code="adminNotification.startDate" /></th>
        <th><spring:message code="adminNotification.endDate" /></th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="notification" items="${notifications}">
        <tr>
          <td>${notification.title}</td>
          <td><fmt:formatDate type="both" value="${notification.localStartDate}" /></td>
          <td><fmt:formatDate type="both" value="${notification.localEndDate}" /></td>
          <td class="text-right">
            <t:editButton url="/private${contestURL}/notifications/icpc/${notification.id}/edit" />
            <t:deleteButton url="/private${contestURL}/notifications/icpc/${notification.id}/delete" confirmMessageCode="adminNotification.delete.confirm" confirmMessageArgument="${notification.title}" />
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </jsp:body>
</t:templateAdmin>