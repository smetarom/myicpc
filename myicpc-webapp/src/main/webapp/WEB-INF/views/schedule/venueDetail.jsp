<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h2>
	<spring:message code="venue" />
	: ${venue.name}
</h2>

<div class="row">
	<div class="col-md-6">
		<h3>
			<spring:message code="venue.eventsAt" arguments="${venue.name}" />
		</h3>
		<c:if test="${empty schedule}">
			<spring:message code="venue.eventsHere.empty" />
		</c:if>
		<c:forEach var="day" items="${schedule}">
			<table class="table table-striped">
				<thead>
					<tr>
						<th colspan="2"><fmt:formatDate value="${day.date}" pattern="EEEE MMMM dd" /> - ${day.name}</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="event" items="${day.eventsChronologically}">
						<tr>
							<td style="width: 115px"><fmt:formatDate pattern="HH:mm" value="${event.localStartDate}" /> - <fmt:formatDate pattern="HH:mm" value="${event.localEndDate}" /></td>
							<td><a href="<spring:url value="${contestURL}/schedule#${event.code}" />">${event.name}</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:forEach>
	</div>
	<div class="col-md-6">
		<c:if test="${not empty venue.googleMapUrl}">
			<h3>
				<spring:message code="venue.onMap" />
			</h3>
			<iframe width="100%" height="350" class="google-map-frame" src="${venue.googleMapUrl}"></iframe>
		</c:if>
	</div>
</div>

<br class="clear" />
