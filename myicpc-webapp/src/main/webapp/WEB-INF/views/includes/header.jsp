<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<nav id="top-menu-nav" class="navbar navbar-inverse navbar-fixed-top desktop">
    <div class="navbar-header">
        <div class="navbar-brand">
            <a href="<spring:url value="${contestURL}" />" id="homepage-link"><span class="fa fa-home"></span> <spring:message
                    code="app.name"/></a>
        </div>
    </div>

    <ul id="top-sub-menu" class="nav navbar-nav">
        <c:if test="${util:scheduleModuleEnabled(contest)}">
            <li class="${sideMenuActive eq 'schedule' ? 'active' : '' } dropdown">
                <t:emptyLink isDropdown="true" styleClass="dropdown-toggle"><span class="text"><span
                        class="glyphicon glyphicon-calendar"></span> <spring:message code="nav.schedule"/></span> <b
                        class="caret"></b></t:emptyLink>
                <table class="dropdown-menu main-dropdown-submenu">
                    <%@ include file="/WEB-INF/views/includes/topMenu/scheduleSubmenu.jsp" %>
                </table>
            </li>
        </c:if>
        <li id="main-menu-scoreboard" class="${sideMenuActive eq 'scoreboard' ? 'active' : '' } dropdown">
            <t:emptyLink isDropdown="true" styleClass="dropdown-toggle"><span class="glyphicon glyphicon-list"></span>
                <span class="text"><spring:message code="nav.scoreboard"/></span> <b class="caret"></b></t:emptyLink>
            <table class="dropdown-menu main-dropdown-submenu">
                <%@ include file="/WEB-INF/views/includes/topMenu/scoreboardSubmenu.jsp" %>
            </table>
        </li>
        <c:if test="${util:questModuleEnabled(contest)}">
            <li id="main-menu-quest" class="${sideMenuActive eq 'quest' ? 'active' : '' } dropdown">
                <t:emptyLink isDropdown="true" styleClass="dropdown-toggle"><span
                        class="glyphicon glyphicon-screenshot"></span> <span class="text"><spring:message
                        code="nav.quest"/></span> <b class="caret"></b></t:emptyLink>
                <table class="dropdown-menu main-dropdown-submenu">
                    <%@ include file="/WEB-INF/views/includes/topMenu/questSubmenu.jsp" %>
                </table>
            </li>
        </c:if>
        <c:if test="${util:galleryModuleEnabled(contest)}">
            <li id="main-menu-gallery" class="${sideMenuActive eq 'gallery' ? 'active' : '' }"><a
                    href="<spring:url value="/gallery" />"><span class="glyphicon glyphicon-camera"></span> <span
                    class="text"
                    ><spring:message code="nav.gallery"/></span></a>
            </li>
        </c:if>
        <c:if test="${util:pollModuleEnabled(contest)}">
            <li class="${sideMenuActive eq 'poll' ? 'active' : '' }"><a
                    href="<spring:url value="${contestURL}/polls" />"><span
                    class="glyphicon glyphicon-bullhorn"></span> <span class="text"><spring:message
                    code="nav.polls"
                    /></span></a>
            </li>
        </c:if>
        <c:if test="${util:rssModuleEnabled(contest)}">
            <li class="${sideMenuActive eq 'rss' ? 'active' : '' }"><a href="<spring:url value="${contestURL}/rss" />"><span
                    class="fa fa-rss"></span> <span class="text"><spring:message code="nav.rss"/></span></a>
            </li>
        </c:if>
    </ul>
    <ul class="nav navbar-nav navbar-right">
        <li class="dropdown">
            <t:emptyLink isDropdown="true" styleClass="dropdown-toggle">
                <span class="glyphicon glyphicon-share-alt"></span> <span class="hidden-xs hidden-sm"><spring:message
                    code="share"/></span>
            </t:emptyLink>
            <ul class="dropdown-menu">
                <%@ include file="/WEB-INF/views/includes/header_share.jsp" %>
            </ul>
        </li>
    </ul>
    <p class="navbar-text navbar-right contest-time hidden-xs" title="<spring:message code="contest.time.hint" />">
        <span class="glyphicon glyphicon-time"></span> <span class="time_holder"></span>
    </p>
    <ul class="nav navbar-nav navbar-right">
        <li class="hidden">
            <t:emptyLink id="scorebord-notification-btn"><span class="glyphicon glyphicon-bell"></span> <span
                    class="hidden-xs hidden-sm text"><spring:message
                    code="scoreboard.notifications"/></span></t:emptyLink>
        </li>
    </ul>
</nav>

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

        var topMenuWidth = $("#top-menu-nav").width();
        if (topMenuWidth < 850) {
            $("#top-sub-menu .text").hide();
        }

        var timediff = ${empty contestTime ? 0 : contestTime};

        function contestTime() {
            $(".time_holder").html(formatContestTime(timediff));
            timediff += 60;
        }

        contestTime();
        if (timediff < 18000) {
            setInterval(contestTime, 60 * 1000);
        }
    });
</script>