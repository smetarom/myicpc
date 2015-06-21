<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<compress:html compressCss="true" compressJavaScript="true" jsCompressor="closure" closureOptLevel="whitespace">
    <!DOCTYPE html>
    <html>
    <head>
        <title>MyICPC</title>
        <jsp:include page="/WEB-INF/views/includes/head.jsp"/>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <style type="text/css">
            body {
                padding-top: 0;
            }

            p {
                font-size: 1.3em;
            }
        </style>
    </head>
    <body>
    <div id="body">
        <jsp:doBody/>
    </div>
    </body>
    </html>
</compress:html>