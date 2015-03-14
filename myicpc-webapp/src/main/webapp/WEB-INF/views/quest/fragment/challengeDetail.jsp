<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<c:if test="${not hideTitle}">
    <h2 style="margin-top: 0">${challenge.name} <small>#${challenge.hashtag}</small></h2>
</c:if>

<div class="col-sm-12">
    <div class="center-block" style="max-width: 500px">
        <p>${challenge.description}</p>
    </div>
    <div class="text-center">
        <t:button context="primary" modalOpenId="participateInChallenge" onclick="showParticipateChallenge('${challenge.hashtag}', '${challenge.name}')">
            <spring:message code="quest.participateThisChallenge" />
        </t:button>
    </div>
</div>
<br class="clear"/>
<br/>
<div class="col-sm-4 quest-submissions">
    <h4><t:glyphIcon icon="ok" /><spring:message code="quest.status.accepted" /></h4>
    <c:set var="submissions" value="${acceptedSubmissions}" />
    <%@ include file="/WEB-INF/views/quest/fragment/challengeSubmissionList.jsp" %>
</div>
<div class="col-sm-4 quest-submissions">
    <h4><t:glyphIcon icon="time" /><spring:message code="quest.status.pending" /></h4>

    <c:set var="submissions" value="${pendingSubmissions}" />
    <%@ include file="/WEB-INF/views/quest/fragment/challengeSubmissionList.jsp" %>
</div>
<div class="col-sm-4 quest-submissions">
    <h4><t:glyphIcon icon="remove" /><spring:message code="quest.status.rejected" /></h4>

    <c:set var="submissions" value="${rejectedSubmissions}" />
    <%@ include file="/WEB-INF/views/quest/fragment/challengeSubmissionList.jsp" %>
</div>

<script type="application/javascript">
    function toggleSubmissionList(tableId) {
        $('#' + tableId + ' tr.additionalRow').toggle('slow');
    }
</script>

<%@ include file="/WEB-INF/views/quest/fragment/participateChallengeModal.jsp" %>
