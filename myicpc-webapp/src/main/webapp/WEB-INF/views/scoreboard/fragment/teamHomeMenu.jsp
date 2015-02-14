<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:secondLevelSubmenu isMobile="${sitePreference.mobile}">
    <t:menuItem activeItem="contest" active="${tab}" url="${contestURL}/team/${team.externalId}"><spring:message code="teamHome.nav.contest" /></t:menuItem>
    <t:menuItem activeItem="profile" active="${tab}" url="${contestURL}/team/${team.externalId}/profile"><spring:message code="teamHome.nav.about" /></t:menuItem>
    <t:menuItem activeItem="insight" active="${tab}" url="${contestURL}/team/${team.externalId}/insight"><spring:message code="teamHome.nav.insight" /></t:menuItem>
    <t:menuItem activeItem="social" active="${tab}" url="${contestURL}/team/${team.externalId}/social"><spring:message code="teamHome.nav.social" /></t:menuItem>
</t:secondLevelSubmenu>
