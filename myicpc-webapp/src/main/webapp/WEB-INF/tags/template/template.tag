<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<compress:html compressCss="true">
    <!DOCTYPE html>
    <html>
    <head>
        <title><c:if test="${not empty pageTitle}">${pageTitle} - </c:if> MyICPC
        </title>
        <script type="text/javascript">
            websocketURL = '127.0.0.1';
            securedWebsocketURL = window.location.host;
            if (websocketURL == '') {
                websocketURL = window.location.host;
            }
        </script>
        <jsp:include page="/WEB-INF/views/includes/head.jsp"/>
        <meta name="viewport" content="width=device-width, initial-scale=1">
    </head>
    <body class="${sitePreference.mobile ? 'mobile' : ''}">
    <div id="bodyContainer">
        <c:if test="${not sitePreference.mobile}">
            <jsp:include page="/WEB-INF/views/includes/header.jsp"/>
        </c:if>
        <c:if test="${sitePreference.mobile}">
            <jsp:include page="/WEB-INF/views/includes/header_mobile.jsp"/>
        </c:if>
        <div id="body">
            <jsp:doBody/>
        </div>
        <%@ include file="/WEB-INF/views/includes/footer.jsp" %>
    </div>
    <iframe src="<c:url value="/loadCacheManifest" />" style="display: none;"></iframe>
    </body>
    </html>
</compress:html>
