<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<c:if test="${util:scheduleModuleEnabled(contest)}">
    <div id="timeline-upcoming-events">
        <h4>
            <t:glyphIcon icon="calendar" />
            <spring:message code="timeline.upcomingEvents" />
        </h4>
        <table class="table table-striped" style="margin-bottom: 5px;">
            <tbody>
            <c:forEach var="event" items="${upcomingEvents}">
                <tr>
                    <c:if test="${sitePreference.mobile}">
                        <td><a href="<spring:url value="/schedule/event/${event.code}" />">${event.name}</a></td>
                    </c:if>
                    <c:if test="${not sitePreference.mobile}">
                        <td><a href="<spring:url value="/${scheduleURL}#${event.code}" />">${event.name}</a></td>
                    </c:if>
                    <td class="text-right"><fmt:formatDate value="${event.localStartDate}" pattern="MMM d, HH:mm" /></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${empty upcomingEvents}">
            <p class="noSelected">
                <spring:message code="timeline.upcomingEvents.noResult" />
            </p>
        </c:if>
        <p class="gray-text">
            <a href="<spring:url value="/schedule" />"><i class="fa fa-arrow-right"></i> <spring:message code="timeline.upcomingEvents.fullSchedule" /></a>
        </p>
    </div>
</c:if>
