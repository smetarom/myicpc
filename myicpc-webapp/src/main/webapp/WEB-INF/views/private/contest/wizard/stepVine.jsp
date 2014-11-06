<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<p><spring:message code="contest.vine.hint"/></p>

<t:springInput labelCode="contest.vine.email" path="webServiceSettings.vineEmail"/>
<t:springInput labelCode="contest.vine.password" path="webServiceSettings.vinePassword"/>

