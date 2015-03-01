<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>

<%@attribute name="title" fragment="true" %>
<%@attribute name="head" fragment="true" %>
<%@attribute name="headline" fragment="true" %>

<compress:html compressCss="true">
    <!DOCTYPE html>
    <html>
    <head>
        <title>
            <c:if test="${not empty title}">
                <jsp:invoke fragment="title" /> &middot;
            </c:if>
                ${contest.shortName} &middot; MyICPC
        </title>
        <jsp:include page="/WEB-INF/views/includes/head.jsp"/>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <jsp:invoke fragment="head" />
    </head>
    <body class="${sitePreference.mobile ? 'mobile' : ''}">
    <div id="bodyContainer">
        <c:if test="${not sitePreference.mobile}">
            <jsp:include page="/WEB-INF/views/includes/header.jsp"/>
        </c:if>
        <c:if test="${sitePreference.mobile}">
            <jsp:include page="/WEB-INF/views/includes/header_mobile.jsp"/>
        </c:if>
        <c:if test="${not empty headline}">
            <div class="page-header" style="margin-bottom: 10px;">
                <h2>
                    <jsp:invoke fragment="headline" />
                </h2>
            </div>
        </c:if>

        <%@ include file="/WEB-INF/views/includes/messages.jsp" %>

        <div id="body" class="clearfix">
            <jsp:doBody/>
        </div>
        <%@ include file="/WEB-INF/views/includes/footer.jsp" %>
    </div>
    <iframe src="<c:url value="/loadCacheManifest" />" style="display: none;"></iframe>
    </body>
    </html>
</compress:html>
