<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<c:if test="${not empty notifications}">
    <c:forEach var="notification" items="${notifications}">
        <t:notification notification="${notification}" />
    </c:forEach>
</c:if>
<c:if test="${empty notifications}">
    <div class="no-items-available-xsmall">
        <spring:message code="hashtagPanel.noResult" arguments="${hashtag1},${hashtag2}" />
    </div>
</c:if>

