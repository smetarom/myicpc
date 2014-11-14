<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<nav class="navbar navbar-default">

    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href='<spring:url value="/private/home" />'><spring:message code="app.nameAdmin"/></a>
    </div>

    <div class="collapse navbar-collapse navbar-ex1-collapse">
        <ul class="nav navbar-nav">
            <li><a href='<spring:url value="/private/contests" />'><spring:message code="nav.admin.contests"/></a></li>
            <li><a href='<spring:url value="/private/users" />'><t:faIcon icon="users" /> <spring:message code="nav.admin.users"/></a></li>
            <li><a href="<spring:url value="/private/help" />"><strong>?</strong> <spring:message
                    code="nav.admin.help"/></a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <%@ include file="/WEB-INF/views/private/includes/header_right_appendix.jsp"%>
        </ul>
    </div>
</nav>
