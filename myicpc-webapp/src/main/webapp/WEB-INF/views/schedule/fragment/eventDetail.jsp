<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="util" uri="http://myicpc.baylor.edu/functions"%>

<c:if test="${not hideTitle}">
	<h2>${event.name}</h2>
</c:if>
<div class="row">
	<div class="col-md-6">
		<br />
		<p>
			<strong><spring:message code="schedule.time" />:</strong>
			<fmt:formatDate type="both" dateStyle="medium" timeStyle="short" value="${event.localStartDate}" />
			-
			<fmt:formatDate type="both" dateStyle="medium" timeStyle="short" value="${event.localEndDate}" />
		</p>
		<c:if test="${not empty event.rolesPrint}">
			<p>
				<strong><spring:message code="schedule.attendees" />:</strong> ${event.rolesPrint}
			</p>
		</c:if>
		<c:if test="${not empty event.thingsToBring}">
			<p>
				<strong><spring:message code="schedule.thingsToBring" /></strong>: ${event.thingsToBring}
			</p>
		</c:if>
		<c:if test="${not empty event.description}">
			<p>
				<strong><spring:message code="schedule.description" /></strong>:<br /> ${util:parseWikiSyntax(event.description)}
			</p>
		</c:if>

		<h3>
			<spring:message code="twitter.for" />
			${hashTags}
		</h3>

        <%--TODO--%>
		<%--<%@ include file="/WEB-INF/views/fragment/social/tweets.jsp"%>--%>


	</div>
	<div class="col-md-6">
		<c:if test="${not empty event.location}">
			<h3>
				<spring:message code="venue" />: ${event.location.name}
			</h3>
            <c:if test="${sitePreference.mobile}">
                <a href="<spring:url value="${contestURL}/venue/${event.location.code}" />" class="btn btn-info btn-block" style="margin: 10px 0">
                    <t:glyphIcon icon="map-marker" /> <spring:message code="venue.detail"/>
                </a>
            </c:if>
            <c:if test="${not sitePreference.mobile}">
                <a href="<spring:url value="${contestURL}/venues#${event.location.code}" />" class="btn btn-info btn-block" style="margin: 10px 0">
                    <t:glyphIcon icon="map-marker" /> <spring:message code="venue.detail"/>
                </a>
            </c:if>
			<iframe width="100%" height="350" src="${event.location.googleMapUrl}" class="google-map-frame"></iframe>
		</c:if>
        <%--TODO--%>
		<%--<jsp:include page="/WEB-INF/views/fragment/team/photos.jsp">--%>
			<%--<jsp:param name="picasaUrl"--%>
				<%--value="https://picasaweb.google.com/data/feed/api/user/hq.icpc?kind=photo&q=Album${contestYear}%20${event.fullPicasaTag}&start-index=1&max-results=5&imgmax=512&access=public&alt=json"--%>
			<%--/>--%>
			<%--<jsp:param name="picasaTag" value="${event.picasaTag}" />--%>
			<%--<jsp:param name="picasaKind" value="event" />--%>
			<%--<jsp:param name="picasaUsername" value="${picasaUsername}" />--%>
		<%--</jsp:include>--%>
	</div>
</div>

<br class="clear" />
