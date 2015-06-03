<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>

  <jsp:attribute name="title">
      ${teamInfo.contestTeamName}
  </jsp:attribute>
  
  <jsp:attribute name="headline">
      <span id="team_${team.id}_rank" class="label label-info">${team.rank }</span> ${teamInfo.contestTeamName}
  </jsp:attribute>

  <jsp:body>
      <c:set var="teamContestId" value="${team.externalId}" />
      <c:set var="teamPresentationId" value="${teamInfo.externalId}" />
      <%@ include file="/WEB-INF/views/scoreboard/fragment/teamHomeMenu.jsp"%>

      <div class="clearfix">
          <div class="col-sm-6 col-md-4">
              <h3>
                  <spring:message code="team.members" />
              </h3>

              <div id="carousel-team-members" class="carousel slide" data-ride="carousel">
                  <!-- Wrapper for slides -->
                  <div class="carousel-inner" role="listbox">
                      <c:forEach var="contestParticipant" items="${peopleInCarousel}" varStatus="status">
                          <div class="item ${status.first ? 'active' : ''}">
                              <c:if test="${not empty contestParticipant.profilePictureUrl}">
                                  <img src="<spring:url value="${contestParticipant.profilePictureUrl}" />" alt="${teammember.fullname}" class="center-block" width="150">
                              </c:if>
                              <c:if test="${empty contestParticipant.profilePictureUrl}">
                                  <img src="<spring:url value="/images/default-profile-small.png" />" alt="${contestParticipant.fullname}" class="center-block" width="150">
                              </c:if>
                              <div style="height: 120px;"> </div>
                              <div class="carousel-caption">
                                  <h3 style="white-space:nowrap;">${contestParticipant.fullname}</h3>
                                  <p>
                                      <c:forEach var="_association" items="${contestParticipant.teamAssociations}">
                                          <spring:message code="${_association.contestParticipantRole.code}" text="${_association.contestParticipantRole.label}"/><br/>
                                      </c:forEach>
                                  </p>
                                  <p>
                                      <a href="<spring:url value="/person/${contestParticipant.id}" />"><spring:message code="person.viewProfile" /></a>
                                  </p>
                              </div>
                          </div>
                      </c:forEach>
                  </div>

                  <!-- Controls -->
                  <a class="left carousel-control" href="#carousel-team-members" role="button" data-slide="prev">
                      <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                      <span class="sr-only">Previous</span>
                  </a>
                  <a class="right carousel-control" href="#carousel-team-members" role="button" data-slide="next">
                      <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                      <span class="sr-only">Next</span>
                  </a>
              </div>

              <br class="clear"/>

              <c:set var="people" value="${coaches}" scope="request" />
              <jsp:include page="/WEB-INF/views/scoreboard/fragment/teamTable.jsp">
                  <jsp:param name="titleCode" value="team.coach" />
              </jsp:include>

              <c:set var="people" value="${contestants}" scope="request" />
              <jsp:include page="/WEB-INF/views/scoreboard/fragment/teamTable.jsp">
                  <jsp:param name="titleCode" value="team.contestants" />
              </jsp:include>

              <c:set var="people" value="${reserves}" scope="request" />
              <jsp:include page="/WEB-INF/views/scoreboard/fragment/teamTable.jsp">
                  <jsp:param name="titleCode" value="team.reserves" />
              </jsp:include>

              <c:set var="people" value="${attendees}" scope="request" />
              <jsp:include page="/WEB-INF/views/scoreboard/fragment/teamTable.jsp">
                  <jsp:param name="titleCode" value="team.attendees" />
              </jsp:include>
          </div>

          <div class="col-sm-6 col-md-4">
              <h3>
                  <spring:message code="team.info" />
              </h3>

              <table>
                  <tbody>
                  <tr>
                      <th style="width: 100px"><spring:message code="team.name" />:</th>
                      <td>${teamInfo.name }</td>
                  </tr>
                  <tr>
                      <th><spring:message code="team.region" />:</th>
                      <%--TODO add region--%>
                      <%--<td><a id="teamRegionLink" href="<spring:url value="${contestURL}/region/${team.region.id}"/>">${team.region.shortName}</a></td>--%>
                  </tr>
                  </tbody>
              </table>

              <h3>
                  <spring:message code="university" />
              </h3>

              <table>
                  <tbody>
                  <tr>
                      <th style="width: 100px"><spring:message code="university.name" />:</th>
                      <td>${teamInfo.university.name }</td>
                  </tr>
                  <c:if test="${not empty teamInfo.university.country && contest.contestSettings.showCountry}">
                      <tr>
                          <th><spring:message code="university.country" />:</th>
                          <td>${teamInfo.university.country}</td>
                      </tr>
                  </c:if>
                  <c:if test="${not empty teamInfo.university.homepageURL}">
                      <tr>
                          <th><spring:message code="university.homepage" />:</th>
                          <td><a href="${util:correctURL(teamInfo.university.homepageURL)}" target="_blank">${teamInfo.university.homepageURL }</a></td>
                      </tr>
                  </c:if>
                  </tbody>
              </table>
              <img src="${util:universityLogoUrl(teamInfo.university.externalId)}" alt="${teamInfo.university.name}" class="img-responsive center-block" onerror='this.style.display = "none"'>
          </div>

          <div class="col-sm-6 col-md-4">
              <h3>
                  <spring:message code="team.regionalResults" />
              </h3>
              <c:forEach var="result" items="${teamInfo.regionalResults}">
                  <div class="panel panel-default">
                      <div class="panel-heading">
                          <h5 class="panel-title">${result.contestName}</h5>
                      </div>
                      <div class="panel-body row">
                          <table style="margin-left: 10px;">
                              <tr>
                                  <th style="width: 180px;"><spring:message code="team.name" />:</th>
                                  <td>${result.teamName}</td>
                              </tr>
                              <tr>
                                  <th><spring:message code="team.rank" />:</th>
                                  <td>${result.rank}</td>
                              </tr>
                              <tr>
                                  <th><spring:message code="team.problemsSolved" />:</th>
                                  <td>${result.problemSolved}</td>
                              </tr>
                              <tr>
                                  <th><spring:message code="team.totalTime" />:</th>
                                  <td>${result.totalTime}</td>
                              </tr>
                          </table>
                      </div>
                  </div>
              </c:forEach>
              <c:if test="${empty teamInfo.regionalResults}">
                  <div class="no-items-available">
                      <spring:message code="team.regionalResults.noResult" />
                  </div>
              </c:if>
          </div>
      </div>

  </jsp:body>
</t:template>
