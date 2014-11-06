<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<compress:html compressCss="true" compressJavaScript="true" jsCompressor="closure" closureOptLevel="whitespace">
    <!DOCTYPE html>
    <html>
    <head>
        <title>Offline - MyICPC</title>
        <%@ include file="/WEB-INF/views/includes/headOffline.jsp" %>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
    <div id="bodyContainer">
        <jsp:include page="/WEB-INF/views/includes/header_offline.jsp"/>
        <div class="alert alert-danger" style="margin-bottom: 0;">
            <spring:message code="offline.warning"/>
            <a href="javascript: location.reload();" class="alert-link"><spring:message
                    code="offline.warning.link"/></a>
        </div>
        <div id="body">
            <jsp:doBody/>
        </div>
        <%@ include file="/WEB-INF/views/includes/footer_offline.jsp" %>
    </div>
    </body>
    </html>
</compress:html>
