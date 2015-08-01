<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
        <%@ include file="/WEB-INF/views/includes/nvd3Dependencies.jsp" %>
        <script src="<c:url value='/js/myicpc/controllers/insight.js'/>" defer></script>
    </jsp:attribute>

    <jsp:attribute name="title">
      ${teamInfo.contestTeamName}
    </jsp:attribute>

    <jsp:attribute name="headline">
      ${teamInfo.contestTeamName}
    </jsp:attribute>

    <jsp:attribute name="javascript">
		<script src="<c:url value='/js/myicpc/controllers/officialGallery.js'/>"></script>
    </jsp:attribute>

    <jsp:body>
        <c:set var="teamContestId" value="${team.externalId}"/>
        <c:set var="teamPresentationId" value="${teamInfo.externalId}"/>
        <%@ include file="/WEB-INF/views/scoreboard/fragment/teamHomeMenu.jsp" %>

        <div class="col-sm-6">
            <t:hashtagPanel id="teamSocial" contestURL="${contestURL}" hashtag1="${teamInfo.hashtag}" hashtag2="${contest.hashtag}" />

        </div>
        <div class="col-sm-6">
            <div ng-app="officialGallery" ng-controller="teamGalleryCtrl" ng-init="init('${teamInfo.fullPicasaTag}')" ng-cloak>
                <h3 ng-if="photos.length"><spring:message code="officialGallery" /></h3>
                <div ng-if="photos.length">
                    <t:button href="${contestURL}/gallery/official#${teamInfo.fullPicasaTag}" context="info" styleClass="btn-block"><spring:message code="officialGallery.viewAll" /></t:button>
                </div>
                <div ng-repeat="photo in photos" class="thumbnail">
                    <img ng-src="{{photo.imageUrl}}" alt="" style="max-height: 350px" />
                </div>
            </div>
        </div>
    </jsp:body>
</t:template>
