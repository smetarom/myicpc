<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="title">
        ${pageHeadline}
    </jsp:attribute>

    <jsp:attribute name="headline">
        ${pageHeadline}
    </jsp:attribute>

    <jsp:body>
        <div class="col-sm-4">
            <jsp:include page="/WEB-INF/views/schedule/fragment/scheduleTable.jsp" />
        </div>
        <div class="col-sm-8">
            <t:emptyLink id="editScheduleRolesBtn" styleClass="alert-link btn btn-primary pull-right"><spring:message code="schedule.editMySchedule.link" /></t:emptyLink>
            <br class="clear" />
            <div id="eventContainer">
                <div class="no-items-available">
                    <spring:message code="schedule.noSelected" />
                </div>
            </div>
        </div>

        <script type="text/javascript">
            function loadEventContent(eventId) {
                $("#eventContainer").html('<div class="inline-spinner"></div>');
                $.get( '<spring:url value="${contestURL}/schedule/ajax/event/" />'+eventId, function( data ) {
                    $("#eventContainer").html(data);
                });
            };

            $(function() {
                if (window.location.hash != '') {
                    loadEventContent(window.location.hash.substring(1));
                    $("#eventContainer").html('<div class="inline-spinner"></div>');
                }
            });
        </script>

    </jsp:body>

</t:template>
