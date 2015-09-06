<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<p><spring:message code="contest.vine.hint"/></p>

<t:springInput id="vineEmail" labelCode="contest.vine.email" path="webServiceSettings.vineEmail"/>
<t:springInput id="vinePassword" labelCode="contest.vine.password" path="webServiceSettings.vinePassword"/>

<p>
    <t:button id="vineCheck" context="warning" onclick="">
        <spring:message code="contest.twitter.checkWS" />
    </t:button>
    <span id="vineCheckResult"></span>
</p>

<script type="application/javascript">
    $('#vineCheck').click(function() {
        var data = {
            vineUsername: $('#vineEmail').val(),
            vinePassword: $('#vinePassword').val()
        };
        $.post('<spring:url value="/social/check/vine" />', data, function(data) {
            $('#vineCheckResult').html(data);
        });
    });
</script>