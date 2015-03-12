<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="headline">
        ${challenge.name}
    </jsp:attribute>
    <jsp:attribute name="title">
        ${challenge.name}
    </jsp:attribute>

    <jsp:body>
        <%@ include file="/WEB-INF/views/quest/fragment/questInfo.jsp" %>

        <%@ include file="/WEB-INF/views/quest/fragment/challengeDetail.jsp" %>
    </jsp:body>
</t:template>
