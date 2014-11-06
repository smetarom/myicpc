<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<nav class="navbar navbar-inverse navbar-static-top" style="z-index: 1010; position: relative">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href='<spring:url value="/" />'><span class="glyphicon glyphicon-home"></span>
            <spring:message code="app.name"/></a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse navbar-ex1-collapse">
        <ul class="nav navbar-nav">
            <li><a href="<spring:url value="/schedule" />"><span class="glyphicon glyphicon-calendar"></span>
                <spring:message code="nav.schedule"/></a></li>
            <li><a href="<spring:url value="/scoreboard" />"><span class="glyphicon glyphicon-list"></span>
                <spring:message code="nav.scoreboard"/></a></li>
            <li><a href="<spring:url value="/quest" />"><span class="glyphicon glyphicon-screenshot"></span>
                <spring:message code="nav.quest"/></a></li>

        </ul>

    </div>
    <!-- /.navbar-collapse -->
</nav>