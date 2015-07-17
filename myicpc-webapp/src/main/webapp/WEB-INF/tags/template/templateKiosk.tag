<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<%@attribute name="head" fragment="true" %>
<%@attribute name="javascript" fragment="true" %>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="app.name" /> Kiosk</title>
    <jsp:include page="/WEB-INF/views/includes/head.jsp"/>
    <link rel="stylesheet" href="<c:url value='/css/myicpc/kiosk.css'/>" type="text/css">
    <jsp:invoke fragment="head" />
</head>
<body id="kiosk">
    <div class="page-header text-center">
        <img src="http://myicpc.icpcnews.com/static/images/2015-Slide-Header2.png" style="padding: 5px 0; max-height: 100px" />
    </div>
    <jsp:doBody/>
    <jsp:include page="/WEB-INF/views/includes/foot.jsp"/>
    <jsp:invoke fragment="javascript" />
</body>
</html>