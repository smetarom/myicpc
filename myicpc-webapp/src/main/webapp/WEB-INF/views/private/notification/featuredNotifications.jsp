<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
	<jsp:attribute name="headline">
		<spring:message code="notificationAdmin.featured" />
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="notificationAdmin.featured" /></li>
	</jsp:attribute>
	
	<jsp:body>
		<table class="table">
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
						<td style="max-width: 450px; overflow-x: hidden">${notification.body}</td>
						<td><fmt:formatDate type="both" value="${notification.timestamp}" /></td>
						<td class="text-right">
							<button onclick="removeFromFeaturedNotification(this, ${notification.id})" class="btn btn-danger btn-sm"><t:glyphIcon icon="remove"/> <spring:message code="notificationAdmin.featured.remove" /></button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<script type="text/javascript">
			function removeFromFeaturedNotification(elem, notificationId) {
				if (confirm('<spring:message code="notificationAdmin.featured.remove.confirm" var="removeConfirm"/>')) {
					$.get('<spring:url value="/private${contestURL}/notifications/featured/" />' + notificationId + '/remove', function () {
						$(elem).parent().parent().hide();
					});
					return true;
				}
				return false;
			}
		</script>
    </jsp:body>
</t:templateAdmin>