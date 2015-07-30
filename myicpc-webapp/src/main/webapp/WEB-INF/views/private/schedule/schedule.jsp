<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdminWithSubmenu>
    <jsp:attribute name="title">
		<spring:message code="nav.admin.schedule" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="nav.admin.schedule" />
	</jsp:attribute>

    <jsp:attribute name="breadcrumb">
		  <li class="active"><spring:message code="nav.admin.schedule"/></li>
	</jsp:attribute>

    <jsp:attribute name="controls">
        <a href="<spring:url value="/private${contestURL}/schedule/event/create" />" class="btn btn-hover btn-default"><span class="glyphicon glyphicon-plus"></span> <spring:message code="scheduleAdmin.create" /></a>
        <a href="<spring:url value="/private${contestURL}/schedule/edit" />" class="btn btn-hover btn-default"><span class="glyphicon glyphicon-pencil"></span> <spring:message code="scheduleAdmin.editBulk" /></a>
        <button data-toggle="modal" data-target="#importSchedule" class="btn btn-hover btn-default"><t:glyphIcon icon="import"/> <spring:message code="scheduleAdmin.import" /></button>

		<t:modalWindow id="importSchedule">
            <jsp:attribute name="title"><spring:message code="scheduleAdmin.import" /></jsp:attribute>
            <jsp:body>
                <form method="post" action='<spring:url value="/private${contestURL}/schedule/import" />?${_csrf.parameterName}=${_csrf.token}' class="form-horizontal" enctype="multipart/form-data">

                    <div class="form-group">
                        <t:csvImport name="rolesCSV" label="Roles CSV file" />
                    </div>
                    <div class="form-group">
                        <t:csvImport name="daysCSV" label="Days CSV file" />
                    </div>
                    <div class="form-group">
                        <t:csvImport name="locationsCSV" label="Locations CSV file" />
                    </div>
                    <div class="form-group">
                        <t:csvImport name="eventsCSV" label="Events CSV file" />
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-9">
                            <t:button context="primary" type="submit">
                                <spring:message code="scheduleAdmin.import" />
                            </t:button>
                        </div>
                    </div>
                </form>
            </jsp:body>
        </t:modalWindow>
    </jsp:attribute>

    <jsp:attribute name="submenu">
	    <h3>
            <spring:message code="scheduleDays" />
        </h3>
        <c:if test="${not empty scheduleDays}">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th></th>
                    <th><spring:message code="scheduleDay.name" /></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="scheduleDay" items="${scheduleDays}">
                    <tr>
                        <td>${scheduleDay.dayOrder}</td>
                        <td>${scheduleDay.name}</td>
                        <td class="text-right">
                            <t:editButton url="/private${contestURL}/schedule/schedule-day/${scheduleDay.id}/edit" />
                            <t:deleteButton url="/private${contestURL}/schedule/schedule-day/${scheduleDay.id}/delete" confirmMessageCode="scheduleAdmin.deleteScheduleDay.confirm" confirmMessageArgument="${scheduleDay.name}" />
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty scheduleDays}">
            <p class="no-items-available-small">
                <spring:message code="scheduleDays.empty" />
            </p>
        </c:if>
		<a href="<spring:url value="/private${contestURL}/schedule/schedule-day/create" />" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> <spring:message code="scheduleAdmin.createScheduleDay" /></a>

		<h3>
            <spring:message code="locations" />
        </h3>
        <c:if test="${not empty locations}">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th><spring:message code="location.name" /></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="location" items="${locations}">
                    <tr>
                        <td>${location.name}</td>
                        <td class="text-right">
                            <t:editButton url="/private${contestURL}/schedule/location/${location.id}/edit" />
                            <t:deleteButton url="/private${contestURL}/schedule/location/${location.id}/delete" confirmMessageCode="scheduleAdmin.deleteLocation.confirm" confirmMessageArgument="${location.name}" />
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty locations}">
            <p class="no-items-available-small">
                <spring:message code="locations.empty" />
            </p>
        </c:if>
		<a href="<spring:url value="/private${contestURL}/schedule/location/create" />" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> <spring:message code="scheduleAdmin.createLocation" /></a>

		<h3>
            <spring:message code="eventRoles" />
        </h3>
        <c:if test="${not empty eventRoles}">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th><spring:message code="eventRole.name" /></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="eventRole" items="${eventRoles}">
                    <tr>
                        <td>${eventRole.name}</td>
                        <td class="text-right">
                            <t:editButton url="/private${contestURL}/schedule/event-role/${eventRole.id}/edit"></t:editButton>
                            <t:deleteButton url="/private${contestURL}/schedule/event-role/${eventRole.id}/delete" confirmMessageCode="scheduleAdmin.deleteEventRole.confirm" confirmMessageArgument="${eventRole.name}"></t:deleteButton>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty eventRoles}">
            <p class="no-items-available-small">
                <spring:message code="eventRoles.empty" />
            </p>
        </c:if>
		<a href="<spring:url value="/private${contestURL}/schedule/event-role/create" />" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> <spring:message code="scheduleAdmin.createEventRole" /></a>
	</jsp:attribute>

    <jsp:body>
        <c:if test="${not empty events}">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th class="nowrap"><spring:message code="event.name" /></th>
                    <th class="nowrap"><spring:message code="event.scheduleDay" /></th>
                    <th><spring:message code="event.location" /></th>
                    <th class="nowrap"><spring:message code="event.startTime" /></th>
                    <th class="nowrap"><spring:message code="event.endTime" /></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="event" items="${events}">
                    <tr>
                        <td>${event.name}</td>
                        <td><spring:message code="scheduleAdmin.scheduleDay.title" arguments="${event.scheduleDay.dayOrder}" /></td>
                        <td>${event.location.name}</td>
                        <td><fmt:formatDate type="both" dateStyle="medium" timeStyle="short" value="${event.localStartDate}" /></td>
                        <td><fmt:formatDate type="both" dateStyle="medium" timeStyle="short" value="${event.localEndDate}" /></td>
                        <td class="text-right nowrap">
                            <t:editButton url="/private${contestURL}/schedule/event/${event.id}/edit" />
                            <t:deleteButton url="/private${contestURL}/schedule/event/${event.id}/delete" confirmMessageCode="scheduleAdmin.deleteEvent.confirm" confirmMessageArgument="${event.name}" />
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty events}">
            <p class="no-items-available">
                <spring:message code="scheduleAdmin.noResult" />
            </p>
        </c:if>

        <a href="<spring:url value="/private${contestURL}/schedule/event/create" />" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> <spring:message code="scheduleAdmin.create" /></a>
    </jsp:body>
</t:templateAdminWithSubmenu>