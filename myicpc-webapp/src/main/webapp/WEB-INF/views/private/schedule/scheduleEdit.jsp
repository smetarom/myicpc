<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="scheduleAdmin.editBulk" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="scheduleAdmin.editBulk" />
	</jsp:attribute>

    <jsp:attribute name="breadcrumb">
		  <li><a href="<spring:url value="/private${contestURL}/schedule" />"><spring:message code="nav.admin.schedule" /></a></li>
		  <li class="active"><spring:message code="scheduleAdmin.editBulk" /></li>
	</jsp:attribute>

    <jsp:body>
        <c:if test="${not empty eventsWrapper.events}">
            <form:form modelAttribute="eventsWrapper">
                <div class="text-right">
                    <t:button type="submit" context="primary"><spring:message code="saveAll" /></t:button>
                </div>
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
                    <c:forEach var="event" items="${eventsWrapper.events}" varStatus="status">
                        <tr>
                            <td><form:input path="events[${status.index}].name" /></td>
                            <td>
                                <form:select path="events[${status.index}].scheduleDay">
                                    <form:options items="${scheduleDays}" itemValue="id" itemLabel="dayLabel" />
                                </form:select>
                            </td>
                            <td>
                                <form:select path="events[${status.index}].location">
                                    <form:option value="${null}"></form:option>
                                    <form:options items="${locations}" itemValue="id" itemLabel="name" />
                                </form:select>
                            </td>
                            <td><form:input path="events[${status.index}].localStartDate" cssClass="startDate" /></td>
                            <td><form:input path="events[${status.index}].localEndDate" cssClass="endDate" /></td>
                            <td>
                                <form:select path="events[${status.index}].roles" items="${eventRoles}"
                                             multiple="true" itemValue="id" itemLabel="name">
                                </form:select>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="text-right">
                    <t:button type="submit" context="primary"><spring:message code="saveAll" /></t:button>
                </div>
            </form:form>
            <script type="text/javascript">
                $(function() {
                    $('.startDate').datetimepicker(datePickerOptions);

                    $('.endDate').datetimepicker(datePickerOptions);
                });
            </script>
        </c:if>
        <c:if test="${empty eventsWrapper.events}">
            <p class="no-items-available">
                <spring:message code="scheduleAdmin.noResult" />
            </p>
        </c:if>
    </jsp:body>
</t:templateAdmin>