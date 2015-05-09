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
                <br/>
                <ul class="nav nav-pills nav-stacked">
                    <li>
                        <a data-toggle="collapse" href="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
                            <t:glyphIcon icon="screenshot" /> 3 new Quest challenges
                        </a>

                        <div class="collapse" id="collapseExample">
                            <h4>15 Year Coach</h4>
                            <p>20 Points. Photo and Answer: Someone who was recognized as bringing teams to the World Finals at least 15 times. Also include as text the person's full name.</p>
                            <h4>ACM President</h4>
                            <p>20 Points. Autograph of the ACM President. Tweet why he is famous.</p>
                        </div>
                    </li>
                    <li><a href="#"><t:glyphIcon icon="bullhorn" /> 2 unanswered polls</a></li>
                </ul>
                <hr />
                <%@ include file="/WEB-INF/views/timeline/timelineUpcomingEvents.jsp"%>
                <hr />
                <%@ include file="/WEB-INF/views/timeline/timelineFollowedTeams.jsp"%>
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
                function timelineAcceptPost(post) {
                    var supportedNotificationTypes = ["submissionSuccess", "analystTeamMsg", "analystMsg", "twitter", "vine"];
                    return supportedNotificationTypes.indexOf(post.type) != -1;
                }

                $(function() {
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
                });
            </script>
        </div>
    </jsp:body>

</t:template>
