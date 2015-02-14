<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="isMobile" type="java.lang.Boolean" %>

<c:set var="isMobile" value="${(empty isMobile) ? false : isMobile}" />

<div class="second-level-submenu ${isMobile ? 'responsive-second-level-submenu' : ''}">
    <nav class="navbar navbar-default top-submenu ${isMobile ? 'mobile' : ''}">
        <ul class="nav navbar-nav">
            <jsp:doBody />
        </ul>
    </nav>
</div>

<c:if test="${isMobile}">
    <script type="application/javascript">
        calcSubmenuWidth = function () {
            var totalWidth = 5;
            var elem = $(this);
            $(".top-submenu .navbar-nav > li").each(function() {
                if ($(this).is(elem)) {
                    console.log($(this).find(".dropdown-menu:first"));
                    console.log($(this).find(".dropdown-menu:first"));
                    console.log($(this).find(".dropdown-menu:first").width());
                    totalWidth += $(this).find(".dropdown-menu:first").width();
                } else {
                    totalWidth += $(this).width();
                }
            });
            if ($("#bodyContainer").width() > totalWidth) {
                totalWidth = $("#bodyContainer").width();
            }
            $(".top-submenu").width(totalWidth);
            console.log(totalWidth)
        };

        $(function() {
            calcSubmenuWidth();
            $(".top-submenu .navbar-nav > li").click(calcSubmenuWidth);
        });
    </script>
</c:if>