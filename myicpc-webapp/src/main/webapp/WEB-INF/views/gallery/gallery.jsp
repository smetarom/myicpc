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
            <button onclick="loadMore('<spring:url value="${contestURL}/gallery/loadMore" />');" class="load-more-btn btn btn-default" type="button">
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

        <script type="application/javascript">
            var currentTile = null;
            var currentMedia = "${not empty active ? active : 'gallery'}";

            function showGalleryModal(tile) {
                var modalTemplate = compileHandlebarsTemplate("gallery-modal-template");
                var context = {};
                context['originalUrl'] = $(tile).data('url');
                context['imageUrl'] = $(tile).data('image-url');
                context['videoUrl'] = $(tile).data('video-url');
                context['authorName'] = $(tile).data('author-name');
                context['avatarUrl'] = $(tile).data('author-avatar');
                context['text'] = $(tile).data('text');

                $("#galleryPopupContent").html(modalTemplate(context));
                currentTile = tile;
            }

            function previousTile() {
                if (currentTile !== null) {
                    var $currentTile = $(currentTile);
                    if($currentTile.prev().length) {
                        var previousTile = $currentTile.prev();
                        showGalleryModal(previousTile);
                        currentTile = previousTile;
                    } else {
                        $('#galleryPopup').modal('hide');
                    }
                }
            }

            function nextTile() {
                if (currentTile !== null) {
                    var nextTile = $(currentTile).next();
                    showGalleryModal(nextTile);
                    currentTile = nextTile;
                }
            }

            function loadMore(url) {
                $.get(url, {'since-notification-id': lastNotificationId, 'media': currentMedia}, function( data ) {
                    if (data.trim() === "") {
                        $(".load-more-btn").addClass('hidden');
                        return false;
                    }
                    $("#galleryTiles").append(data);
                    return true;
                });
            }

            $('#galleryPopup').on('hide.bs.modal', function () {
                $("#galleryPopupContent").html('');
                currentTile = null;
            })

            $('#galleryPopup').keydown(function(e) {
                if (e.keyCode === 37) {
                    previousTile();
                } else if (e.keyCode === 39) {
                    nextTile();
                }
            });

            $(window).scroll(function() {
                console.log($(".load-more-btn").hasClass('hidden'))
                if($(window).scrollTop() == $(document).height() - $(window).height() && !$(".load-more-btn").hasClass('hidden')) {

                    $(".load-more-btn").removeClass('hidden');
                    loadMore("<spring:url value="${contestURL}/gallery/loadMore" />");
                }
            });
        </script>

<script id="gallery-modal-template" type="text/x-jquery-tmpl">
<div class="col-sm-8 gallery-content">
    {{#if videoUrl}}
        <video src="{{videoUrl}}" controls autoplay width="100%" >
            Your browser does not support the video player.
        </video>
    {{else}}
        <img src="{{imageUrl}}" alt="{{authorName}}" class="img-responsive center-block">
    {{/if}}
</div>
<div class="col-sm-4">
    <div class="text-center">
        <button type="button" onclick="previousTile();" class="btn btn-link"><t:glyphIcon icon="arrow-left" /> <spring:message code="previous" /></button>
        <button type="button" class="btn btn-link" data-dismiss="modal"><t:glyphIcon icon="th" /></button>
        <button type="button" onclick="nextTile();" class="btn btn-link"><spring:message code="next" /> <t:glyphIcon icon="arrow-right" /></button>
    </div>
    <table>
        <tr>
            <td>
                <img src="{{avatarUrl}}" alt="{{authorName}}" width="50" height="50">
            </td>
            <td class="gallery-detail-author"><strong>{{authorName}}</strong></td>
        </tr>
    </table>

    <p>
        {{{text}}}
    </p>

    <div>
        <a href="{{originalUrl}}" target="_blank" class="btn btn-primary btn-block"><spring:message code="view.originalPost" /></a>
    </div>
</div>
</script>

    </jsp:body>

</t:template>
