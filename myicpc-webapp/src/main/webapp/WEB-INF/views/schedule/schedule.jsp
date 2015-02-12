<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
        <%@ include file="/WEB-INF/views/includes/nvd3Dependencies.jsp" %>
        <script src="<c:url value='/js/myicpc/controllers/scorebar.js'/>" defer></script>
    </jsp:attribute>

    <jsp:attribute name="title">
        ${pageHeadline}
    </jsp:attribute>

    <jsp:attribute name="headline">
        ${pageHeadline}
    </jsp:attribute>

    <jsp:body>
        <div class="col-sm-4">
            <c:forEach var="day" items="${schedule}">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th colspan="2"><fmt:formatDate value="${day.localDate}" pattern="EEEE MMMM dd" /> - ${day.name}</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="event" items="${day.eventsChronologically}">
                        <tr>
                            <td style="width: 115px">

                                <fmt:formatDate type="time" pattern="HH:mm" value="${event.localStartDate}" /> - <fmt:formatDate type="time" pattern="HH:mm" value="${event.localEndDate}" />
                            </td>
                            <td><a href="#${event.code}" onclick="loadEventContent(${event.id})">${event.name}</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:forEach>
        </div>
        <div class="col-sm-8">
            <t:emptyLink id="editScheduleRolesBtn" styleClass="alert-link btn btn-primary pull-right"><spring:message code="schedule.editMySchedule.link" /></t:emptyLink>
            <br class="clear" />
            <div id="eventContainer">
                <div class="noSelectedBig">
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
