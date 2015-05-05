<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<td class="pin">
    <a href="#" ng-click="pinTeam(team);" onclick=" return false;">
        <span class="glyphicon" ng-class="{true:'glyphicon-star', 'undefined':'glyphicon-star-empty', false:'glyphicon-star-empty'}[team.followed]"></span>
    </a>
</td>
<td class="team-rank">{{team.teamRank}}</td>
<td class="nowrap team-name"><a href="<spring:url value="${contestURL}/team/{{team.teamId}}"/>" title="{{team.teamName}}">{{team.teamName | limitTo: 50}}</a></td>
<c:if test="${contest.contestSettings.showRegion}">
    <td class="nowrap hide-region">
        <a href="javascript:void(0)" ng-click="filterByRegion(team.regionId)">{{team.regionName}}</a>
    </td>
</c:if>
<c:if test="${contest.contestSettings.showUniversity}">
    <td class="text-center">
        <a href="javascript:void(0)" ng-click="filterByUniversity(team.universityName)">{{team.universityName}}</a>
    </td>
</c:if>
<c:if test="${contest.contestSettings.showCountry}">
    <td class="text-center">
        <a href="javascript:void(0)" ng-click="filterByNationality(team.nationality)">{{team.nationality}}</a>
    </td>
</c:if>
<td class="text-center total-solved">{{team.nSolved}}</td>
<td class="text-center total-time">{{team.totalTime}}</td>
<%@ include file="/WEB-INF/views/scoreboard/fragment/scoreboardTeamSubmissions.jsp"%>