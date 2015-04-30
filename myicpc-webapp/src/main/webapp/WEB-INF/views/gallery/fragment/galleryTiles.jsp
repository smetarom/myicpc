<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<c:forEach var="notification" items="${notifications}">
    <a href="#" onclick="showGalleryModal(this);" class="gallery-thumbnail" data-toggle="modal" data-target="#galleryPopup"
       data-id="${notification.id}"
       data-image-url="${notification.imageUrl}"
       data-video-url="${notification.videoUrl}"
       data-url="${notification.url}"
       data-author-name="${notification.authorName}"
       data-author-avatar="${notification.profilePictureUrl}">
        <c:if test="${not empty notification.videoUrl}">
            <div class="play-button glyphicon glyphicon-play-circle"></div>
        </c:if>
        <img src="${notification.thumbnailUrl}" alt="${notification.title}" width="250" height="250"
             onerror="removeOnError(this, ${notification.id})">
    </a>
</c:forEach>

<script type="application/javascript">
    var lastNotificationId = ${not empty lastNotificationId ? lastNotificationId : -1};
    if (lastNotificationId == -1) {
        $('.load-more-btn').hide();
    }
</script>