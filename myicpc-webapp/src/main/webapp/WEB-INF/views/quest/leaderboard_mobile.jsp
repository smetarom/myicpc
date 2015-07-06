<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
        <script src="<c:url value='/js/myicpc/controllers/questLeaderboard.js'/>" defer></script>
    </jsp:attribute>
    <jsp:attribute name="headline">
        <spring:message code="quest.leaderboard" />
    </jsp:attribute>
    <jsp:attribute name="title">
        <spring:message code="quest.leaderboard" />
    </jsp:attribute>
    <jsp:attribute name="javascript">
        <script type="application/javascript">
            function updateLeaderboard() {
                var $leaderboardNotification = $("#leaderboard-notification");
                $leaderboardNotification.removeClass('hidden');
            }

            $(function() {
                startSubscribe('${r.contextPath}', '${contest.code}', 'quest', updateLeaderboard, null);

                $("#leaderboard-notification a").click(function() {
                    var $leaderboardNotification = $("#leaderboard-notification");
                    $leaderboardNotification.append('<span class="fa fa-spinner fa-spin"></span>');
                    $.get("<spring:url value="/${contestURL}/quest/leaderboard/${activeLeaderboard.urlCode}/update" />", function(data) {
                        $("#mainLeaderboardMobile").html(data);
                        $leaderboardNotification.addClass('hidden');
                        $leaderboardNotification.find(':last-child').remove();
                    })
                });
            })
        </script>
    </jsp:attribute>


    <jsp:body>
        <%@ include file="/WEB-INF/views/quest/fragment/questInfo.jsp" %>

        <c:if test="${not empty leaderboards}">
            <c:if test="${leaderboards.size() > 1}">
                <t:secondLevelSubmenu isMobile="${sitePreference.mobile}">
                    <c:forEach var="leaderboard" items="${leaderboards}" varStatus="status">
                        <t:menuItem activeItem="${leaderboard.urlCode}" active="${activeTab}" url="${contestURL}/quest/leaderboard/${leaderboard.urlCode}">${leaderboard.name}</t:menuItem>
                    </c:forEach>
                </t:secondLevelSubmenu>
            </c:if>

            <div id="leaderboard-notification" class="alert alert-warning alert-dismissible hidden">
                <button type="button" class="close" onclick="$(this).parent().addClass('hidden');">&times;</button>
                <spring:message code="quest.leaderboard.newSubmission" /> <a href="javascript:void(0)" class="alert-link"><spring:message code="quest.leaderboard.newSubmission.refresh" /></a>
            </div>

            <div id="mainLeaderboardMobile">
                <%@include file="/WEB-INF/views/quest/fragment/leaderboard_mobile.jsp" %>
            </div>
        </c:if>

        <c:if test="${empty leaderboards}">
            <div class="no-items-available">
                <spring:message code="quest.leaderboard.noResult" />
            </div>
        </c:if>

    </jsp:body>
</t:template>
