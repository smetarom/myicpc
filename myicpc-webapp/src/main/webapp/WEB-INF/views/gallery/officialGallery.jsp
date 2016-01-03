<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
    </jsp:attribute>
    <jsp:attribute name="headline">
        <spring:message code="officialGallery" />
    </jsp:attribute>
    <jsp:attribute name="title">
        <spring:message code="officialGallery" />
    </jsp:attribute>
    <jsp:attribute name="javascript">
        <script src="<c:url value='/js/myicpc/controllers/officialGallery.js'/>"></script>
    </jsp:attribute>

    <jsp:body>
        <div ng-app="officialGallery" ng-controller="officialGalleryCtrl">
            <form class="form-inline gallery-filter text-right col-sm-12" id="officialGalleryFilter" ng-init="init(${contest.contestYear})">
                <select class="form-control" ng-change="eventFilterChanged()" ng-model="currentEvent">
                    <option value=""><spring:message code="officialGallery.event" /></option>
                    <c:forEach var="gallery" items="${galleries}">
                        <option>${gallery.name}</option>
                    </c:forEach>
                </select>
                <select class="form-control" ng-change="teamFilterChanged()" ng-model="currentTeam">
                    <option value=""><spring:message code="officialGallery.team" /></option>
                    <c:forEach var="team" items="${teams}">
                        <option>${team.contestTeamName}</option>
                    </c:forEach>
                </select>
                <select class="form-control" ng-change="peopleFilterChanged()" ng-model="currentPerson">
                    <option value=""><spring:message code="officialGallery.people" /></option>
                    <c:forEach var="contestParticipant" items="${contestParticipants}">
                        <option value="${contestParticipant.fullname}">${contestParticipant.fullname}</option>
                    </c:forEach>
                </select>
            </form>

            <div class="text-center" ng-cloak>
                <span ng-repeat="photo in photos">
                    <a href="#" ng-click="showPhotoDetail($index)" class="gallery-thumbnail" data-toggle="modal" data-target="#galleryPopup">
                        <img ng-src="{{photo.thumbnailUrl}}" alt="${notification.title}" width="250" height="250">
                    </a>
                </span>
                <br />
                <button ng-click="loadMore()" class="load-more-btn btn btn-default" type="button">
                    <spring:message code="showMore" />
                </button>
            </div>

            <div class="modal fade" id="galleryPopup" tabindex="-1" role="dialog" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div id="galleryPopupContent" class="modal-body clearfix">
                            <div class="col-sm-8 gallery-content">
                                <div>
                                    <img ng-src="{{currentPhoto.imageUrl}}" alt="" class="img-responsive center-block">
                                </div>
                            </div>
                            <div class="col-sm-4 gallery-submenu">
                                <div class="text-center">
                                    <button type="button" ng-click="previousPhoto()" class="btn btn-link"><t:glyphIcon icon="arrow-left" /> <spring:message code="previous" /></button>
                                    <button type="button" class="btn btn-link" data-dismiss="modal"><t:glyphIcon icon="th" /></button>
                                    <button type="button" ng-click="nextPhoto()" class="btn btn-link"><spring:message code="next" /> <t:glyphIcon icon="arrow-right" /></button>
                                </div>
                                <p>{{currentPhoto.author}}</p>
                                <p ng-hide="!currentPhoto.events.length">
                                    <spring:message code="officialGallery.event" />:
                                    <span ng-repeat="event in currentPhoto.events">
                                        <a href="#" ng-click="searchEvent(event)">{{event}}</a>{{$last ? '' : ', '}}
                                    </span>
                                </p>
                                <p ng-hide="!currentPhoto.teams.length">
                                    <spring:message code="officialGallery.team" />:
                                    <span ng-repeat="team in currentPhoto.teams">
                                        <a href="#" ng-click="searchTeam(team)">{{team}}</a>{{$last ? '' : ', '}}
                                    </span>
                                </p>
                                <p ng-hide="!currentPhoto.people.length">
                                    <spring:message code="officialGallery.people" />:
                                    <span ng-repeat="person in currentPhoto.people">
                                        <a href="#" ng-click="searchPeople(person)">{{person}}</a>{{$last ? '' : ', '}}
                                    </span>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </jsp:body>

</t:template>
