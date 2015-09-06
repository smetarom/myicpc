<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<p><spring:message code="contest.instagram.hint"/></p>

<t:springInput id="instagramKey" labelCode="contest.instagram.apiKey" path="webServiceSettings.instagramKey"/>
<t:springInput labelCode="contest.instagram.apiSecret" path="webServiceSettings.instagramSecret"/>

<p>
    <t:button id="instagramCheck" context="warning" onclick="">
        <spring:message code="contest.instagram.checkWS" />
    </t:button>
    <span id="instagramCheckResult"></span>
</p>

<script type="application/javascript">
    $('#instagramCheck').click(function() {
        var data = {
            instagramClientId: $('#instagramKey').val()
        };
        $.post('<spring:url value="/social/check/instagram" />', data, function(data) {
            $('#instagramCheckResult').html(data);
        });
    });
</script>