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
    <jsp:attribute name="javascript">
        <script type="application/javascript">
            $(function() {
                var tableWidth = $('#leaderboard-not-fixed table.fixed-offset').outerWidth(true);
                $('#leaderboard-not-fixed').width(tableWidth + 20);

                $leaderboardFixed = $("#leaderboard-fixed");;
                $leaderboardNotFixed = $("#leaderboard-not-fixed");
                $leaderboardFixed.scroll(function () {
                    $leaderboardNotFixed.scrollTop($leaderboardFixed.scrollTop());
                });
                $leaderboardNotFixed.scroll(function () {
                    $leaderboardFixed.scrollTop($leaderboardNotFixed.scrollTop());
                });
                var top = $("#mainLeaderboard").offset().top;
                var bottom = $("#footer").offset().top;
                var header = $(".leaderboard-header").outerHeight(true);
                var tableHeight = bottom - top - header;
                if (tableHeight < 250) {
                    tableHeight = 250;
                }
                $leaderboardFixed.height(tableHeight);
                $leaderboardNotFixed.height(tableHeight);
            })
        </script>
    </jsp:attribute>

    <jsp:body>
        <%@ include file="/WEB-INF/views/quest/fragment/questInfo.jsp" %>

        <c:if test="${not empty leaderboards}">
            <c:if test="${leaderboards.size() > 1}">
                <t:secondLevelSubmenu isMobile="${sitePreference.mobile}">
                    <c:forEach var="leaderboard" items="${leaderboards}" varStatus="status">
                        <t:menuItem activeItem="${leaderboard.urlCode}" active="${activeTab}" url="${contestURL}/quest/leaderboard/${leaderboard.urlCode}">${leaderboard.name}</t:menuItem>
                    </c:forEach>
                </t:secondLevelSubmenu>
            </c:if>

            <div id="mainLeaderboard" style="width: 100%; overflow-y: auto">
                <div class="leaderboard-header">
                    <div class="fixed-columns">
                        <table class="table">
                            <thead>
                            <tr class="leaderboard-header-row">
                                <th class="col-rank"><spring:message code="quest.leaderboard.rank" /></th>
                                <th class="col-participant-name"><spring:message code="quest.leaderboard.participant" /></th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <table class="fixed-offset table">
                        <thead>
                        <tr class="leaderboard-header-row">
                            <th class="col-role"><spring:message code="quest.leaderboard.role" /></th>
                            <th class="col-points"><spring:message code="quest.leaderboard.points" /></th>
                            <th class="col-points"><spring:message code="quest.leaderboard.numSolved" /></th>
                            <c:forEach var="challenge" items="${challenges}">
                                <th class="col-quest-label text-center">
                                    <div class="vertical-text">
                                        <a href="<spring:url value="${contestURL}/quest/challenges#" />${challenge.hashtag}">${challenge.hashtagSuffix}</a>
                                    </div>
                                </th>
                            </c:forEach>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div id="leaderboard-fixed" class="fixed-columns" style="height: 250px; overflow-x: hidden; overflow-y: hidden">
                    <table class="table table-striped table-condensed">
                        <tbody>
                        <c:forEach var="questParticipant" items="${participants}" varStatus="status">
                            <tr style="position: relative">
                                <td class="col-rank">${status.index + 1}</td>
                                <td class="col-participant-name"><a href="<spring:url value="${contestURL}/person/${questParticipant.contestParticipant.id}" />">${questParticipant.contestParticipant.fullname}</a></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div id="leaderboard-not-fixed" style="height: 250px; overflow-y: auto; overflow-x: visible">
                    <table class="fixed-offset table table-striped table-condensed" style="width: auto">
                        <tbody>
                            <c:forEach var="questParticipant" items="${participants}" varStatus="status">
                                <tr style="position: relative">
                                    <td class="col-role">as</td>
                                    <td class="col-points">${questParticipant.totalPoints}</td>
                                    <td class="col-points">${questParticipant.acceptedSubmissions}</td>
                                    <c:forEach var="challenge" items="${challenges}">
                                        <td class="col-quest-label text-center">
                                            <t:questTick state="${questParticipant.submissionMap[challenge.id].submissionState}" note="${questParticipant.submissionMap[challenge.id].reasonToReject}" />
                                        </td>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <%--<div id="mainLeaderboard" class="table-responsive" ng-app="quest" ng-controller="leaderboardCtrl">--%>
                <%--<table class="table striped-rows" ng-cloak>--%>
                    <%--<thead>--%>
                        <%--<tr>--%>
                            <%--<th><spring:message code="quest.leaderboard.rank" /></th>--%>
                            <%--<th style="min-width: 200px;"><spring:message code="quest.leaderboard.participant" /></th>--%>
                            <%--<th class="text-center"><spring:message code="quest.leaderboard.points" /></th>--%>
                            <%--<th class="text-center"><spring:message code="quest.leaderboard.numSolved" /></th>--%>
                            <%--<th ng-repeat="challenge in challenges | orderBy:'name'">--%>
                                <%--<div class="vertical-text">--%>
                                    <%--<a href="<spring:url value="${contestURL}/quest/challenges#" />{{challenge.id}}">{{challenge.name}}</a>--%>
                                <%--</div>--%>
                            <%--</th>--%>
                        <%--</tr>--%>
                    <%--</thead>--%>
                    <%--<tbody>--%>
                    <%--<tr ng-repeat="participant in participants | orderBy:[sortByPoints,'name']">--%>
                        <%--<td>{{$index + 1}}</td>--%>
                        <%--<td><a href="<spring:url value="/person/" />{{participant.id}}">{{participant.name}}</a></td>--%>
                        <%--<td class="text-center">{{participant.points}}</td>--%>
                        <%--<td class="text-center">{{participant.solved}}</td>--%>
                        <%--<td class="text-center" ng-repeat="challenge in challenges | orderBy:'name'">--%>
                            <%--<i ng-if="participant[challenge.id].state === 'ACCEPTED'" id="submission_{{participant.id}}_{{challenge.id}}" class="fa fa-check leaderboard-submission" style="color: green;" rel="tooltip"--%>
                               <%--data-toggle="tooltip" title="<spring:message code="quest.leaderboard.accepted" />"--%>
                                    <%--></i>--%>
                            <%--<i ng-if="participant[challenge.id].state === 'PENDING'" id="submission_{{participant.id}}_{{challenge.id}}" class="fa fa-clock-o leaderboard-submission" style="color: orange;" rel="tooltip"--%>
                               <%--data-toggle="tooltip" title="<spring:message code="quest.leaderboard.pending" />"--%>
                                    <%--></i>--%>
                            <%--<i ng-if="participant[challenge.id].state === 'REJECTED'" id="submission_{{participant.id}}_{{challenge.id}}" class="fa fa-ban leaderboard-submission" style="color: red;" rel="tooltip"--%>
                               <%--data-toggle="tooltip" title="<spring:message code="questAdmin.submissions.reject.reason" />: {{participant[challenge.id].reason}}"--%>
                                    <%--></i>--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                    <%--</tbody>--%>
                <%--</table>--%>
            <%--</div>--%>

            <%--<script type="application/javascript">--%>
                <%--$(function () {--%>
                    <%--var ngController = angular.element($("#mainLeaderboard")).scope();--%>
                    <%--var challengesJSON = ${empty challengesJSON ? '[]' : challengesJSON};--%>
                    <%--var participantsJSON = ${empty participantsJSON ? '[]' : participantsJSON};--%>
                    <%--ngController.init(challengesJSON, participantsJSON);--%>

                <%--});--%>
            <%--</script>--%>


        </c:if>

        <c:if test="${empty leaderboards}">
            <div class="no-items-available">
                <spring:message code="quest.leaderboard.noResult" />
            </div>
        </c:if>

    </jsp:body>
</t:template>
