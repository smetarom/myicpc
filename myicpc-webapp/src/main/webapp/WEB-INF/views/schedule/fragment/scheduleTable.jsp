<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

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
                <td>
                    <c:if test="${param.isMobile eq 'true'}">
                        <a href="<spring:url value="${contestURL}/schedule/event/${event.code}" />">${event.name}</a>
                    </c:if>
                    <c:if test="${param.isMobile ne 'true'}">
                        <a href="#${event.code}" onclick="loadEventContent(${event.id})" class="display-block">${event.name}</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:forEach>