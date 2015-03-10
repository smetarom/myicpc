<div class="clearfix">
    <c:forEach var="submission" items="${winnerVoteSubmissions}" varStatus="status">
        <div class="col-sm-4">
            <div class="thumbnail">
                <%@ include file="/WEB-INF/views/private/quest/fragment/submissionTemplate.jsp"%>
                <hr class="divider" />
                <p class="text-center">
                    <strong>${submission.votes} <spring:message code="quest.votes" /></strong>
                </p>
            </div>
        </div>
        <c:if test="${util:isNLine(status, 2, 3)}">
            <br class="clear" />
        </c:if>
    </c:forEach>
    <c:if test="${empty winnerVoteSubmissions}">
        <div class="no-items-available">
            <spring:message code="questAdmin.votes.winners.noResult" />
        </div>
    </c:if>
</div>