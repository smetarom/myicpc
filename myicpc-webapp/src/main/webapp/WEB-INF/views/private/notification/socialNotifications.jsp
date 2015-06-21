<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
	<jsp:attribute name="headline">
		<spring:message code="notificationAdmin.all" />
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="notificationAdmin.social" /></li>
	</jsp:attribute>
	
	<jsp:body>
		<div class="well well-sm">
			<span class="pull-right">
				<spring:message code="showingPages" arguments="${notificationPage.number + 1}, ${notificationPage.totalPages}" />
			</span>
			<form:form class="form-inline" role="form" action="${formAction}" commandName="notificationFilter">
			  <div class="form-group">
			    <form:label path="notificationType" class="control-label">
					<spring:message code="notification.origin" />: </form:label>
			    <form:select path="notificationType" class="form-control">
			    	<form:option value="">
							<spring:message code="notificationAdmin.filter.allOrigins" />
						</form:option>
		        	<form:options items="${notificationTypes}" itemLabel="description" />
		        </form:select>
			  </div>
			  <div class="form-group">
			    <form:label path="title" class="control-label">
					<spring:message code="notification.title" />: </form:label>
				<form:input path="title" class="form-control" />
			  </div>
			  <div class="form-group">
			    <form:label path="body" class="control-label">
					<spring:message code="notification.body" />: </form:label>
				<form:input path="body" class="form-control" />
			  </div>
			  <button type="submit" class="btn btn-default">
					<spring:message code="questAdmin.submissions.filter" />
				</button>
			</form:form>
		</div>
		
		<table class="table table-striped" id="socialNotifications">
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
						<td>${notification.notificationType.description}</td>
						<td>${notification.title}</td>
						<td style="max-width: 450px; overflow-x: hidden">${notification.body}</td>
						<td><fmt:formatDate type="both" value="${notification.timestamp}" /></td>
						<td class="text-right">
							<a onclick="showNotificatioPreview(${notification.id})" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-eye-open"></span></a>
							<button onclick="deleteNotification(this, ${notification.id})" class="btn btn-warning btn-sm">
								<t:glyphIcon icon="remove" /> <spring:message code="adminNotification.suspicious.delete" />
							</button>
							<button onclick="banNotification(this, ${notification.id})" class="btn btn-danger btn-sm">
								<t:glyphIcon icon="ban-circle" /> <spring:message code="adminNotification.suspicious.ban" />
							</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<t:pager pager="${notificationPage}" />

		
		<t:modalWindow id="previewNotificationModal">
            <jsp:attribute name="title">
                <spring:message code="notificationAdmin.social.preview" />
            </jsp:attribute>
            <jsp:body>
                ...
            </jsp:body>
        </t:modalWindow>
		
		<br />
		<script type="text/javascript">
			function banNotification(elem, notificationId) {
				if (confirm('<spring:message code="adminNotification.suspicious.ban.confirm" />')) {
					$.get('<spring:url value="/private${contestURL}/notifications/suspicious/" />' + notificationId + '/ban', function (data) {
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
			function showNotificatioPreview(notificationId) {
				var $previewNotification = $("#previewNotificationModal .modal-body");
				$previewNotification.empty();
				$.get('<spring:url value="/private${contestURL}/notifications/" />' + notificationId + '/preview', function(data) {
					$previewNotification.html(data);
					$("#previewNotificationModal").modal('show');
				});
			}
		</script>
    </jsp:body>
</t:templateAdmin>