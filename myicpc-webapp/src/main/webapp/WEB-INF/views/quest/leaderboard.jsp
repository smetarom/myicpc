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
            $(function() {
                var $leaderboard = $("#mainLeaderboard");
                var $scrollTable = $("#mainLeaderboard .scroller");
                var $fixedRows = $("#mainLeaderboard .fixed-rows table");
                var $nonFixedColumns = $("#mainLeaderboard .non-fixed-header table");
                var scrollbarWidth = 15;

                var top = $("#mainLeaderboard").offset().top;
                var bottom = $("#footer").offset().top;
                var header = $(".non-fixed-header").outerHeight(true);
                var tableHeight = bottom - top - scrollbarWidth;
                if (tableHeight < 350) {
                    tableHeight = 350;
                }
                $leaderboard.height(tableHeight);
                $nonFixedColumns.parent().width($leaderboard.width() - scrollbarWidth);
                $fixedRows.parent().height($leaderboard.height() - scrollbarWidth)

                $scrollTable.scroll(function () {
                    $fixedRows.css('top', ($scrollTable.scrollTop()*-1));
                    $nonFixedColumns.css('left', ($scrollTable.scrollLeft() * -1));
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

            <div id="mainLeaderboard" style="width: 100%; overflow-y: auto">
                <div class="table-container">
                    <table class="table fixed-header">
                        <thead>
                            <tr class="leaderboard-header-row">
                                <th class="col-rank"><spring:message code="quest.leaderboard.rank" /></th>
                                <th class="col-participant-name"><spring:message code="quest.leaderboard.participant" /></th>
                                <th class="col-role"><spring:message code="quest.leaderboard.role" /></th>
                            </tr>
                        </thead>
                    </table>
                    <div class="fixed-rows">
                        <table class="table table-striped table-condensed">
                            <thead>
                                <th colspan="2" class="leaderboard-header-row"></th>
                            </thead>
                            <tbody>
                            <c:forEach var="questParticipant" items="${participants}" varStatus="status">
                                <tr style="position: relative">
                                    <td class="col-rank">${status.index + 1}</td>
                                    <td class="col-participant-name"><a href="<spring:url value="${contestURL}/person/${questParticipant.contestParticipant.id}" />">${questParticipant.contestParticipant.fullname}</a></td>
                                    <td class="col-role">
                                        <c:forEach var="role" items="${questParticipant.contestParticipantRoles}">
                                            <spring:message code="${role.code}" text="${role.label}" /><br/>
                                        </c:forEach>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="non-fixed-header">
                        <table class="fixed-offset table">
                            <thead>
                            <tr class="leaderboard-header-row">
                                <th class="col-role"><spring:message code="quest.leaderboard.role" /></th>
                                <th class="col-points"><spring:message code="quest.leaderboard.points" /></th>
                                <th class="col-points"><spring:message code="quest.leaderboard.numSolved" /></th>
                                <c:forEach var="challenge" items="${challenges}">
                                    <th class="col-quest-label text-center">
                                        <div class="vertical-text">
                                            <a href="<spring:url value="${contestURL}/quest/challenges#" />${challenge.hashtag}">${challenge.hashtagSuffix}</a>
                                        </div>
                                    </th>
                                </c:forEach>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div class="scroller">
                        <table class="fixed-offset table table-striped table-condensed" style="width: auto">
                            <thead>
                                <th colspan="${challenges.size() + 3}" class="leaderboard-header-row"></th>
                            </thead>
                            <tbody>
                            <c:forEach var="questParticipant" items="${participants}" varStatus="status">
                                <tr style="position: relative">
                                    <td class="col-role">
                                        <c:forEach var="role" items="${questParticipant.contestParticipantRoles}">
                                            <spring:message code="${role.code}" text="${role.label}" /><br/>
                                        </c:forEach>
                                    </td>
                                    <td class="col-points">${questParticipant.totalPoints}</td>
                                    <td class="col-points">${questParticipant.acceptedSubmissions}</td>
                                    <c:forEach var="challenge" items="${challenges}">
                                        <td class="col-quest-label text-center">
                                            <t:questTick submission="${questParticipant.submissionMap[challenge.id]}" />
                                        </td>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </c:if>

        <c:if test="${empty leaderboards}">
            <div class="no-items-available">
                <spring:message code="quest.leaderboard.noResult" />
            </div>
        </c:if>

    </jsp:body>
</t:template>
