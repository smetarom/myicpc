<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>
<c:forEach items="${availableNotificationTypes}" var="type">
    <script id="timeline-${type}" type="text/x-jquery-tmpl">
        <t:notification type="${type}" />
    </script>
</c:forEach>