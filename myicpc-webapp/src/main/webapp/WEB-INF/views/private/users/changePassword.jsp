<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdminEdit>
	<jsp:attribute name="headline">
		<spring:message code="userAdmin.changePassword" />: ${user.username}
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li><a href="<spring:url value="${breadcrumbURL}" />"><spring:message code="userAdmin.title" /></a></li>
		<li class="active"><spring:message code="userAdmin.changePassword" /></li>
	</jsp:attribute>

	<jsp:body>
		<spring:url var="formAction" value="${formURL}" />
		<form:form class="form-horizontal" role="form" action="${formAction}" commandName="systemUser">
		  <c:if test="${requireOldPassword}">
			  	<t:springInput labelCode="userAdmin.profile.oldPassword" path="oldPlainPassword" type="password" required="true" />
		  </c:if>
		  <t:springInput labelCode="user.password" path="password" type="password" required="true" />
		  <t:springInput labelCode="user.passwordCheck" path="passwordCheck" type="password" required="true" />
		  
		  <hr />
		  <div class="form-group text-right">
		    	<a href="<spring:url value="/private/profile" />" class="btn btn-default">
					<spring:message code="close" /> </a> 
		    	<button type="submit" class="btn btn-primary">
					<spring:message code="userAdmin.changePassword" /> 
				</button>
		  </div>
		</form:form>
    </jsp:body>
</t:templateAdminEdit>