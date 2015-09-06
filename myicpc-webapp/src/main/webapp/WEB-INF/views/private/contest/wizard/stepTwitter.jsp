<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<p><spring:message code="contest.twitter.hint"/></p>

<t:springInput id="twitterConsumerKey" labelCode="contest.twitterConsumerKey" path="webServiceSettings.twitterConsumerKey"/>
<t:springInput id="twitterConsumerSecret" labelCode="contest.twitterConsumerSecret" path="webServiceSettings.twitterConsumerSecret"/>
<t:springInput id="twitterAccessToken" labelCode="contest.twitterAccessToken" path="webServiceSettings.twitterAccessToken"/>
<t:springInput id="twitterAccessTokenSecret" labelCode="contest.twitterAccessTokenSecret" path="webServiceSettings.twitterAccessTokenSecret"/>

<p>
    <t:button id="twitterCheck" context="warning" onclick="">
        <spring:message code="contest.twitter.checkWS" />
    </t:button>
    <span id="twitterCheckResult"></span>
</p>

<script type="application/javascript">
    $('#twitterCheck').click(function() {
        var data = {
            twitterConsumerKey: $('#twitterConsumerKey').val(),
            twitterConsumerSecret: $('#twitterConsumerSecret').val(),
            twitterAccessToken: $('#twitterAccessToken').val(),
            twitterAccessTokenSecret: $('#twitterAccessTokenSecret').val()
        };
        $.post('<spring:url value="/social/check/twitter" />', data, function(data) {
            $('#twitterCheckResult').html(data);
        });
    });

</script>

