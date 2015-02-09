<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<nav class="navbar navbar-default top-submenu">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#submenu-navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="submenu-navbar-collapse">
            <ul class="nav navbar-nav">
                <t:menuItem activeItem="contest" active="${tab}" url="${contestURL}/team/${team.externalId}"><spring:message code="teamHome.nav.contest" /></t:menuItem>
                <t:menuItem activeItem="profile" active="${tab}" url="${contestURL}/team/${team.externalId}/profile"><spring:message code="teamHome.nav.about" /></t:menuItem>
                <t:menuItem activeItem="insight" active="${tab}" url="${contestURL}/team/${team.externalId}/insight"><spring:message code="teamHome.nav.insight" /></t:menuItem>
                <t:menuItem activeItem="social" active="${tab}" url="${contestURL}/team/${team.externalId}/social"><spring:message code="teamHome.nav.social" /></t:menuItem>
            </ul>
        </div>
    </div>
</nav>