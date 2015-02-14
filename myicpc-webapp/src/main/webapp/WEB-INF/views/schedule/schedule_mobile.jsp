<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="title">
        ${pageHeadline}
    </jsp:attribute>

    <jsp:attribute name="headline">
        ${pageHeadline}
    </jsp:attribute>

    <jsp:body>
        <jsp:include page="/WEB-INF/views/schedule/fragment/scheduleTable.jsp">
            <jsp:param name="isMobile" value="true" />
        </jsp:include>
    </jsp:body>

</t:template>
