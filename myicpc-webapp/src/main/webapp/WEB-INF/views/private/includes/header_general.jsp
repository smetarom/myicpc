<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<nav id="admin-navbar" class="navbar navbar-default">

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
            <sec:authorize url="/private/users">
                <li><a href='<spring:url value="/private/users" />'><t:faIcon icon="users" /> <spring:message code="nav.admin.users"/></a></li>
            </sec:authorize>
            <sec:authorize url="/private/getting-started">
                <li><a href='<spring:url value="/private/getting-started" />'><spring:message code="nav.admin.gettingStarted"/></a></li>
            </sec:authorize>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <sec:authorize url="/private/settings">
                <li>
                    <a href="<spring:url value="/private/settings" />"><t:tooltip titleCode="settingsAdmin.title"><t:glyphIcon icon="cog" /></t:tooltip></a>
                </li>
            </sec:authorize>
            <%@ include file="/WEB-INF/views/private/includes/header_right_appendix.jsp"%>
        </ul>
    </div>
    <script type="text/javascript">
        $(function () {
            $(".dropdown").hover(
                    function () {
                        $('.dropdown-menu', this).stop(true, true).slideDown("fast");
                        $(this).toggleClass('open');
                    },
                    function () {
                        $('.dropdown-menu', this).stop(true, true).slideUp("fast");
                        $(this).toggleClass('open');
                    }
            );
        });
    </script>
</nav>
