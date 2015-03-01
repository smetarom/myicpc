<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<p><spring:message code="contest.twitter.hint"/></p>

<t:springInput id="picasaUsername" labelCode="contest.picasaUsername" path="webServiceSettings.picasaUsername"/>
<t:springInput id="picasaPassword" labelCode="contest.picasaPassword" path="webServiceSettings.picasaPassword"/>
<t:springInputWithButton id="picasaCrowdAlbumId" labelCode="contest.picasaCrowdAlbumId" path="webServiceSettings.picasaCrowdAlbumId">
    <jsp:attribute name="button">
        <button type="button" onclick="createCrowdPicasaAlbum()"  class="btn btn-default"><spring:message code="contest.picasa.createAlbum" /></button>
    </jsp:attribute>
</t:springInputWithButton>
<t:springInputWithButton id="picasaPrivateAlbumId" labelCode="contest.picasaPrivateAlbumId" path="webServiceSettings.picasaPrivateAlbumId">
    <jsp:attribute name="button">
        <button type="button" onclick="createPrivatePicasaAlbum()" class="btn btn-default"><spring:message code="contest.picasa.createAlbum" /></button>
    </jsp:attribute>
</t:springInputWithButton>

<script type="application/javascript">
    function createCrowdPicasaAlbum() {
        $.get('<spring:url value="/private/picasa/create-album/crowd" />',
                {'username': $("#picasaUsername").val(), 'password': $("#picasaPassword").val()},
                function(data) {
                    $("#picasaCrowdAlbumId").val(data);
                }
        )
    }

    function createPrivatePicasaAlbum() {
        $.get('<spring:url value="/private/picasa/create-album/private" />',
                {'username': $("#picasaUsername").val(), 'password': $("#picasaPassword").val()},
                function(data) {
                    $("#picasaPrivateAlbumId").val(data);
                }
        )
    }
</script>

