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
<t:springCheckbox path="contestSettings.showRegion" labelCode="contest.showRegion" styleClass="checkboxSwitch" hintCode="contest.showRegion.hint" />
<t:springCheckbox path="contestSettings.showUniversity" labelCode="contest.showUniversity" styleClass="checkboxSwitch" hintCode="contest.showUniversity.hint" />
<t:springCheckbox path="contestSettings.showCountry" labelCode="contest.showCountry" styleClass="checkboxSwitch" hintCode="contest.showCountry.hint" />

<script type="application/javascript">
    function hideJSONUrlField() {
        var selected = $("#contestSettings\\.scoreboardStrategyType").val();
        if (selected === "JSON") {
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
