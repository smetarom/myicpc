<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div class="col-sm-12">
	<h3>
		<spring:message code="teamAdmin.sync.auto" />
	</h3>

	<table class="table">
		<tbody>
			<tr>
				<td>
					<t:plainForm action="/private${contestURL}/teams/synchronize">
						<t:button type="submit" context="primary"><spring:message code="teamAdmin.sync.btn" /></t:button>
						<t:helpIcon helpTextCode="teamAdmin.sync.hint" placement="bottom" />
					</t:plainForm>
				</td>
				<td>
					<t:plainForm action="/private${contestURL}/teams/synchronize/staff-members">
						<t:button type="submit" context="primary"><spring:message code="teamAdmin.sync.staff.btn" /></t:button>
						<t:helpIcon helpTextCode="teamAdmin.sync.staff.hint" placement="bottom" />
					</t:plainForm>
				</td>
				<td>
					<t:plainForm action="/private${contestURL}/teams/synchronize/social-content">
						<t:button type="submit" context="primary"><spring:message code="teamAdmin.sync.socialInfo.btn" /></t:button>
						<t:helpIcon helpTextCode="teamAdmin.sync.socialInfo.hint" placement="bottom" />
					</t:plainForm>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<br />
<br />

<t:plainForm action="/private${contestURL}/teams/synchronize-manual" styleClass="col-sm-12" fileUpload="true">
	<fieldset>
		<legend>
			<spring:message code="teamAdmin.sync.manual" />
		</legend>
		<p><spring:message code="teamAdmin.sync.manual.hint" /></p>
		<div class="form-group">
			<label for="universityJSON" class="col-sm-3 control-label"><spring:message code="teamAdmin.sync.manual.university" />:* </label>
			<div class="col-sm-9">
				<input type="file" class="form-control" name="universityJSON" id="universityJSON" accept=".json" required="required">
			</div>
		</div>
		<div class="form-group">
			<label for="teamJSON" class="col-sm-3 control-label"><spring:message code="teamAdmin.sync.manual.team" />:* </label>
			<div class="col-sm-9">
				<input type="file" class="form-control" name="teamJSON" id="teamJSON" accept=".json" required="required">
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-3 col-sm-9">
				<button type="submit" class="btn btn-primary">
					<spring:message code="teamAdmin.sync.manual.btn" />
				</button>
			</div>
		</div>
	</fieldset>
</t:plainForm>

<form method="post" action='<spring:url value="/private${contestURL}/teams/contestteamidmapping" />' class="form-horizontal col-sm-10" enctype="multipart/form-data">
	<fieldset>
		<legend>
			<spring:message code="teamAdmin.teamContestIds" />
		</legend>
		<div class="form-group">
			<label for="teamContestJSON" class="col-sm-3 control-label"><spring:message code="teamAdmin.teamContestIds.file" />:* </label>
			<div class="col-sm-9">
				<input type="file" class="form-control" name="teamContestJSON" id="teamContestJSON" accept=".json" required="required">
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-3 col-sm-9">
				<button type="submit" class="btn btn-primary">
					<spring:message code="teamAdmin.teamContestIds.btn" />
				</button>
			</div>
		</div>
	</fieldset>
</form>
