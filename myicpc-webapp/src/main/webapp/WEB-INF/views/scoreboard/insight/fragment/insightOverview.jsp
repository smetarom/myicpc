<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<table class="table">
    <thead>
    <tr>
        <th style="width: 165px"><spring:message code="insight.dashboard.numProblems" /></th>
        <c:forEach items="${data.currentTeamResult}" var="result">
            <th class="text-center">${result.label}</th>
        </c:forEach>
    </tr>
    <tr>
        <td><spring:message code="insight.dashboard.teamsSolved" /></td>
        <c:forEach items="${data.currentTeamResult}" var="result">
            <td class="text-center">${result.solved}</td>
        </c:forEach>
    </tr>
    <tr>
        <td><spring:message code="insight.dashboard.teamsAttempted" /></td>
        <c:forEach items="${data.currentTeamResult}" var="result">
            <td class="text-center">${result.submitted}</td>
        </c:forEach>
    </tr>
    </thead>
</table>

<table class="table">
    <thead>
        <tr>
            <th style="width: 165px"><spring:message code="insight.dashboard.problems" /></th>
            <c:forEach items="${data.currentProblemResult}" var="result">
                <th>${result.label}</th>
            </c:forEach>
        </tr>
        <tr>
            <td><spring:message code="insight.dashboard.teamsSolved" /></td>
            <c:forEach items="${data.currentProblemResult}" var="result">
                <td>${result.solved}</td>
            </c:forEach>
        </tr>
        <tr>
            <td><spring:message code="insight.dashboard.teamsAttempted" /></td>
            <c:forEach items="${data.currentProblemResult}" var="result">
                <td>${result.submitted}</td>
            </c:forEach>
        </tr>
    </thead>
</table>