<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdminEdit>
    <jsp:attribute name="title">
		${headlineTitle}
	</jsp:attribute>

	<jsp:attribute name="headline">
		${headlineTitle}
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
        <li><a href="<spring:url value="/private${contestURL}/kiosk" />"><spring:message code="nav.admin.kiosk" /></a></li>
		<li class="active">${headlineTitle}</li>
	</jsp:attribute>

    <jsp:body>
        <t:form action="/private${contestURL}/kiosk/content/update" entity="kioskContent"
                resetFormButton="true" cancelFormURL="/private${contestURL}/kiosk">
            <jsp:attribute name="controls">
                <t:button onclick="previewKioskContent()"><spring:message code="preview" /></t:button>
                <t:button type="submit" context="primary"><spring:message code="save" /></t:button>
            </jsp:attribute>
            <jsp:body>
                <t:springInput labelCode="kiosk.content" path="name" required="true" />
                <t:springCheckbox path="active" labelCode="kiosk.content.active" />
                <t:springTextarea path="content" labelCode="kiosk.content.body" id="kiosk-content-html" rows="12" required="true" />
            </jsp:body>
        </t:form>

        <div class="col-sm-offset-3 col-sm-9">
            <iframe id="kiosk-content-preview" srcdoc="" style="display: none">
                <p>Your browser does not support iframes.</p>
            </iframe>
        </div>

        <script type="application/javascript">
            previewKioskContent = function() {
                $("#kiosk-content-preview").attr('srcdoc', $("#kiosk-content-html").val()).show();
            }
        </script>
    </jsp:body>
</t:templateAdminEdit>