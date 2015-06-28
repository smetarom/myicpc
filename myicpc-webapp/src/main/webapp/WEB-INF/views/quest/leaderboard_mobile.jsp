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

            <table class="table table-striped table-condensed">
                <thead>
                    <tr class="leaderboard-header-row">
                        <th><spring:message code="quest.leaderboard.rank" /></th>
                        <th ><spring:message code="quest.leaderboard.participant" /></th>
                        <th class="text-center"><spring:message code="quest.leaderboard.points" /></th>
                        <th class="text-center"><spring:message code="quest.leaderboard.numSolved" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="questParticipant" items="${participants}" varStatus="status">
                        <tr style="position: relative">
                            <td>${status.index + 1}</td>
                            <td><a href="<spring:url value="${contestURL}/person/${questParticipant.contestParticipant.id}" />">${questParticipant.contestParticipant.fullname}</a></td>
                            <td class="text-center">${questParticipant.totalPoints}</td>
                            <td class="text-center">${questParticipant.acceptedSubmissions}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <c:if test="${empty leaderboards}">
            <div class="no-items-available">
                <spring:message code="quest.leaderboard.noResult" />
            </div>
        </c:if>

    </jsp:body>
</t:template>
