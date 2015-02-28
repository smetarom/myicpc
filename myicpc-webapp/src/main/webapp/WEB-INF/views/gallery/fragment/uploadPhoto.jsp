<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<spring:url var="formAction" value="${contestURL}/gallery/uploadPhoto" />
<form action="${formAction}" class="form-horizontal" enctype="multipart/form-data" method="post">
    <t:modalWindow id="uploadModalPhoto">
        <jsp:attribute name="title">
            <t:glyphIcon icon="picture" /> <spring:message code="crowdGallery.uploadPhoto" />
        </jsp:attribute>
        <jsp:attribute name="footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">
                <spring:message code="close" />
            </button>
            <button type="submit" class="btn btn-primary">
                <spring:message code="upload" />
            </button>
        </jsp:attribute>
        <jsp:body>
            <div class="form-group">
                <label for="photoCaption" class="col-lg-2 control-label"><spring:message code="crowdGallery.caption" />:</label>
                <div class="col-lg-10">
                    <input type="text" id="photoCaption" class="form-control" name="caption" placeholder="<spring:message code="crowdGallery.caption.placeholder" />" />
                </div>
            </div>
            <div class="form-group">
                <label for="photoFile" class="col-lg-2 control-label"><spring:message code="crowdGallery.uploadFile" />:*</label>
                <div class="col-lg-10">
                    <input type="file" class="form-control" id="photoFile" name="file" accept="image/*" required="required" />
                </div>
            </div>
        </jsp:body>
    </t:modalWindow>
</form>