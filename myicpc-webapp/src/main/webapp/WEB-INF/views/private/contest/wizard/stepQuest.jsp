<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:springInput labelCode="contest.quest.hashtagPrefix" path="questConfiguration.hashtagPrefix" hintCode="contest.quest.hashtagPrefix.hint" />
<t:springInput labelCode="contest.quest.pointsForVote" path="questConfiguration.pointsForVote" hintCode="contest.quest.pointsForVote.hint" type="number" />
<t:springInput labelCode="contest.quest.instructionUrl" path="questConfiguration.instructionUrl" hintCode="contest.quest.instructionUrl.hint" />
<t:springInput id="questDeadline" labelCode="contest.quest.deadline" path="questConfiguration.localDeadline" hintCode="contest.quest.deadline.hint" />

<script type="text/javascript">
    $(function () {
        $('#questDeadline').datetimepicker(datePickerOptions);
    });
</script>

