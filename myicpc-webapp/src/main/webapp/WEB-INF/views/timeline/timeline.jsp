<%@ taglib prefix="sp" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="javascript">
        <script src="<c:url value='/js/myicpc/controllers/timeline.js'/>" defer></script>

        <script type="application/javascript">
            function timelineAcceptPost(post) {
                var supportedNotificationTypes = ["submissionSuccess", "analystTeamMsg", "analystMsg", "twitter", "vine", "instagram", "picasa", "questChallenge", "adminNotification"];
                return supportedNotificationTypes.indexOf(post.type) != -1;
            }
            var timelineLoadMoreUrl = '<spring:url value="${contestURL}/timeline/loadMore" />';
            $(function() {
                Timeline.lastTimelineIdLoaded = ${not empty lastTimelineId ? lastTimelineId : 0};
                Timeline.init();
                Timeline.acceptFunction = timelineAcceptPost;
                startSubscribe('${r.contextPath}', '${contest.code}', 'notification', updateTimeline, null);
                videoAutoplayOnScroll();

                $(window).scroll(function() {
                    if($(window).scrollTop() === $(document).height() - $(window).height() && $('#timeline .timeline-loading').hasClass('hidden')) {

                    } else if($(window).scrollTop() === 0) {
                        Timeline.displayPendingNotification();
                    }
                });
                $(window).scroll(videoAutoplayOnScroll);

                // get upcoming events
                <c:if test="${util:scheduleModuleEnabled(contest)}">
                $.get("<spring:url value="${contestURL}/schedule/ajax/upcoming-events" />", function(data) {
                    $("#upcomingEventsContainer").html(data);
                });
                </c:if>
            });
        </script>
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
                <br/>
                <c:if test="${not empty openQuests}">
                    <%@ include file="/WEB-INF/views/timeline/timelineQuest.jsp"%>
                    <hr />
                </c:if>
                <div id="upcomingEventsContainer"></div>
                <%@ include file="/WEB-INF/views/timeline/timelineFollowedTeams.jsp"%>
            </div>
            <br class="clear" />
            <div class="col-sm-0 col-md-2"></div>
            <div class="media col-sm-8 col-md-6 col-lg-5">
                <button type="button" id="loadMoreTimeline" class="btn btn-default center-block"
                        onclick="Timeline.loadMorePosts('<spring:url value="${contestURL}/timeline/loadMore" />')">
                    <spring:message code="showMore" />
                </button>
                <div class="timeline-loading hidden">
                    <i class="fa fa-spinner fa-spin"></i> <spring:message code="loadingCtn" />
                </div>
            </div>
        </div>
    </jsp:body>

</t:template>
