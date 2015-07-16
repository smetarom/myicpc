<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<%@attribute name="title" fragment="true" %>
<%@attribute name="head" fragment="true" %>
<%@attribute name="headline" fragment="true" %>
<%@attribute name="headlineRight" fragment="true" %>
<%@attribute name="javascript" fragment="true" %>

<!DOCTYPE html>
<html>
<head>
    <title>
        <c:if test="${not empty title}">
            <jsp:invoke fragment="title" /> &middot;
        </c:if>
            ${contest.shortName} &middot; MyICPC
    </title>
    <%@ include file="/WEB-INF/views/includes/head.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <jsp:invoke fragment="head" />
</head>
<body class="${sitePreference.mobile ? 'mobile' : ''}">
<div id="bodyContainer">
    <c:if test="${not sitePreference.mobile}">
        <%@ include file="/WEB-INF/views/includes/header.jsp" %>
    </c:if>
    <c:if test="${sitePreference.mobile}">
        <%@ include file="/WEB-INF/views/includes/header_mobile.jsp" %>
    </c:if>
    <c:if test="${not empty headline}">
        <div id="pageTitle" class="page-header clearfix" style="margin-bottom: 10px;">
            <c:if test="${not empty headline}">
                <div class="pull-right">
                    <jsp:invoke fragment="headlineRight" />
                </div>
            </c:if>
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

<%@ include file="/WEB-INF/views/includes/foot.jsp" %>
<c:if test="${not sitePreference.mobile}">
    <%@ include file="/WEB-INF/views/includes/foot_desktop.jsp" %>
</c:if>
<c:if test="${sitePreference.mobile}">
    <%@ include file="/WEB-INF/views/includes/foot_mobile.jsp" %>
</c:if>
<jsp:invoke fragment="javascript" />
</body>
</html>
