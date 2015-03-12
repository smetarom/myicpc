<div class="alert alert-info quest-info" role="alert">
    <c:if test="${not empty contest.questConfiguration.instructionUrl}">
        <spring:message code="quest.info.instructions" arguments="${contest.questConfiguration.instructionUrl}" />
        <br/>
    </c:if>
    <span class="hidden-xs">
        <spring:message code="quest.info.icpcSetup.long" arguments="https://icpc.baylor.edu/private/profile/myicpc" />
    </span>
    <span class="visible-xs">
        <spring:message code="quest.info.icpcSetup" arguments="https://icpc.baylor.edu/private/profile/myicpc" />
    </span>
</div>