<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:springInput labelCode="contest.eventFeedURL" path="contestSettings.eventFeedURL" required="true"
               hintCode="contest.eventFeedURL.hint"/>
<t:springInput labelCode="contest.eventFeedUsername" path="contestSettings.eventFeedUsername"
               hintCode="contest.eventFeedUsername.hint"/>
<t:springInput labelCode="contest.eventFeedPassword" path="contestSettings.eventFeedPassword"
               hintCode="contest.eventFeedUsername.hint"/>
<t:springSelect labelCode="contest.scoreboardStrategy" path="contestSettings.scoreboardStrategyType"
                options="${scoreboardStrategies}" itemValue="name" itemLabel="label" required="true"/>
<div id="JSONUrlField">
    <t:springInput labelCode="contest.JSONScoreboardURL" path="contestSettings.JSONScoreboardURL"
                   hintCode="contest.JSONScoreboardURL.hint"/>
</div>

<script type="application/javascript">
    function hideJSONUrlField() {
        var selected = $("#contestSettings\\.scoreboardStrategyType").val();
        if (selected === "UNSORTED_JSON" || selected === "SORTED_JSON") {
            $("#JSONUrlField").show();
        } else {
            $("#JSONUrlField").hide();
        }
    }
    $(function () {
        hideJSONUrlField();
        $("#contestSettings\\.scoreboardStrategyType").change(hideJSONUrlField);
    });
</script>
