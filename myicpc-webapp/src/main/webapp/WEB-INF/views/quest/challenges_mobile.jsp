<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="headline">
        <spring:message code="nav.quest.challenges" />
    </jsp:attribute>
    <jsp:attribute name="title">
        <spring:message code="nav.quest.challenges" />
    </jsp:attribute>

    <jsp:body>
        <%@ include file="/WEB-INF/views/quest/fragment/questInfo.jsp" %>

        <c:if test="${not empty challenges}">
            <div class="col-sm-12">
                <jsp:include page="/WEB-INF/views/quest/fragment/challengeSubmenu.jsp">
                    <jsp:param name="isMobile" value="true"/>
                </jsp:include>
            </div>
        </c:if>
        <c:if test="${empty challenges}">
            <div class="no-items-available">
                <spring:message code="quest.challenge.noResult" />
            </div>
        </c:if>
    </jsp:body>
</t:template>
