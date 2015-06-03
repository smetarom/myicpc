<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<span class="label label-success"><spring:message code="quest.challenge.points" arguments="${challenge.defaultPoints}" /></span>
<c:if test="${challenge.requiresPhoto}">
    <span class="label label-default"><spring:message code="quest.challenge.attachPhoto" /></span>
</c:if>
<c:if test="${challenge.requiresPhoto and challenge.requiresVideo}">
    <spring:message code="or" />
</c:if>
<c:if test="${challenge.requiresVideo}">
    <span class="label label-default"><spring:message code="quest.challenge.attachVideo" /></span>
</c:if>