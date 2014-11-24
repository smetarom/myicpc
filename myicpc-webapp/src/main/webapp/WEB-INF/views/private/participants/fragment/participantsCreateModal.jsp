<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags"%>

<t:form action="/private${contestURL}/participants/create" entity="newParticipant">
	<t:modalWindow id="createNewPerson">
		<jsp:attribute name="title"><spring:message code="participantAdmin.create" /></jsp:attribute>
		<jsp:attribute name="footer">
			<t:button styleClass="btn-hover" dataDismiss="true"><spring:message code="close" /></t:button>
			<t:button type="submit" context="primary"><spring:message code="save" /></t:button>
		</jsp:attribute>
		<jsp:body>
			<t:springInput labelCode="person.firstname" path="firstname" required="true" />
			<t:springInput labelCode="person.lastname" path="lastname" required="true" />
			<div class="form-group">
				<label for="participantRole" class="col-sm-3 control-label">
					<spring:message code="person.role" />:* </label>
				<div class="col-sm-9">
					<select name="participantRole" id="participantRole" class="form-control">
						<c:forEach items="${participantRoles}" var="participantRole">
							<option value="${participantRole}"><spring:message code="${participantRole.code}" text="${participantRole.label}" /></option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group" id="teamSelect">
				<label for="teamInfoId" class="col-sm-3 control-label">
					<spring:message code="person.team" />:* </label>
				<div class="col-sm-9">
					<select name="teamInfoId" id="teamInfoId" class="form-control">
						<c:forEach items="${teams}" var="team">
							<option value="${team.id}">${team.contestTeamName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<t:springInput labelCode="person.twitterUsername" path="twitterUsername"></t:springInput>
			<t:springInput labelCode="person.vineUsername" path="vineUsername"></t:springInput>
			<t:springInput labelCode="person.instagramUsername" path="instagramUsername"></t:springInput>
			<t:springInput labelCode="person.externalId" path="externalId" type="number"></t:springInput>
		</jsp:body>
	</t:modalWindow>
</t:form>

<script type="text/javascript">
	$(function () {
		$("#participantRole").change(function() {
			if ($(this).val() == 'STAFF') {
				$('#teamSelect').hide();
			} else {
				$('#teamSelect').show();
			}
		});
	});
</script>
