<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="headline">
        <spring:message code="nav.quest" />
    </jsp:attribute>
    <jsp:attribute name="title">
        <spring:message code="nav.quest" />
    </jsp:attribute>

    <jsp:attribute name="head">
        <script src="<c:url value='/js/myicpc/controllers/timeline.js'/>" defer></script>
    </jsp:attribute>

    <jsp:body>
        <%@ include file="/WEB-INF/views/quest/fragment/questInfo.jsp" %>

        <c:if test="${not empty voteCandidates}">
            <div class="col-sm-12">
                <div id="vote-panel" class=" clearfix">
                    <div class="pull-right">
                        <t:button styleClass="btn-hover" onclick="$('#vote-panel').hide()"><spring:message code="quest.vote.hide" /></t:button>
                    </div>
                    <br class="clear"/>
                    <div class="row-eq-height">
                        <c:forEach var="submission" items="${voteCandidates}">
                            <div class="col-sm-3 thumbnail">
                                <div>
                                    <t:questSubmission submission="${submission}" videoAutoplay="true" />
                                    <p class="text-center">
                                        <t:button context="primary"><spring:message code="vote" /></t:button>
                                    </p>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:if>

        <div class="col-sm-6">
            <h3><spring:message code="quest.challenge.title" /></h3>
            <c:if test="${not empty challenges}">
                <div id="quest-challanges-carousel" class="carousel slide" data-ride="carousel" data-interval="20000">
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <c:forEach var="challenge" items="${challenges}" varStatus="status">
                            <div class="item ${status.index == 0 ? 'active' : ''}">
                                <c:if test="${not empty challenge.imageURL}">
                                    <img src="${challenge.imageURL}" alt="${challenge.name}" class="center-block img-responsive" onerror='this.style.display = "none"'>
                                </c:if>
                                <div class="challenge-description">
                                    <h4>
                                        <c:if test="${not empty challenge.secondsToEndDate and challenge.secondsToEndDate gt 0}">
                                            <span class="pull-right"><span id="challenge-${challenge.id}-countdown"></span> <spring:message code="quest.flashChallenge.left" /></span>
                                        </c:if>
                                            ${challenge.name}
                                    </h4>
                                    <p>${challenge.description}</p>
                                    <div class="text-center">
                                        <button class="btn btn-primary" onclick="showParticipateChallenge('${challenge.hashtag}', '${challenge.name}')">
                                            <spring:message code="quest.participateThisChallenge" />
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Controls -->
                    <c:if test="${challenges.size() gt 1}">
                        <a class="left carousel-control" href="#quest-challanges-carousel" data-slide="prev"> <span class="glyphicon glyphicon-chevron-left"></span></a>
                        <a class="right carousel-control" href="#quest-challanges-carousel" data-slide="next"> <span class="glyphicon glyphicon-chevron-right"></span>
                    </c:if>
                </a>
                </div>

                <%--<%@ include file="/WEB-INF/views/fragment/quest/participateChallengeModal.jsp"%>--%>
                <script type="text/javascript">
                    $(function() {
                        <c:forEach var="challenge" items="${challenges}">
                            <c:if test="${not empty challenge.secondsToEndDate and challenge.secondsToEndDate gt 0}">
                                var timediff${challenge.id} = ${challenge.secondsToEndDate};
                                flashChallengeTime${challenge.id}();
                                var flashChallengeInterval${challenge.id} = setInterval(flashChallengeTime${challenge.id}, 1000);

                                function flashChallengeTime${challenge.id}() {
                                    if (timediff${challenge.id} >= 0) {
                                        $("#challenge-${challenge.id}-countdown").html(convertSecondsToHHMMSS(timediff${challenge.id}));
                                        timediff${challenge.id} -= 1;
                                    } else {
                                        $("#challenge-${challenge.id}-countdown").html('<spring:message code="quest.challenge.over" />');
                                        clearInterval(flashChallengeInterval${challenge.id});

                                    }
                                }
                            </c:if>
                        </c:forEach>
                    });
                </script>
            </c:if>
            <c:if test="${empty challenges}">
                <div class="no-items-available">
                    <spring:message code="quest.challenge.noResult" />
                </div>
            </c:if>
        </div>

        <div class="col-sm-6">
            <h3><spring:message code="quest.feed.title" /></h3>

            <%@ include file="/WEB-INF/views/timeline/timelineNotificationTemplates.jsp"%>
            <div id="timeline-body">
                <c:forEach items="${notifications}" var="notification">
                    <t:notification notification="${notification}" />
                </c:forEach>
            </div>
        </div>

        <script type="application/javascript">
            function timelineAcceptPost(post) {
                var supportedNotificationTypes = ["twitter", "vine"];
                if (supportedNotificationTypes.indexOf(post.type) == -1) {
                    return false;
                }
                var questHashtag = '${not empty questHashtag ? questHashtag : '#Quest'}';
                console.log(post.body.search(new RegExp(questHashtag, "i")));
                return post.body.search(new RegExp(questHashtag, "i")) != -1;
            }

            $(function() {
                Timeline.init();
                Timeline.ignoreScrolling = true;
                Timeline.acceptFunction = timelineAcceptPost;
                startSubscribe('${r.contextPath}', '${contest.code}', 'notification', updateTimeline, null);
                videoAutoplayOnScroll();

                $(window).scroll(videoAutoplayOnScroll);
            });
        </script>

    </jsp:body>
</t:template>
