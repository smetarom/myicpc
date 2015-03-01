<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="galleryAdmin.picasa.title" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="galleryAdmin.picasa.title" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
        <li class="active"><spring:message code="galleryAdmin.picasa.title" /></li>
	</jsp:attribute>

    <jsp:body>
        <c:forEach var="photo" items="${newPhotos}">
            <div class="col-sm-4 col-md-3">
                <div class="thumbnail">
                    <img src="${photo.thumbnailUrl}" alt="...">
                    <div class="caption text-center">
                        <form method="post" action="<spring:url value="/private${contestURL}/picasa/approve/${photo.picasaId}" />?${_csrf.parameterName}=${_csrf.token}">
                            <p>
                                <input name="title" value="${photo.title}" style="width: 100%" placeholder="" />
                            </p>
                            <p>
                                <a href="#" onclick="showPhotoDetail('${photo.url}')" data-toggle="modal" data-target="#photoDetail"><spring:message code="galleryAdmin.picasa.viewFull" /></a>
                            </p>
                            <p>
                                <a href='<spring:url value="/private${contestURL}/picasa/delete/${photo.picasaId}" />' class="btn btn-danger pull-left">
                                    <span class="glyphicon glyphicon-remove"></span> <spring:message code="delete" />
                                </a>
                                <button type="submit" class="btn btn-success pull-right">
                                    <span class="glyphicon glyphicon-ok"></span> <spring:message code="approve" />
                                </button>
                                <br class="clear" />
                            </p>
                        </form>
                    </div>
                </div>
            </div>
        </c:forEach>

        <t:modalWindow id="photoDetail">
            <img id="photoContainer" class="img-responsive" />
        </t:modalWindow>

        <script type="text/javascript">
            function showPhotoDetail(photoURL) {
                $("#photoContainer").attr("src", photoURL);
            }
        </script>
    </jsp:body>
</t:templateAdmin>