<c:forEach var="teamInfo" items="${teamInfos}" varStatus="status">
    <div class="col-sm-6 col-md-4 vertical-top">
        <t:panelWithHeading>
            <jsp:attribute name="heading"><c:out value="${teamInfo.contestTeamName}" /></jsp:attribute>
            <jsp:attribute name="footer">
                <div class="text-right">
                    <a href="<spring:url value="/team/${teamInfo.externalId}/presentation" />" class="btn btn-primary"><spring:message code="team.viewProfile" /></a>
                </div>
            </jsp:attribute>
            <jsp:attribute name="table">
                <table class="table">
                    <tbody>
                        <c:if test="${contest.contestSettings.showTeamNames}">
                            <tr>
                                <th><spring:message code="university" />: </th>
                                <td>${teamInfo.university.name}</td>
                            </tr>
                        </c:if>
                        <c:if test="${contest.contestSettings.showRegion}">
                            <tr>
                                <th><spring:message code="team.region" />: </th>
                                <td>${teamInfo.region.name}</td>
                            </tr>
                        </c:if>
                        <c:if test="${contest.contestSettings.showCountry}">
                            <tr>
                                <th><spring:message code="university.country" />: </th>
                                <td>${teamInfo.university.country}</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </jsp:attribute>
            <jsp:body>
                <img class="img-responsive" src="<spring:url value="/images/missing-image.jpg" />" alt="${teamInfo.contestTeamName}" />
            </jsp:body>
        </t:panelWithHeading>
    </div>
    <br class="${util:isNLine(status, 1, 2) ? 'clear hidden-md hidden-lg' : 'hidden'} " />
    <br class="${util:isNLine(status, 2, 3) ? 'clear hidden-xs hidden-sm' : 'hidden'} " />
</c:forEach>