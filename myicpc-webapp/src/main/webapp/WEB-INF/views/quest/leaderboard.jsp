<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
        <script src="<c:url value='/js/myicpc/controllers/questLeaderboard.js'/>" defer></script>
    </jsp:attribute>
    <jsp:attribute name="headline">
        <spring:message code="quest.leaderboard" />
    </jsp:attribute>
    <jsp:attribute name="title">
        <spring:message code="quest.leaderboard" />
    </jsp:attribute>

    <jsp:body>
        <%@ include file="/WEB-INF/views/quest/fragment/questInfo.jsp" %>

        <c:if test="${not empty leaderboards}">
            <t:secondLevelSubmenu isMobile="${sitePreference.mobile}">
                <c:forEach var="leaderboard" items="${leaderboards}" varStatus="status">
                    <t:menuItem activeItem="${leaderboard.urlCode}" active="${activeTab}" url="${contestURL}/team/${team.externalId}">${leaderboard.name}</t:menuItem>
                </c:forEach>
            </t:secondLevelSubmenu>

            <div id="mainLeaderboard" class="table-responsive" ng-app="quest" ng-controller="leaderboardCtrl">
                <table class="table striped-rows" ng-cloak>
                    <thead>
                        <tr>
                            <th><spring:message code="quest.leaderboard.rank" /></th>
                            <th style="min-width: 200px;"><spring:message code="quest.leaderboard.participant" /></th>
                            <th class="text-center"><spring:message code="quest.leaderboard.points" /></th>
                            <th class="text-center"><spring:message code="quest.leaderboard.numSolved" /></th>
                            <th ng-repeat="challenge in challenges | orderBy:'name'">
                                <div class="vertical-text">
                                    <a href="<spring:url value="${contestURL}/quest/challenges#" />{{challenge.id}}">{{challenge.name}}</a>
                                </div>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="participant in participants | orderBy:[sortByPoints,'name']">
                        <td>{{$index + 1}}</td>
                        <td><a href="<spring:url value="/person/" />{{participant.id}}">{{participant.name}}</a></td>
                        <td class="text-center">{{participant.points}}</td>
                        <td class="text-center">{{participant.solved}}</td>
                        <td class="text-center" ng-repeat="challenge in challenges | orderBy:'name'">
                            <i ng-if="participant[challenge.id].state === 'ACCEPTED'" id="submission_{{participant.id}}_{{challenge.id}}" class="fa fa-check leaderboard-submission" style="color: green;" rel="tooltip"
                               data-toggle="tooltip" title="<spring:message code="quest.leaderboard.accepted" />"
                                    ></i>
                            <i ng-if="participant[challenge.id].state === 'PENDING'" id="submission_{{participant.id}}_{{challenge.id}}" class="fa fa-clock-o leaderboard-submission" style="color: orange;" rel="tooltip"
                               data-toggle="tooltip" title="<spring:message code="quest.leaderboard.pending" />"
                                    ></i>
                            <i ng-if="participant[challenge.id].state === 'REJECTED'" id="submission_{{participant.id}}_{{challenge.id}}" class="fa fa-ban leaderboard-submission" style="color: red;" rel="tooltip"
                               data-toggle="tooltip" title="<spring:message code="questAdmin.submissions.reject.reason" />: {{participant[challenge.id].reason}}"
                                    ></i>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <script type="application/javascript">
                $(function () {
                    var ngController = angular.element($("#mainLeaderboard")).scope();
                    var challengesJSON = ${empty challengesJSON ? '[]' : challengesJSON};
                    var participantsJSON = ${empty participantsJSON ? '[]' : participantsJSON};
                    ngController.init(challengesJSON, participantsJSON);

                });
            </script>


        </c:if>

        <c:if test="${empty leaderboards}">
            <div class="no-items-available">
                <spring:message code="quest.leaderboard.noResult" />
            </div>
        </c:if>

    </jsp:body>
</t:template>
