<%@ taglib prefix="sp" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="javascript">
        <script src="<c:url value='/js/myicpc/controllers/timeline.js'/>" defer></script>

<script id="featured-notification-template" type="text/x-jquery-tmpl">
    <div id="{{carouselId}}" class="carousel slide timeline-carousel" data-ride="carousel" data-interval="15000" data-keyboard="false">
        <div class="carousel-inner" role="listbox">
            {{#each data}}
                <div class="item {{#if first}}active{{/if}}">
                    <table class="width100">
                        <tr>
                            <td style="width:15px">
                                <a href="{{../hashCarouselId}}" role="button" data-slide="prev">
                                    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                                </a>
                            </td>
                            <td>
                                <h4>{{title}}</h4>
                            </td>
                            <td class="text-right" style="width:15px">
                                <a href="{{../hashCarouselId}}" role="button" data-slide="next">
                                    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                                </a>
                            </td>
                        </tr>
                    </table>
                    {{#if ../poll}}
                    <t:plainForm action="/${contestURL}/poll/submit-answer-redirect">
                        <select name="optionId" class="form-control" onchange="this.form.submit()">
                            <option label="<spring:message code="selectOption" />"><spring:message code="selectOption" /></option>
                            {{#each body}}
                              <option value="{{key}}" label="{{name}}">{{name}}</option>
                            {{/each}}
                        </select>
                        <input type="hidden" name="pollId" value="{{entityId}}" />
                    </t:plainForm>
                    {{/if}}
                    {{#unless ../poll}}
                        {{{body}}}
                    {{/unless}}
                    {{#if ../quest}}
                        <div class="text-center">
                            <a href="<spring:url value="${contestURL}/quest/challenges#" />{{entityId}}" class="btn btn-primary btn-sm">
                                <spring:message code="quest.challenge.participate" />
                            </a>
                        </div>
                    {{/if}}
                </div>
            {{/each}}
        </div>
    </div>
    <hr/>
</script>

        <script type="application/javascript">
            var timelineLoadMoreUrl = '<spring:url value="${contestURL}/timeline/loadMore" />';
            var featuredPolls = ${not empty openPolls ? openPolls : '[]'};
            var featuredQuests = ${not empty openQuests ? openQuests : '[]'};
            var featuredAdminnotifications = ${not empty openAdminNotifications ? openAdminNotifications : '[]'};
            var featuredCarouselTemplate = compileHandlebarsTemplate("featured-notification-template")
            function timelineAcceptPost(post) {
                var supportedNotificationTypes = ["submissionSuccess", "analystTeamMsg", "analystMsg", "twitter", "vine", "instagram", "picasa", "questChallenge", "adminNotification"];
                return supportedNotificationTypes.indexOf(post.type) != -1;
            }
            function renderFeaturedNotifications() {
                if (featuredPolls.length > 0) {
                    for (var i = 0, len = featuredPolls.length; i < len; i++) {
                        featuredPolls[i].body = JSON.parse(featuredPolls[i].body);
                        featuredPolls[i].first = false;
                    }
                    featuredPolls[0].first = true;
                    $("#timeline-featured-polls").html(featuredCarouselTemplate({
                        carouselId: 'timeline-poll-carousel',
                        hashCarouselId: '#timeline-poll-carousel',
                        data: featuredPolls,
                        poll: true
                    }));
                }
                if (featuredAdminnotifications.length > 0) {
                    for (var i = 0, len = featuredAdminnotifications.length; i < len; i++) {
                        featuredAdminnotifications[i].first = false;
                    }
                    featuredAdminnotifications[0].first = true;
                    $("#timeline-featured-admin-notifications").html(featuredCarouselTemplate({
                        carouselId: 'timeline-admin-notification-carousel',
                        hashCarouselId: '#timeline-admin-notification-carousel',
                        data: featuredAdminnotifications
                    }));
                }
                if (featuredQuests.length > 0) {
                    for (var i = 0, len = featuredQuests.length; i < len; i++) {
                        featuredQuests[i].first = false;
                    }
                    featuredQuests[0].first = true;
                    $("#timeline-featured-quest-challenge").html(featuredCarouselTemplate({
                        carouselId: 'timeline-quest-carousel',
                        hashCarouselId: '#timeline-quest-carousel',
                        data: featuredQuests,
                        quest: true
                    }));
                }
            }

            $(function() {
                Timeline.lastTimelineIdLoaded = ${not empty lastTimelineId ? lastTimelineId : 0};
                Timeline.init();
                Timeline.acceptFunction = timelineAcceptPost;
                startSubscribe('${r.contextPath}', '${contest.code}', 'notification', updateTimeline, null);

                videoAutoplayOnScroll();
                $(window).scroll(videoAutoplayOnScroll);

                // timeline featured notification
                renderFeaturedNotifications();

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
            <div class="col-sm-4 col-md-4 col-lg-3 timeline-submenu">
                <br/>
                <div id="timeline-featured-admin-notifications"></div>
                <div id="timeline-featured-polls"></div>
                <div id="timeline-featured-quest-challenge"></div>
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
