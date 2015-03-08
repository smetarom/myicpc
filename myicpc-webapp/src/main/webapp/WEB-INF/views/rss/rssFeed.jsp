<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="headline">
        ${pageTitle}
    </jsp:attribute>
    <jsp:attribute name="title">
        ${pageTitle}
    </jsp:attribute>

    <jsp:body>
        <c:forEach var="message" items="${rssMessagesPage.getContent()}">
            <div class="col-sm-12 col-md-6">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <h4>${message.name}</h4>
                            ${message.text}
                    </div>
                    <div class="panel-footer">
                        <div class="hidden-xs">
                            <div class="pull-right">
                                <a href="${message.url }"><spring:message code="rss.seeOriginal" /></a> |
                                <a href="#" onclick="shareFacebookUrl('${message.url }');" class="icon-16 icon-facebook-16"> </a> |
                                <a href="https://twitter.com/intent/tweet?url=${message.url}" class="icon-16 icon-twitter-16"></a> |
                                <a href="#" onclick="shareGoogleUrl('${message.url }');" class="icon-16 icon-google-16"></a>
                            </div>
                        </div>
                        <div class="visible-xs">
                            <a href="${message.url }"><spring:message code="rss.seeOriginal" /></a> |
                            <a href="#" onclick="shareFacebookUrl('${message.url }');" class="icon-16 icon-facebook-16"> </a> |
                            <a href="https://twitter.com/intent/tweet?url=${message.url}" class="icon-16 icon-twitter-16"></a> |
                            <a href="#" onclick="shareGoogleUrl('${message.url }');" class="icon-16 icon-google-16"></a>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>

        <%--<t:pager pager="${rssMessagesPage}"></t:pager>--%>
    </jsp:body>

</t:template>
