<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="util" uri="http://myicpc.baylor.edu/functions"%>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

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
			<c:if test="${not empty event.location.googleMapUrl}">
				<iframe width="100%" height="350" src="${event.location.googleMapUrl}" class="google-map-frame"></iframe>
			</c:if>
		</c:if>
	</div>
	<div class="col-md-6">
		<h3>
			<spring:message code="schedule.posts" />
		</h3>

		<t:hashtagPanel id="eventDetail" contestURL="${contestURL}" hashtag1="${event.hashtag}" hashtag2="${contest.hashtag}" />

		<div id="eventPhotoGallery" ng-controller="eventGalleryCtrl" ng-init="init('${event.fullPicasaTag}')" ng-cloak>
            <h3 ng-if="photos.length"><spring:message code="officialGallery" /></h3>
            <div ng-if="photos.length">
                <t:button href="${contestURL}/gallery/official#${event.fullPicasaTag}" context="info" styleClass="btn-block"><spring:message code="officialGallery.viewAll" /></t:button>
            </div>
			<div ng-repeat="photo in photos" class="thumbnail">
                <img ng-src="{{photo.imageUrl}}" alt="" style="max-height: 350px" />
            </div>
		</div>
	</div>
</div>

<br class="clear" />
