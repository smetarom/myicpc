<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>
<%@ taglib prefix="util" uri="http://myicpc.baylor.edu/functions"%>

<nav id="top-menu-nav" class="navbar navbar-inverse navbar-fixed-top desktop">
    <div class="container-fluid">
        <div class="navbar-header">
            <div class="navbar-brand">
                <a href="<spring:url value="${contestURL}" />" id="homepage-link">
                    <t:faIcon icon="home" /> <spring:message code="app.name"/>
                </a>
            </div>
        </div>

        <ul id="top-sub-menu" class="nav navbar-nav">
            <c:if test="${util:scheduleModuleEnabled(contest)}">
                <li class="${sideMenuActive eq 'schedule' ? 'active' : '' } dropdown">
                    <t:emptyLink isDropdown="true" styleClass="dropdown-toggle">
                        <t:glyphIcon icon="calendar" /> <span class="hidden-xs hidden-sm"><spring:message code="nav.schedule"/></span> <b class="caret"></b>
                    </t:emptyLink>
                    <table class="dropdown-menu main-dropdown-submenu">
                        <%@ include file="/WEB-INF/views/includes/topMenu/scheduleSubmenu.jsp" %>
                    </table>
                </li>
            </c:if>
            <li id="main-menu-scoreboard" class="${sideMenuActive eq 'scoreboard' ? 'active' : '' } dropdown">
                <t:emptyLink isDropdown="true" styleClass="dropdown-toggle">
                    <t:glyphIcon icon="list" /> <span class="hidden-xs hidden-sm"><spring:message code="nav.scoreboard"/></span> <b class="caret"></b>
                </t:emptyLink>
                <table class="dropdown-menu main-dropdown-submenu">
                    <%@ include file="/WEB-INF/views/includes/topMenu/scoreboardSubmenu.jsp" %>
                </table>
            </li>
            <c:if test="${util:questModuleEnabled(contest)}">
                <li id="main-menu-quest" class="${sideMenuActive eq 'quest' ? 'active' : '' } dropdown">
                    <t:emptyLink isDropdown="true" styleClass="dropdown-toggle">
                        <t:glyphIcon icon="screenshot" /> <span class="hidden-xs hidden-sm"><spring:message code="nav.quest"/></span> <b class="caret"></b>
                    </t:emptyLink>
                    <table class="dropdown-menu main-dropdown-submenu">
                        <%@ include file="/WEB-INF/views/includes/topMenu/questSubmenu.jsp" %>
                    </table>
                </li>
            </c:if>
            <c:if test="${util:galleryModuleEnabled(contest)}">
                <li id="main-menu-gallery" class="${sideMenuActive eq 'gallery' ? 'active' : '' }">
                    <a href="<spring:url value="${contestURL}/gallery" />">
                        <t:glyphIcon icon="camera" /> <span class="hidden-xs hidden-sm"><spring:message code="nav.gallery"/></span>
                    </a>
                </li>
            </c:if>
            <c:if test="${util:pollModuleEnabled(contest)}">
                <li class="${sideMenuActive eq 'poll' ? 'active' : '' }">
                    <a href="<spring:url value="${contestURL}/polls" />">
                        <t:glyphIcon icon="bullhorn" /> <span class="hidden-xs hidden-sm"><spring:message code="nav.polls"/></span>
                    </a>
                </li>
            </c:if>
            <c:if test="${util:rssModuleEnabled(contest)}">
                <li class="${sideMenuActive eq 'rss' ? 'active' : '' }">
                    <a href="<spring:url value="${contestURL}/blog" />">
                        <t:faIcon icon="rss" /> <span class="hidden-xs hidden-sm"><spring:message code="nav.rss"/></span>
                    </a>
                </li>
            </c:if>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li>
                <a href="javascript:void(0)">
                    <span id="notification-counter" class="label ${featuredNotificationsCount == 0 ? 'label-default' : 'label-danger'} notification-counter">
                        ${featuredNotificationsCount}
                    </span>
                </a>
            </li>
        </ul>
        <p class="navbar-text navbar-right contest-time hidden-xs" title="<spring:message code="contest.time.hint" />">
            <span class="glyphicon glyphicon-time"></span> <span class="time_holder"></span>
        </p>
        <ul class="nav navbar-nav navbar-right">
            <li class="hidden">
                <t:emptyLink id="scorebord-notification-btn">
                    <t:glyphIcon icon="bell" /> <span class="hidden-xs hidden-sm text"><spring:message code="scoreboard.notifications"/></span>
                </t:emptyLink>
            </li>
        </ul>
    </div>
</nav>

<div id="featured-notification-container"></div>