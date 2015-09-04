<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<%@attribute name="head" fragment="true" %>

<!DOCTYPE html>
<html>
<head>
    <title>MyICPC</title>
    <jsp:include page="/WEB-INF/views/includes/head.jsp"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value='/css/myicpc/error.css'/>" type="text/css">
    <jsp:invoke fragment="head" />
</head>
<body>
<div id="body">
    <jsp:doBody/>
</div>
</body>
</html>