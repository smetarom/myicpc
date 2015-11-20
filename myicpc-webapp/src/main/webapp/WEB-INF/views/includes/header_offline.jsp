<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<nav class="navbar navbar-default navbar-static-top" style="z-index: 1010; position: relative">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href='#' ng-click="selectedTab=null">
            <span class="glyphicon glyphicon-home"></span>
            <spring:message code="app.name"/>
            <spring:message code="offline" />
        </a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse navbar-ex1-collapse">
        <ul class="nav navbar-nav">
            <li ng-class="{active: selectedTab=='schedule'}">
                <a href="#schedule" ng-click="selectedTab='schedule'">
                    <span class="glyphicon glyphicon-calendar"></span>
                    <spring:message code="nav.schedule"/>
                </a>
            </li>
            <li ng-class="{active: selectedTab=='scoreboard'}">
                <a href="#scoreboard" ng-click="selectedTab='scoreboard'">
                    <span class="glyphicon glyphicon-list"></span>
                    <spring:message code="nav.scoreboard"/>
                </a>
            </li>
            <li ng-class="{active: selectedTab=='quest'}">
                <a href="#quest" ng-click="selectedTab='quest'">
                    <span class="glyphicon glyphicon-screenshot"></span>
                    <spring:message code="nav.quest"/>
                </a>
            </li>

        </ul>

    </div>
    <!-- /.navbar-collapse -->
</nav>