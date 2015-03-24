<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="headline">
        <spring:message code="nav.quest" />
    </jsp:attribute>
    <jsp:attribute name="title">
        <spring:message code="nav.quest" />
    </jsp:attribute>

    <jsp:body>
        <%@ include file="/WEB-INF/views/quest/fragment/questInfo.jsp" %>

        <div class="col-sm-12">
            <ul class="nav nav-pills">
                <c:forEach var="leaderboard" items="${leaderboards}" varStatus="status">
                    <li class="${status.index == 0 ? 'active' : ''}"><a href="#">${leaderboard.name}</a></li>
                </c:forEach>
            </ul>
        </div>

    </jsp:body>
</t:template>
