<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<form action="<spring:url value="/private${contestURL}/teams/abbreviation" />" class="form-inline" enctype="multipart/form-data" method="post">
	<div class="form-group">
		<label for="file"><spring:message code="teamAdmin.abbr.file" />: </label>
	</div>
	<div class="form-group">
		<input type="file" class="form-control" id="file" name="file">
	</div>
	<button type="submit" class="btn btn-primary">
		<spring:message code="upload" />
	</button>
</form>