<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="title">
        ${pageHeadline}
    </jsp:attribute>

    <jsp:attribute name="headline">
        ${pageHeadline}
    </jsp:attribute>

    <jsp:body>
        <t:emptyLink id="editScheduleRolesBtn" styleClass="btn btn-primary btn-block no-border-radius" style="position:relative; top: -10px"><spring:message code="schedule.editMySchedule.link" /></t:emptyLink>

        <jsp:include page="/WEB-INF/views/schedule/fragment/scheduleTable.jsp">
            <jsp:param name="isMobile" value="true" />
        </jsp:include>

        <%@ include file="/WEB-INF/views/schedule/fragment/scheduleRolesDialog.jsp"%>
    </jsp:body>

</t:template>
