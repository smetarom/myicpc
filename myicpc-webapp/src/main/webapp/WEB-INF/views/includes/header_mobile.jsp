<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav id="mobile-top-menu" class="navbar navbar-inverse navbar-fixed-top mobile" style="z-index: 1200">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span class="glyphicon glyphicon-share-alt"> <spring:message code="share"/></span>
        </button>
        <a class="navbar-brand" href='<spring:url value="${contestURL}/" />'><span class="fa fa-home"></span> <spring:message
                code="app.name"/></a>
    </div>

    <div class="collapse navbar-collapse navbar-ex1-collapse">
        <ul class="nav navbar-nav">
            <%@ include file="/WEB-INF/views/includes/topMenu/socialShare.jsp" %>
        </ul>
    </div>
</nav>

<nav id="mobile-submenu" class="navbar navbar-default">
    <table style="width: 100%; text-align: center;">
        <tbody>
        <tr>
            <c:if test="${util:scheduleModuleEnabled(contest)}">
                <td class="${sideMenuActive eq 'schedule' ? 'active' : '' }">
                    <t:emptyLink id="main-schedule-link" styleClass="side-menu-schedule"><span
                            class="glyphicon glyphicon-calendar"></span></t:emptyLink>
                </td>
            </c:if>
            <td class="${sideMenuActive eq 'scoreboard' ? 'active' : '' }">
                <t:emptyLink id="main-scoreboard-link" styleClass="side-menu-scoreboard"><span
                        class="glyphicon glyphicon-list"></span></t:emptyLink>
            </td>
            <c:if test="${util:questModuleEnabled(contest)}">
                <td class="${sideMenuActive eq 'quest' ? 'active' : '' }">
                    <t:emptyLink id="main-quest-link" styleClass="side-menu-quest"><span
                            class="glyphicon glyphicon-screenshot"></span></t:emptyLink>
                </td>
            </c:if>
            <c:if test="${util:galleryModuleEnabled(contest)}">
                <td class="${sideMenuActive eq 'gallery' ? 'active' : '' }">
                    <a id="main-gallery-link" href="<spring:url value="${contestURL}/gallery" />" class="side-menu-gallery"><span
                            class="glyphicon glyphicon-camera"
                            ></span></a>
                </td>
            </c:if>
            <td><t:emptyLink id="main-misc-link" styleClass="side-menu-misc"><span
                    class="fa fa-ellipsis-v"></span></t:emptyLink>
        </tr>
        </tbody>
    </table>
</nav>

<div id="main-schedule-submenu" style="display: none;">
    <table class="width100 text-center mobile-submenu">
        <%@ include file="/WEB-INF/views/includes/topMenu/scheduleSubmenu.jsp" %>
    </table>
</div>

<div id="main-scoreboard-submenu" style="display: none;">
    <table class="width100 text-center mobile-submenu">
        <%@ include file="/WEB-INF/views/includes/topMenu/scoreboardSubmenu.jsp" %>
    </table>
</div>

<div id="main-quest-submenu" style="display: none;">
    <table class="width100 text-center mobile-submenu">
        <%@ include file="/WEB-INF/views/includes/topMenu/questSubmenu.jsp" %>
    </table>
</div>

<div id="main-misc-submenu" style="display: none;">
    <table class="width100 text-center mobile-submenu">
        <tr>
            <td>
                <c:if test="${util:pollModuleEnabled(contest)}">
                    <t:topSubmenuLink labelCode="nav.polls" url="/polls" icon="glyphicon glyphicon-bullhorn"/>
                </c:if>
            </td>
            <td>
                <c:if test="${util:rssModuleEnabled(contest)}">
                    <t:topSubmenuLink labelCode="nav.rss" url="/rss" icon="fa fa-rss"></t:topSubmenuLink>
                </c:if>
            </td>
        </tr>
        <tr>
            <td><t:topSubmenuLink labelCode="nav.settings" url="/settings" icon="glyphicon glyphicon-cog"/></td>
            <td class="mobile-submenu">
                <t:emptyLink onclick="$('#main-misc-submenu').hide();" modalId="feedbackModal"><span
                        class="fa fa-pencil-square-o"></span><br/><spring:message code="feedback"/></t:emptyLink>
            </td>
        </tr>
    </table>
</div>