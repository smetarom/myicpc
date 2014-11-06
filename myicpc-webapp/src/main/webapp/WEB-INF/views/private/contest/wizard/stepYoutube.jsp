<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<p><spring:message code="contest.youtube.hint"/></p>

<t:springInput labelCode="contest.youtube.username" path="webServiceSettings.youTubeUsername"/>
<t:springInput labelCode="contest.youtube.pullSince" path="webServiceSettings.youtubePullSince"/>

