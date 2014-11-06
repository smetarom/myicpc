<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav class="navbar navbar-default">

    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href='<spring:url value="/private/home" />'><spring:message code="app.nameAdmin"/></a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse navbar-ex1-collapse">
        <ul class="nav navbar-nav">
            <li><a href='<spring:url value="/private/participants" />'><spring:message
                    code="nav.admin.participants"/></a></li>
            <li><a href='<spring:url value="/private/users" />'><spring:message code="nav.admin.users"/></a></li>
            <li><a href="<spring:url value="/private/help" />"><strong>?</strong> <spring:message
                    code="nav.admin.help"/></a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><sec:authentication
                    property="principal.username"/> <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="<spring:url value="/private/profile" />"><spring:message code="nav.admin.profile"/></a>
                    </li>
                    <li class="divider"></li>
                    <li><a href="<spring:url value="/private/j_spring_security_logout" />"><spring:message
                            code="logout"/></a></li>
                </ul>
            </li>
        </ul>
    </div>
    <!-- /.navbar-collapse -->
</nav>
