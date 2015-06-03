<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<table class="table table-striped table-condensed table-hover">
    <tbody>
        <t:labelTableRow label="contest.code" style="width:250px;">${contest.code}</t:labelTableRow>
        <t:labelTableRow label="contest.wsToken">${contest.webServiceSettings.wsCMToken}</t:labelTableRow>
        <t:labelTableRow label="contest.name">${contest.name}</t:labelTableRow>
        <t:labelTableRow label="contest.shortName">${contest.shortName}</t:labelTableRow>
        <t:labelTableRow label="contest.startTime"><fmt:formatDate value="${contest.startTime}" /></t:labelTableRow>
        <t:labelTableRow label="contest.timeDifference">${contest.timeDifference} <spring:message code="minute.shortcut" /></t:labelTableRow>
        <t:labelTableRow label="contest.showTeamNames"><t:tick condition="${contest.showTeamNames}" /></t:labelTableRow>
        <t:labelTableRow label="contest.hashtag">${contest.hashtag}</t:labelTableRow>
    </tbody>
</table>

<hr />
<table class="table table-striped table-condensed table-hover">
    <tbody>
        <t:labelTableRow label="contest.eventFeedURL" style="width:250px;">${contest.contestSettings.eventFeedURL}</t:labelTableRow>
        <t:labelTableRow label="contest.eventFeedUsername" rendered="${not empty contest.contestSettings.eventFeedUsername}">${contest.contestSettings.eventFeedUsername}</t:labelTableRow>
        <t:labelTableRow label="contest.eventFeedPassword" rendered="${not empty contest.contestSettings.eventFeedPassword}">${contest.contestSettings.eventFeedPassword}</t:labelTableRow>
        <t:labelTableRow label="contest.scoreboardStrategy"><spring:message code="${contest.contestSettings.scoreboardStrategyType.code}" text="${contest.contestSettings.scoreboardStrategyType.label}" /></t:labelTableRow>
    </tbody>
</table>


