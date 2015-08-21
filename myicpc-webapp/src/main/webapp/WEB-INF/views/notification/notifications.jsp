<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="headline">
        <spring:message code="featuredNotifications.title" />
    </jsp:attribute>
    <jsp:attribute name="title">
        <spring:message code="featuredNotifications.title" />
    </jsp:attribute>

    <jsp:attribute name="javascript">
        <script type="application/javascript">
            function dismissFeaturedNotification(button, notificationId) {
                dismissNotification(notificationId, '${ctx}');
                $(button).parent().parent().slideUp("slow");
            }

            $(function() {
                $("select").addClass('form-control');
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <%@ include file="/WEB-INF/views/timeline/timelineNotificationTemplates.jsp"%>

        <div class="container">
            <c:forEach items="${notifications}" var="notification">
                <div>
                    <div class="pull-right">
                        <button type="button" onclick="dismissFeaturedNotification(this, ${notification.id})" class="btn btn-primary btn-xs">
                            <t:glyphIcon icon="remove" /><spring:message code="dismiss" />
                        </button>
                    </div>
                    <t:notification notification="${notification}" />
                </div>
            </c:forEach>

            <c:if test="${empty notifications}">
                <div class="no-items-available">
                    <spring:message code="notification.allCaughtUp" />
                </div>
            </c:if>

        </div>

    </jsp:body>
</t:template>
