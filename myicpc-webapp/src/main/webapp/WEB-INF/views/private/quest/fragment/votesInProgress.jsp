<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<div class="clearfix">
    <c:forEach var="submission" items="${inProgressVoteSubmissions}">
        <div class="col-sm-3">
            <div class="thumbnail">
                <%@ include file="/WEB-INF/views/private/quest/fragment/submissionTemplate.jsp"%>
                <hr class="divider" />
                <p class="text-center">
                    <strong>${submission.votes} <spring:message code="quest.votes" /></strong>
                </p>
            </div>
        </div>
    </c:forEach>
    <c:if test="${empty inProgressVoteSubmissions}">
        <div class="no-items-available">
            <spring:message code="questAdmin.votes.inProgress.noResult" />
        </div>
    </c:if>
</div>