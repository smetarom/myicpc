<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="nav.admin.gallery.official" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="officialGalleryAdmin" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="nav.admin.gallery.official" /></li>
	</jsp:attribute>

    <jsp:attribute name="controls">
        <t:button href="/private${contestURL}/gallery/official/create" styleClass="btn-hover"><t:glyphIcon icon="plus"/> <spring:message code="officialGalleryAdmin.create" /></t:button>
        <t:button modalOpenId="importGalleryAlbums" styleClass="btn-hover"><t:glyphIcon icon="import" /> <spring:message code="officialGalleryAdmin.import" /></t:button>

        <t:modalWindow id="importGalleryAlbums">
            <jsp:attribute name="title"><spring:message code="officialGalleryAdmin.import" /></jsp:attribute>
            <jsp:body>
                <p><spring:message code="officialGalleryAdmin.import.hint" /></p>
                <form method="post" action='<spring:url value="/private${contestURL}/gallery/official/import" />?${_csrf.parameterName}=${_csrf.token}' class="form-horizontal" enctype="multipart/form-data">
                    <div class="form-group">
                        <t:csvImport name="galleriesCSV" label="Gallery CSV file" />
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-9">
                            <t:button context="primary" type="submit">
                                <spring:message code="import" />
                            </t:button>
                        </div>
                    </div>
                </form>
            </jsp:body>
        </t:modalWindow>
    </jsp:attribute>

  <jsp:body>
    <table class="table">
      <thead>
      <tr>
        <th><spring:message code="galleryAlbum.name" /></th>
        <th class="text-center"><spring:message code="galleryAlbum.publish" /></th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="gallery" items="${galleries}">
        <tr>
          <td>${gallery.name}</td>
          <td class="text-center"><t:tick condition="${gallery.published}" /></td>
          <td class="text-right">
            <t:editButton url="/private${contestURL}/gallery/official/${gallery.id}/edit" />
            <t:button href="/private${contestURL}/gallery/official/${gallery.id}/publish" styleClass="btn-xs"><spring:message code="officialGalleryAdmin.publish" /></t:button>
            <t:deleteButton url="/private${contestURL}/gallery/official/${gallery.id}/delete" confirmMessageCode="officialGalleryAdmin.delete.confirm" confirmMessageArgument="${gallery.name}" />
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </jsp:body>
</t:templateAdmin>