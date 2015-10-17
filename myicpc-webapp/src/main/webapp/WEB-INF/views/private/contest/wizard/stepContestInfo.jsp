<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<c:if test="${not empty contest.code and not empty contest.webServiceSettings.wsCMToken}">
    <div class="alert alert-info" role="alert">
        <spring:message code="contestAdmin.sync.hint"/>&nbsp;<a href="#" id="synchronize-cm-contest" class="alert-link"><spring:message
            code="contestAdmin.sync.link"/></a>
    </div>
</c:if>

<t:springInput id="contestName" labelCode="contest.name" path="name" required="true"/>
<t:springInput type="email" labelCode="contest.email" path="contestSettings.email" required="true" hintCode="contest.email.hint"/>
<t:springInput id="contestShortName" labelCode="contest.shortName" path="shortName" required="true"/>
<t:springInput labelCode="contest.startTime" path="startTime" id="startTime" required="true"
               hintCode="contest.startTime.hint"/>
<t:springInput type="number" labelCode="contest.timeDifference" path="timeDifference" id="contestTimeDifference"
               required="true" hintCode="contest.timeDifference.hint"/>
<t:springCheckbox path="hidden" labelCode="contest.hidden" styleClass="checkboxSwitch" hintCode="contest.hidden.hint" />
<div class="form-group">
    <label class="col-sm-3 control-label"><spring:message code="contest.showTeamNames"/>:* </label>

    <div class="col-sm-9">
        <label for="showTeamNamesTrue" class="normal-label">
            <form:radiobutton path="showTeamNames" id="showTeamNamesTrue" value="true"/>
            &nbsp;<spring:message code="contest.showTeamNames.hint.1"/>
        </label>
        <br/>
        <label for="showTeamNamesFalse" class="normal-label">
            <form:radiobutton path="showTeamNames" id="showTeamNamesFalse" value="false"/>
            &nbsp;<spring:message code="contest.showTeamNames.hint.2"/>
        </label>
    </div>
</div>
<t:springInput id="contestHashtag" labelCode="contest.hashtag" path="hashtag" required="true" hintCode="contest.hashtag.hint"/>

<script type="text/javascript">
    $(function () {
        $('#startTime').datetimepicker(datePickerOptions);

        $('#synchronize-cm-contest').click(function () {
            $.getJSON('<spring:url value="/private/contest/cm-contest-details" />', function(data) {
                $('#contestName').val(data.name);
                $('#contestShortName').val(data.shortName);
                $('#startTime').val(data.startDate);
                $('#contestTimeDifference').val(data.offset);
                $('#contestHashtag').val(data.hashtag);
                if (data.isWorldFinals) {
                    $('#showTeamNamesFalse').prop('checked', true);
                }
            })
        });
    });
</script>