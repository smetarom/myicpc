<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<p><spring:message code="installAdmin.wizard.summary.hint" arguments="${adminUser.username}"/></p>

<div class="form-group text-right">
    <a href="<spring:url value="/private/install"/>" class="btn btn-default"><spring:message code="no"/></a>
    <button type="submit" class="btn btn-primary">
        <spring:message code="yes"/>
    </button>
</div>