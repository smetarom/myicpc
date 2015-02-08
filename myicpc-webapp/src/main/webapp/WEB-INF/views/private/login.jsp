<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>

    <title><spring:message code="login.pleaseSignIn"/></title>

    <link rel="stylesheet" href='<spring:url value="/css/bootstrap.min.css" />' type="text/css">
    <link rel="stylesheet" href='<spring:url value="/css/myicpc/admin.css" />' type="text/css">

</head>

<body id="loginPage">
<div class="container text-center">
    <h2 class="form-signin-heading">
        <spring:message code="login.pleaseSignIn"/>
    </h2>
    <c:if test="${not empty logoutSuccess}">
        <div class="alert alert-info" role="alert"><spring:message code="logout.success" /></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">
            <spring:message code="login.failed"/>
            <br/> <strong><spring:message
                code="login.failed.reason"/></strong>: ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
        </div>
    </c:if>
    <form class="form-signin" action='<spring:url value='/private/login' />' method='POST'>
        <%-- <p>Username: 'admin' Password: 'icpc'</p> --%>
        <input type="text" class="form-control" name="username" placeholder="<spring:message code="login.username" />"
               autofocus/>
        <input type="password" class="form-control" name="password"
               placeholder="<spring:message code="login.password" />"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button class="btn btn-lg btn-primary btn-block" name="submit" type="submit" value="submit">
            <spring:message code="signIn"/>
        </button>
    </form>
</div>
</body>
</html>