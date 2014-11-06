<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<compress:html compressCss="true" compressJavaScript="true" jsCompressor="closure" closureOptLevel="whitespace">
    <!DOCTYPE html>
    <html>
    <head>
        <title><c:if test="${not empty pageTitle}">
            ${pageTitle} -
        </c:if> MyICPC</title>
        <script type="text/javascript">
            websocketURL = '127.0.0.1';
            if (websocketURL == '') {
                websocketURL = window.location.host;
            }
        </script>
        <jsp:include page="/WEB-INF/views/includes/head.jsp"/>
    </head>
    <body id="kiosk">
    <jsp:doBody/>
    </body>
    </html>
</compress:html>
