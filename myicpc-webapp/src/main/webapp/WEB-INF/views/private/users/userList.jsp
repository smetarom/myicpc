<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateGeneralAdmin>
	<jsp:attribute name="title">
		<spring:message code="userAdmin.title" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="userAdmin.title" />
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="userAdmin.title" /></li>
	</jsp:attribute>
	
	<jsp:attribute name="controls">
		<t:button href="/private/users/create" styleClass="btn-hover"><t:glyphIcon icon="plus"/> <spring:message code="userAdmin.createBtn" /></t:button>
		<button data-toggle="modal" data-target="#importUsersModal" class="btn btn-hover btn-default"><t:glyphIcon icon="import"/> <spring:message code="userAdmin.import.title" /></button>
		<t:button href="/private/users/report/systemUserReport.pdf" styleClass="btn-hover"><t:faIcon icon="file-pdf-o"/> <spring:message code="export.pdf" /></t:button>

		<t:modalWindow id="importUsersModal">
			<jsp:attribute name="title"><spring:message code="userAdmin.import.title" /></jsp:attribute>
			<jsp:body>
				<form class="form-inline" enctype="multipart/form-data" method="post" action='<spring:url value="/private/users/import" />'>
					<div class="form-group">
						<t:csvImport name="usersCSV" label="CSV file" />
					</div>
					<button type="submit" class="btn btn-primary">
						<spring:message code="import" />
					</button>
				</form>
				<p><spring:message code="userAdmin.import.hint" /></p>
			</jsp:body>
		</t:modalWindow>
	</jsp:attribute>
	
	<jsp:body>
		<div class="row">
			<div class="col-sm-12">
				<table class="table table-striped">
					<thead>
						<tr>
							<th><spring:message code="user.username" /></th>
							<th><spring:message code="user.name" /></th>
							<th class="text-center"><spring:message code="user.enabled" /></th>
							<th><spring:message code="user.roles" /></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="user" items="${users}">
							<tr>
								<td>${user.username}</td>
								<td>${user.fullname}</td>
								<td class="text-center"><t:tick condition="${user.enabled}" /></td>
								<td>
									<c:forEach var="role" items="${user.roles}">
										<span class="label label-default"><spring:message code="${role.userRoleLabel.code}" text="${role.userRoleLabel.label}" /></span>
									</c:forEach>
								</td>
								<td class="text-right">
									<t:editButton url="/private/users/${user.id}/edit" />
									<a href="<spring:url value="/private/users/${user.id}/changePassword" />" class="btn btn-default btn-xs"><span class="fa fa-refresh"></span> <spring:message
											code="userAdmin.changePassword"
										/></a>
									<t:deleteButton url="/private/users/${user.id}/delete" confirmMessageCode="userAdmin.delete.confirm" confirmMessageArgument="${user.username}" />
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>					
		
    </jsp:body>
</t:templateGeneralAdmin>