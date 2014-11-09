<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
	<jsp:attribute name="headline">
		${user.fullname} <small>${user.username}</small>
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="userAdmin.profile.title" /></li>
	</jsp:attribute>

	<jsp:body>
		<table>
			<tbody>
				<tr>
					<th><spring:message code="user.enabled" />: </th>
					<td>${user.enabled}</td>
				</tr>
				<tr>
					<th style="min-width: 150px;">
						<spring:message code="user.roles" />: 
					</th>
					<td>
						<c:forEach var="role" items="${user.roles}">
							<span class="label label-default">${role.authorityLabel}</span>
						</c:forEach>
					</td>
				</tr>
			</tbody>
		</table>
		<br />
		<a href="<spring:url value="/private/profile/changePassword" />" class="btn btn-default"><spring:message code="userAdmin.changePassword" /></a>
						
		
    </jsp:body>
</t:templateAdmin>