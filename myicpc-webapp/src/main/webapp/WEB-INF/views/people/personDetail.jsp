<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="title">
        ${contestParticipant.fullname}
    </jsp:attribute>
    <jsp:attribute name="headline">
        ${contestParticipant.fullname}
        <small>
            <c:forEach items="${associations}" var="association">
                <spring:message code="${association.contestParticipantRole.code}" text="${association.contestParticipantRole.label}" />
            </c:forEach>
        </small>
    </jsp:attribute>

    <jsp:body>
        <div class="col-sm-6">
            <h3><spring:message code="person.attendedContests" /></h3>
            <c:if test="${not empty attendedContests}">
                <table class="table">
                    <thead>
                        <tr>
                            <th><spring:message code="contest.year" /></th>
                            <th><spring:message code="contest" /></th>
                            <th><spring:message code="contest.role" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${attendedContests}" var="attendedContest">
                            <tr>
                                <td>${attendedContest.year}</td>
                                <td>${attendedContest.name}</td>
                                <td><spring:message code="${attendedContest.contestParticipantRole.code}" text="${attendedContest.contestParticipantRole.label}" /></td>
                            </tr>

                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty attendedContests}">
                <p class="no-items-available">
                    <spring:message code="noDataAvailable" />
                </p>
            </c:if>
        </div>

        <div class="col-sm-6">
            <h3><spring:message code="person.quest" /></h3>
            <c:if test="${not empty questSubmissions}">
                <c:set var="submissions" value="${questSubmissions}" />
                <c:set var="showSubmissions" value="true" />
                <%@ include file="/WEB-INF/views/quest/fragment/challengeSubmissionList.jsp" %>
            </c:if>
            <c:if test="${empty questSubmissions}">
                <p class="no-items-available">
                    <spring:message code="person.quest.noResult" arguments="${contestParticipant.fullname}" />
                    <a href="<spring:url value="${contestURL}/quest" />"><spring:message code="nav.quest" /></a>
                </p>
            </c:if>
        </div>

    </jsp:body>

</t:template>
