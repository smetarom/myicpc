<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="notificationAdmin.suspicious" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="notificationAdmin.suspicious" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="notificationAdmin.suspicious" /></li>
	</jsp:attribute>

    <jsp:body>
        <table class="table" id="suspiciousNotifications">
            <thead>
            <tr>
                <th><spring:message code="notification.origin" /></th>
                <th><spring:message code="notification.title" /></th>
                <th><spring:message code="notification.body" /></th>
                <th><spring:message code="notification.createDate" /></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="notification" items="${notifications}">
                <tr>
                    <td>${notification.notificationType.label}</td>
                    <td>${notification.title}</td>
                    <td style="max-width: 450px; overflow-x: hidden">${notification.highlightedBody}</td>
                    <td><fmt:formatDate type="both" value="${notification.timestamp}" /></td>
                    <td class="text-right">
                        <a href="#" onclick="banNotification(this, ${notification.id})" class="btn btn-danger btn-sm">
                            <t:glyphIcon icon="ban-circle" /> <spring:message code="adminNotification.suspicious.ban" />
                        </a>
                        <a href="#" onclick="deleteNotification(this, ${notification.id})" class="btn btn-warning btn-sm">
                            <t:glyphIcon icon="remove" /> <spring:message code="adminNotification.suspicious.delete" />
                        </a>
                        <a href="#" onclick="ignoreNotification(this, ${notification.id})" class="btn btn-default btn-sm">
                            <t:glyphIcon icon="ok" /> <spring:message code="adminNotification.suspicious.ok" />
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <script type="application/javascript">
            function ignoreNotification(elem, notificationId) {
                $.get('<spring:url value="/private${contestURL}/notifications/suspicious/" />' + notificationId + '/ignore', function( data ) {
                    $(elem).parent().parent().hide();
                });
            }
            function banNotification(elem, notificationId) {
                if (confirm('<spring:message code="adminNotification.suspicious.ban.confirm" />')) {
                    $.get('<spring:url value="/private${contestURL}/notifications/" />' + notificationId + '/ban', function (data) {
                        $(elem).parent().parent().hide();
                    });
                    return true;
                }
                return false;
            }
            function deleteNotification(elem, notificationId) {
                if (confirm('<spring:message code="adminNotification.suspicious.delete.confirm" />')) {
                    $.get('<spring:url value="/private${contestURL}/notifications/" />' + notificationId + '/delete', function (data) {
                        $(elem).parent().parent().hide();
                    });
                    return true;
                }
                return false;
            }
        </script>

    </jsp:body>
</t:templateAdmin>