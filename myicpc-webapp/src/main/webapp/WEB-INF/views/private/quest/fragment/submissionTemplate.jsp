<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<div class="text-center">
    <h4>${submission.submissionState}</h4>
    <strong>
        <a href="<spring:url value="/quest/challenges#${submission.challenge.hashtag}" />">${submission.challenge.name}</a>
        <br />
        ${submission.participant.teamMember.fullname}
    </strong>
</div>
<hr class="divider" />
<t:questSubmission submission="${submission}" />