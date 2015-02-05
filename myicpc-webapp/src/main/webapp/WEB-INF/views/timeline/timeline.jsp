<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
        <script src="<c:url value='/js/myicpc/controllers/timeline.js'/>" defer></script>
    </jsp:attribute>

    <jsp:body>
        <div id="timeline" class="clearfix">
            <%@ include file="/WEB-INF/views/timeline/timelineNewNotifications.jsp"%>
            <%@ include file="/WEB-INF/views/timeline/timelineNotificationTemplates.jsp"%>

            <div class="col-sm-0 col-md-2"></div>
            <div class="media col-sm-8 col-md-6 col-lg-5">
                <div id="timeline-body">
                    <c:forEach items="${notifications}" var="notification">
                        <t:notification notification="${notification}" />
                    </c:forEach>
                </div>
            </div>
            <div class="col-sm-4 col-md-4 col-lg-3">

            </div>
            <br class="clear" />
            <div class="col-sm-0 col-md-2"></div>
            <div class="media col-sm-8 col-md-6 col-lg-5">
                <button type="button" id="loadMoreTimeline" class="btn btn-default center-block">
                    <spring:message code="showMore" />
                </button>
                <div class="timeline-loading hidden">
                    <i class="fa fa-spinner fa-spin"></i> <spring:message code="loadingCtn" />
                </div>
            </div>

            <script type="application/javascript">
                $(function() {
                    Timeline.init();
                    startSubscribe('${r.contextPath}', '${contest.code}', 'notification', updateTimeline, null);

                    $(window).scroll(function() {
                        if($(window).scrollTop() === $(document).height() - $(window).height() && $('#timeline .timeline-loading').hasClass('hidden')) {

                        } else if($(window).scrollTop() === 0) {
                            console.log('adsdas')
                            Timeline.displayPendingNotification();
                        }
                    });
                });
            </script>
        </div>
    </jsp:body>

</t:template>
