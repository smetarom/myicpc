<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<td class="pin">
    <a href="#" ng-click="pinTeam(team);" onclick=" return false;">
        <span class="glyphicon" ng-class="{true:'glyphicon-star', 'undefined':'glyphicon-star-empty', false:'glyphicon-star-empty'}[team.followed]"></span>
    </a>
</td>
<td class="team-rank">{{team.teamRank}}</td>
<td class="nowrap team-name"><a href="<spring:url value="/team/{{team.teamId}}"/>">{{team.teamName}}</a></td>
<c:if test="${contest.contestSettings.showRegion}">
    <td class="nowrap hide-region">
        <a href="<spring:url value="${contestURL}/region/{{team.regionId}}"/>">{{team.regionName}}</a>
    </td>
</c:if>
<c:if test="${contest.contestSettings.showUniversity}">
    <td class="text-center">{{team.universityName}}</td>
</c:if>
<c:if test="${contest.contestSettings.showCountry}">
    <td class="text-center">{{team.nationality}}</td>
</c:if>
<td class="text-center total-solved">{{team.nSolved}}</td>
<td class="text-center total-time">{{team.totalTime}}</td>
<%@ include file="/WEB-INF/views/scoreboard/fragment/scoreboardTeamSubmissions.jsp"%>