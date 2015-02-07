<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h4>
    <t:faIcon icon="users" />
    <spring:message code="timeline.cheerUpTeams" />
</h4>
<table class="table" style="margin-bottom: 5px;">
    <tbody>
    <c:forEach var="team" items="${followingTeams}">
        <tr>
            <td><a href="<spring:url value="/team/${team.teamContestId}" />">${team.contestTeamName}</a></td>
            <td class="text-right nowrap"><a href="javascript:shareFacebook('${team.contestTeamName}', '#${team.hashtag} ${hashTags}')"><i class="icon-16 icon-facebook-16"></i></a>&nbsp;&nbsp;<a
                    href="https://twitter.com/intent/tweet?hashtags=${team.hashtag},${hashTagsURL}&text=${team.contestTeamName}"
                    ><i class="icon-16 icon-twitter-16"></i></a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<c:if test="${empty followingTeams}">
    <p class="text-center">
        <t:emptyLink id="cheerOnTeamBtn" modalId="cheerOnTeamDialog" styleClass="btn btn-link"><spring:message code="timeline.cheerUpTeams.hint" /></t:emptyLink>
    </p>
</c:if>
<c:if test="${not empty followingTeams}">
    <p class="gray-text">
        <t:emptyLink id="cheerOnTeamBtn" modalId="cheerOnTeamDialog"><span class="fa fa-arrow-right"></span> <spring:message code="timeline.cheerUpTeams.edit" /></t:emptyLink>
    </p>
</c:if>