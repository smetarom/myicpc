<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:springCheckbox path="moduleConfiguration.codeInsightModule" labelCode="module.codeInsight" styleClass="moduleSwitch" />
<t:springInput labelCode="contest.editActivityURL" path="contestSettings.editActivityURL"
               hintCode="contest.editActivityURL.hint"/>
<script type="application/javascript">
    $(function() {
        toogleModuleInputs($("input.moduleSwitch"));
        $(".bootstrap-switch").click(function() {
            toogleModuleInputs($(this).find("input.moduleSwitch"));
        });
    })
</script>
