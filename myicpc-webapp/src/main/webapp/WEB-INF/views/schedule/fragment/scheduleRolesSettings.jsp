<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<p>
    <spring:message code="schedule.roles.hint" />
</p>

<form class="clearfix" action="<spring:url value="${contestURL}/schedule/updateScheduleRole" />" method="post">
    <c:forEach var="role" items="${roles}">
        <div class="checkbox">
            <label> <input type="checkbox" name="scheduleRoles[]" value="${role.id}" ${empty activeRoles[role.id] ? '' : 'checked="checked"'}> ${role.name}
            </label>
        </div>
    </c:forEach>
    <hr class="divider" />
    <div class="pull-right">
        <button type="button" class="btn btn-default" data-dismiss="modal">
            <spring:message code="close" />
        </button>
        <button type="submit" class="btn btn-primary">
            <spring:message code="save" />
        </button>
    </div>
</form>