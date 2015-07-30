<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
    </jsp:attribute>
    <jsp:attribute name="headline">
        ${pageTitle}
    </jsp:attribute>
    <jsp:attribute name="title">
        ${pageTitle}
    </jsp:attribute>
    <jsp:attribute name="javascript">

        <script id="gallery-item-template" type="text/x-jquery-tmpl">
            <a href="#" onclick="Gallery.showGalleryModal(this);" class="gallery-thumbnail" data-toggle="modal" data-target="#galleryPopup"
               data-id="{{id}}"
               data-image-url="{{imageUrl}}"
               data-video-url="{{videoUrl}}"
               data-thumbnail-url="{{thumbnailUrl}}"
               data-url="{{url}}"
               data-author-name="{{authorName}}"
               data-author-avatar="{{profileUrl}}">
                {{#if isVideo}}
                    <div class="play-button glyphicon glyphicon-play-circle"></div>
                {{/if}}
                <img src="{{thumbnailUrl}}" alt="{{title}}" width="250" height="250" onerror="removeOnError(this, {{id}})">
            </a>
        </script>

        <script id="gallery-modal-template" type="text/x-jquery-tmpl">
        <div class="col-sm-8 gallery-content">
            <div>
                {{#if videoUrl}}
                    <video src="{{videoUrl}}" poster="{{thumbnailUrl}}" controls autoplay width="100%" onerror="removeOnError(this, {{notificationId}})">
                        Your browser does not support the video player.
                    </video>
                {{else}}
                    <img src="{{imageUrl}}" alt="{{authorName}}" class="img-responsive center-block" onerror="removeOnError(this, {{notificationId}})">
                {{/if}}
            </div>
        </div>
        <div class="col-sm-4 gallery-submenu">
            <div class="text-center">
                <button type="button" onclick="Gallery.previousTile();" class="btn btn-link"><t:glyphIcon icon="arrow-left" /> <spring:message code="previous" /></button>
                <button type="button" class="btn btn-link" data-dismiss="modal"><t:glyphIcon icon="th" /></button>
                <button type="button" onclick="Gallery.nextTile();" class="btn btn-link"><spring:message code="next" /> <t:glyphIcon icon="arrow-right" /></button>
            </div>
            <table>
                <tr>
                    <td>
                        <img src="{{avatarUrl}}" alt="{{authorName}}" width="50" height="50">
                    </td>
                    <td class="gallery-detail-author"><strong>{{authorName}}</strong></td>
                </tr>
            </table>

            <div>
                <a href="{{originalUrl}}" target="_blank" class="btn btn-link btn-block"><spring:message code="view.originalPost" /></a>
            </div>
        </div>
        </script>

        <script src="<c:url value='/js/myicpc/gallery.js'/>" defer></script>
        <script type="application/javascript">
            var currentTile = null;
            var currentMedia = "${not empty active ? active : 'gallery'}";

            function removeOnError(elem, id) {
                console.log($(elem));
                $(elem).parent().remove();
                $.post("<spring:url value="${contestURL}/gallery/remove/" />" + id);
            }

            var $galleryPopup = $('#galleryPopup');
            $galleryPopup.on('hide.bs.modal', function () {
                $("#galleryPopupContent").html('');
                currentTile = null;
            });

            $galleryPopup.keydown(function(e) {
                if (e.keyCode === 37) {
                    Gallery.previousTile();
                } else if (e.keyCode === 39) {
                    Gallery.nextTile();
                }
            });

            function acceptGalleryPost(notification) {
                var acceptedTypes = ${not empty acceptedTypes ? acceptedTypes : []};
                if (acceptedTypes.valueOf(notification.type) !== -1) {
                    if (notification.imageUrl !== null || notification.videoUrl !== null) {
                        return true;
                    }
                }
                return false;
            };

            $(window).scroll(function() {
                if($(window).scrollTop() == $(document).height() - $(window).height() && !$(".load-more-btn").hasClass('hidden')) {
                    $(".load-more-btn").removeClass('hidden');
                    Gallery.loadMore("<spring:url value="${contestURL}/gallery/loadMore" />");
                }
            });

            $(function() {
                Gallery.setLoadMoreUrl('<spring:url value="${contestURL}/gallery/loadMore" />')
                Gallery.acceptGalleryPost = acceptGalleryPost;
                startSubscribe('${r.contextPath}', '${contest.code}', 'notification', Gallery.updateGallery, null);
            })
        </script>


    </jsp:attribute>

    <jsp:body>
        <div style="margin: 0 ${sitePreference.mobile ? '3px' : '15px'}" class="clearfix">
            <div class="btn-group ${sitePreference.mobile ? 'btn-group-justified' : ''}">
                <c:if test="${active ne 'gallery'}">
                    <a href="<spring:url value="${contestURL}/gallery" />" class="btn btn-default">
                        <t:glyphIcon icon="th" />
                        <span class="hidden-xs"><spring:message code="crowdGallery.allSources" /></span>
                    </a>
                </c:if>
                <c:if test="${active ne 'photos'}">
                    <a href="<spring:url value="${contestURL}/photos" />" class="btn btn-default">
                        <t:glyphIcon icon="camera" />
                        <span class="hidden-xs"><spring:message code="crowdGallery.onlyPhotos" /></span>
                    </a>
                </c:if>
                <c:if test="${active ne 'videos'}">
                    <a href="<spring:url value="${contestURL}/videos" />" class="btn btn-default">
                        <t:glyphIcon icon="facetime-video" />
                        <span class="hidden-xs"><spring:message code="crowdGallery.onlyVideos" /></span>
                    </a>
                </c:if>
                <c:if test="${showUploadButton && sitePreference.mobile}">
                    <a data-toggle="modal" href="#uploadModalPhoto" class="btn btn-default"><t:glyphIcon icon="picture" /> <spring:message code="crowdGallery.uploadPhoto" /></a>
                </c:if>
            </div>

            <c:if test="${showUploadButton}">
                <div class="pull-right">
                    <div class="btn-group hidden-xs">
                        <a data-toggle="modal" href="#uploadModalPhoto" class="btn btn-default"><t:glyphIcon icon="picture" /> <spring:message code="crowdGallery.uploadPhoto" /> </a>
                    </div>
                </div>
                <%@ include file="/WEB-INF/views/gallery/fragment/uploadPhoto.jsp"%>
            </c:if>
        </div>

        <div class="col-sm-12 text-center">
            <div id="galleryTiles" class="text-center">
                <%@ include file="/WEB-INF/views/gallery/fragment/galleryTiles.jsp" %>
            </div>
            <br />
            <button onclick="Gallery.loadMore('<spring:url value="${contestURL}/gallery/loadMore" />');" class="load-more-btn btn btn-default" type="button">
                <spring:message code="showMore" />
            </button>
        </div>

        <div class="modal fade" id="galleryPopup" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div id="galleryPopupContent" class="modal-body clearfix">
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>

</t:template>
