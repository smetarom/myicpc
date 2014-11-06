<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<p><spring:message code="installAdmin.wizard.setupAdmin.hint"/></p>

<t:springInput labelCode="installAdmin.wizard.setupAdmin.username" path="username" hintCode="user.username.hint"/>
<t:springInput labelCode="installAdmin.wizard.setupAdmin.password" path="password" type="password"
               hintCode="user.password.hint"/>
<t:springInput labelCode="user.passwordCheck" path="passwordCheck" type="password"/>
