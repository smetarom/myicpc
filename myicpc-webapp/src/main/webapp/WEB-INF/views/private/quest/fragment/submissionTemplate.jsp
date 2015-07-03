<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<div class="text-center">
    <h4>${submission.submissionState}</h4>
    <strong>
        <a href="<spring:url value="${contestURL}/quest/challenges#${submission.challenge.hashtag}" />">${submission.challenge.name}</a>
        <br />
        <a href="<spring:url value="${contestURL}/people/${submission.participant.contestParticipant.id}" />">${submission.participant.contestParticipant.fullname}</a>
    </strong>
</div>
<hr class="divider" />
<t:questSubmission submission="${submission}" />