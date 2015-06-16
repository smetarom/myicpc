<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:secondLevelSubmenu isMobile="${sitePreference.mobile}">
    <c:if test="${not empty teamContestId}">
        <t:menuItem activeItem="contest" active="${tab}" url="${contestURL}/team/teamContestId}"><spring:message code="teamHome.nav.contest" /></t:menuItem>
    </c:if>
    <c:if test="${not empty teamPresentationId}">
        <t:menuItem activeItem="profile" active="${tab}" url="${contestURL}/team/${teamPresentationId}/profile"><spring:message code="teamHome.nav.about" /></t:menuItem>
    </c:if>
    <c:if test="${not empty teamContestId}">
        <t:menuItem activeItem="insight" active="${tab}" url="${contestURL}/team/${teamContestId}/insight"><spring:message code="teamHome.nav.insight" /></t:menuItem>
    </c:if>
    <c:if test="${not empty teamPresentationId}">
        <t:menuItem activeItem="social" active="${tab}" url="${contestURL}/team/${teamPresentationId}/social"><spring:message code="teamHome.nav.social" /></t:menuItem>
    </c:if>
</t:secondLevelSubmenu>
