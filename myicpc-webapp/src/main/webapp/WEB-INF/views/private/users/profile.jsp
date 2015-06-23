<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateGeneralAdmin>
	<jsp:attribute name="title">
		${systemUser.fullname}
	</jsp:attribute>

	<jsp:attribute name="headline">
		${systemUser.fullname} <small>${systemUser.username}</small>
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="userAdmin.profile.title" /></li>
	</jsp:attribute>

	<jsp:attribute name="controls">
		<t:button href="/private/profile/edit" styleClass="btn-hover"><t:glyphIcon icon="pencil"/> <spring:message code="userAdmin.editProfile" /></t:button>
		<t:button href="/private/profile/changePassword" styleClass="btn-hover"><t:glyphIcon icon="lock"/> <spring:message code="userAdmin.changePassword" /></t:button>
	</jsp:attribute>

	<jsp:body>
		<table>
			<tbody>
				<t:labelTableRow label="user.roles">
					<c:forEach var="role" items="${systemUser.roles}">
						<span class="label label-default"><spring:message code="${role.userRoleLabel.code}" text="${role.userRoleLabel.label}" /></span>
					</c:forEach>
				</t:labelTableRow>
			</tbody>
		</table>
		<br />
    </jsp:body>
</t:templateGeneralAdmin>