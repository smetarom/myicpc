<%@ taglib prefix="sp" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>
    <jsp:body>
        <script src="<c:url value='/js/myicpc/controllers/timeline.js'/>"></script>

        <script type="application/javascript">
            var timelineLoadMoreUrl = '<spring:url value="${contestURL}/timeline/loadMore" />';
            var featuredCarouselTemplate = compileHandlebarsTemplate("featured-notification-template")
            function timelineAcceptPost(post) {
                var supportedNotificationTypes = ["submissionSuccess", "analystTeamMsg", "analystMsg", "twitter", "vine", "instagram", "picasa", "gallery", "questChallenge", "adminNotification", "eventOpen"];
                return supportedNotificationTypes.indexOf(post.type) != -1;
            }
            function showTimelineSubmenu() {
                $("#notification-mobile-side-bar").toggle('slide',{direction: 'right'});
            }
            function showOfficialGallery(tag) {
                window.location.href = "<spring:url value="${contestURL}/gallery/official" />#" + tag;
            }
            function banNotification(elem, notificationId) {
                if (confirm('<spring:message code="adminNotification.suspicious.ban.confirm" />')) {
                    $.get('<spring:url value="/private${contestURL}/notifications/" />' + notificationId + '/ban', function (data) {
                        $(elem).parent().parent().hide();
                    });
                    return true;
                }
                return false;
            }
            function deleteNotification(elem, notificationId) {
                if (confirm('<spring:message code="adminNotification.suspicious.delete.confirm" />')) {
                    $.get('<spring:url value="/private${contestURL}/notifications/" />' + notificationId + '/delete', function (data) {
                        $(elem).parent().parent().hide();
                    });
                    return true;
                }
                return false;
            }
            function showNotificatioPreview(notificationId) {
                var $previewNotification = $("#previewNotificationModal .modal-body");
                $previewNotification.empty();
                $.get('<spring:url value="/private${contestURL}/notifications/" />' + notificationId + '/preview', function(data) {
                    $previewNotification.html(data);
                    $("#previewNotificationModal").modal('show');
                });
            }
            $(function() {
                Timeline.lastTimelineIdLoaded = ${not empty lastTimelineId ? lastTimelineId : 0};
                Timeline.init();
                Timeline.acceptFunction = timelineAcceptPost;
                startSubscribe('${r.contextPath}', '${contest.code}', 'notification', updateTimeline, null);

                videoAutoplayOnScroll();
                $(window).scroll(videoAutoplayOnScroll);
            });
        </script>

        <div id="timeline" class="clearfix">
            <%@ include file="/WEB-INF/views/timeline/timelineNewNotifications.jsp"%>
            <%@ include file="/WEB-INF/views/timeline/timelineNotificationTemplates.jsp"%>
            <div class="col-sm-0 col-md-2"></div>
            <div class="media col-sm-8 col-md-6 col-lg-5">
                <div id="timeline-body">
                    <c:forEach items="${notifications}" var="notification">
                        <t:notification notification="${notification}" editable="${editable}" />
                    </c:forEach>
                </div>
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

</t:templateAdmin>
