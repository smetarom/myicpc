<div class="col-sm-12">
    <table class="table table-striped">
        <thead>
            <th><spring:message code="team.name" /></th>
            <c:if test="${contest.contestSettings.showTeamNames}">
                <th class="hidden-xs"><spring:message code="university" /></th>
            </c:if>
            <c:if test="${contest.contestSettings.showRegion}">
                <th class="hidden-xs"><spring:message code="team.region" /></th>
            </c:if>
            <c:if test="${contest.contestSettings.showCountry}">
                <th class="hidden-xs"><spring:message code="university.country" /></th>
            </c:if>
        </thead>
        <tbody>
            <c:forEach var="teamInfo" items="${teamInfos}">
                <tr>
                    <td>
                        <a href="<spring:url value="/team/${teamInfo.externalId}" />">
                            <c:out value="${teamInfo.contestTeamName}" />
                        </a>
                    </td>
                    <c:if test="${contest.contestSettings.showTeamNames}">
                        <td class="hidden-xs"><c:out value="${teamInfo.university.name}" /></td>
                    </c:if>
                    <c:if test="${contest.contestSettings.showCountry}">
                        <td class="hidden-xs"><c:out value="${teamInfo.university.country}" /></td>
                    </c:if>
                </tr>
            </c:forEach>

        </tbody>
    </table>
</div>

