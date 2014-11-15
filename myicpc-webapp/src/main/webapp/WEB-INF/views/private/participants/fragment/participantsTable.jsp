<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags"%>

<form action="<spring:url value="/private/participants/update" />" method="post">
 	<br />
 	<div class="well well-sm clearfix">
 		<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createNewPerson">
			<span class="glyphicon glyphicon-plus"></span> <spring:message code="participantAdmin.create" />
		</button>
  		<button type="submit" class="btn btn-primary pull-right">
			<spring:message code="saveAll" />
		</button>
 	</div>
	<table class="table table-striped">
		<thead>
			<tr>
				<th><spring:message code="teammember.name" /></th>
				<th></th>
				<th><spring:message code="teammember.twitter" /> (w/o @)</th>
				<th><spring:message code="teammember.vine" /></th>
				<th><spring:message code="teammember.instagram" /></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="teamMember" items="${teamMembers}">
				<tr>
					<td>${teamMember.officialFullname}</td>
					<td><c:forEach var="association" items="${teamMember.associations}">
							<spring:message code="${association.teamMemberRole.code}" /> ${not empty association.teamInfo ? ' - ' : ''} ${association.teamInfo.contestTeamName}<br />
						</c:forEach></td>
					<td width="300"><input type="text" name="twitter_${teamMember.id}" value="${teamMember.twitterUsername}" class="form-control" /></td>
					<td width="300"><input type="text" name="vine_${teamMember.id}" value="${teamMember.vineUsername}" class="form-control" /></td>
					<td width="300"><input type="text" name="instagram_${teamMember.id}" value="${teamMember.instagramUsername}" class="form-control" /></td>
					<td><t:deleteButton url="/private/participants/${teamMember.id}/delete" confirmMessageCode="participantAdmin.delete.confirm" confirmMessageArgument="${teamMember.fullname}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<button type="submit" class="btn btn-primary pull-right">
		<spring:message code="saveAll" />
	</button>
</form>