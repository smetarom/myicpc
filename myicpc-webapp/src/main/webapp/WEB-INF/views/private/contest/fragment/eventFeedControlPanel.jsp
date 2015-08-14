<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:alert context="${isFeedRunning ? 'success' : 'danger'}">
    <spring:message code="${isFeedRunning ? 'admin.panel.feed.status.running' : 'admin.panel.feed.status.stopped'}" />
</t:alert>
