<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<table class="table">
	<thead>
		<tr>
			<th><spring:message code="team" /></th>
			<th><spring:message code="university" /></th>
			<th><spring:message code="teamAdmin.abbreviation" /></th>
			<th><spring:message code="teamAdmin.hashtag" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="teamInfo" items="${teamInfos}">
			<tr>
				<td>${teamInfo.name}</td>
				<td>${teamInfo.university.name}</td>
				<td>${teamInfo.abbreviation}</td>
				<td>#${teamInfo.hashtag}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>