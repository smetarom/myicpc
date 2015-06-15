<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<c:if test="${not empty featuredNotifications}">
    <ul class="featured-list">
        <c:forEach var="notification" items="${featuredNotifications}">
            <li class="alert alert-info alert-dismissable">
                <button type="button" onclick="appendIdToCookieArray('ignoreFeaturedNotifications', ${notification.id}, '${ctx}')" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                <c:choose>
                    <c:when test="${notification.notificationType.questChallenge}">
                        <t:faIcon icon="trophy" />
                        <a href="<spring:url value="${contestURL}/quest/challenges#${notification.hashtags}" />">
                            ${notification.title}
                        </a>
                    </c:when>
                    <c:when test="${notification.notificationType.adminNotification}">
                        <t:faIcon icon="exclamation-triangle" />
                        <a href="javascript:void(0)" data-toggle="modal" data-target="#admin-notification-${notification.id}">
                            ${notification.title}
                        </a>
                    </c:when>
                </c:choose>
            </li>
        </c:forEach>
    </ul>
    <div class="text-center" style="background-color: #E0E0E0; padding: 5px 0">
        <a href="<spring:url value="${contestURL}/notifications" />"><strong><spring:message code="seeAll" /></strong></a>
    </div>
    <c:forEach var="notification" items="${featuredNotifications}">
        <c:if test="${notification.notificationType.adminNotification}">
            <t:modalWindow id="admin-notification-${notification.id}">
                                <jsp:attribute name="title">
                                    ${notification.title}
                                </jsp:attribute>
                <jsp:body>
                    ${notification.body}
                </jsp:body>
            </t:modalWindow>
        </c:if>
    </c:forEach>
</c:if>
<c:if test="${empty featuredNotifications}">
    <div class="no-items-available-small" style="background-color: #E0E0E0">
        <spring:message code="notification.allCaughtUp" />
    </div>
</c:if>

