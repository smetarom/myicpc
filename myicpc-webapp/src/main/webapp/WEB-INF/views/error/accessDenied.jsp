<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateError>
    <jsp:attribute name="head">
        <style type="text/css">
            body {
                background-color: #fb8c00;
            }
        </style>
    </jsp:attribute>

    <jsp:body>
        <div class="error-container center-block">
            <t:panelWithHeading>
                <div style="font-size: 4em;">
                    <t:faIcon icon="lock" />
                </div>
                <h1><spring:message code="denied" /></h1>

                <p style="font-size: 1.2em;">
                    <spring:message code="denied.hint" />
                    <a href="<spring:url value="/private/logout" />"> <spring:message code="denied.hint.1" /></a>
                    <spring:message code="denied.hint.2" />
                </p>

                <button type="button" onclick="window.history.back()" class="btn btn-primary">
                    <spring:message code="back" />
                </button>
            </t:panelWithHeading>
        </div>
    </jsp:body>
</t:templateError>
