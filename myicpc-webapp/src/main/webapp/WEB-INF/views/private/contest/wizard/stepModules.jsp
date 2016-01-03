<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div class="clearfix">
    <div class="col-sm-6">
        <table class="table">
            <tbody>
            <jsp:include page="/WEB-INF/views/private/contest/fragment/moduleControlRow.jsp">
                <jsp:param name="module" value="map" />
            </jsp:include>
            <jsp:include page="/WEB-INF/views/private/contest/fragment/moduleControlRow.jsp">
                <jsp:param name="module" value="codeInsight" />
            </jsp:include>
            <jsp:include page="/WEB-INF/views/private/contest/fragment/moduleControlRow.jsp">
                <jsp:param name="module" value="schedule" />
            </jsp:include>
            <jsp:include page="/WEB-INF/views/private/contest/fragment/moduleControlRow.jsp">
                <jsp:param name="module" value="rss" />
            </jsp:include>
            <jsp:include page="/WEB-INF/views/private/contest/fragment/moduleControlRow.jsp">
                <jsp:param name="module" value="poll" />
            </jsp:include>
            <jsp:include page="/WEB-INF/views/private/contest/fragment/moduleControlRow.jsp">
                <jsp:param name="module" value="gallery" />
            </jsp:include>
            <jsp:include page="/WEB-INF/views/private/contest/fragment/moduleControlRow.jsp">
                <jsp:param name="module" value="officialGallery" />
            </jsp:include>
            <jsp:include page="/WEB-INF/views/private/contest/fragment/moduleControlRow.jsp">
                <jsp:param name="module" value="quest" />
            </jsp:include>
            </tbody>
        </table>
    </div>

    <div id="moduleDescriptionWrapper" class="col-sm-6">
        <p class="no-items-available"><spring:message code="moduleAdmin.hint" /></p>
        <p id="mapModule" style="display: none;"><spring:message code="module.map.description" /></p>
        <p id="codeInsightModule" style="display: none;"><spring:message code="module.codeInsight.description" /></p>
        <p id="scheduleModule" style="display: none;"><spring:message code="module.schedule.description" /></p>
        <p id="rssModule" style="display: none;"><spring:message code="module.rss.description" /></p>
        <p id="pollModule" style="display: none;"><spring:message code="module.poll.description" /></p>
        <p id="galleryModule" style="display: none;"><spring:message code="module.gallery.description" /></p>
        <p id="officialGalleryModule" style="display: none;"><spring:message code="module.officialGallery.description" /></p>
        <p id="questModule" style="display: none;"><spring:message code="module.quest.description" /></p>
    </div>
</div>

<script type="application/javascript">
    function showModuleDescription(id) {
        $("#moduleDescriptionWrapper p").hide();
        $("#"+id).show();
    }
</script>