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
        <jsp:include page="/WEB-INF/views/includes/headAdmin.jsp"/>
        <meta name="viewport" content="width=device-width, initial-scale=1">
    </head>
    <body>
    <div id="bodyContainer">
        <div id="body">
            <jsp:doBody/>
        </div>
    </div>
    </body>
    </html>
</compress:html>
