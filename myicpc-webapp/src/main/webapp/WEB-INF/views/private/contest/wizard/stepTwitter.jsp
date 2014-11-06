<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<p><spring:message code="contest.twitter.hint"/></p>

<t:springInput labelCode="contest.twitterConsumerKey" path="webServiceSettings.twitterConsumerKey"/>
<t:springInput labelCode="contest.twitterConsumerSecret" path="webServiceSettings.twitterConsumerSecret"/>
<t:springInput labelCode="contest.twitterAccessToken" path="webServiceSettings.twitterAccessToken"/>
<t:springInput labelCode="contest.twitterAccessTokenSecret" path="webServiceSettings.twitterAccessTokenSecret"/>

