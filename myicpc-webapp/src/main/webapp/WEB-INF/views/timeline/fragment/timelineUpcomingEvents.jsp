<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<c:if test="${not empty upcomingEvents}">
    <h4>
        <t:glyphIcon icon="calendar" />
        <spring:message code="timeline.upcomingEvents" />
    </h4>
    <div style="height: 200px; overflow: auto">
        <table class="table table-striped" style="margin-bottom: 5px;">
            <tbody>
            <c:forEach var="event" items="${upcomingEvents}">
                <tr>
                    <c:if test="${sitePreference.mobile}">
                        <td><a href="<spring:url value="${contestURL}/schedule/event/${event.code}" />">${event.name}</a></td>
                    </c:if>
                    <c:if test="${not sitePreference.mobile}">
                        <td><a href="<spring:url value="${contestURL}/schedule/#${event.code}" />">${event.name}</a></td>
                    </c:if>
                    <td class="text-right nowrap"><fmt:formatDate value="${event.localStartDate}" pattern="MMM d, HH:mm" /></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <hr/>
</c:if>