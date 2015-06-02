<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<footer id="footer">
    <a id="feedbackLink" class="btn btn-info pull-right btn-xs">
        <t:faIcon icon="envelope-o" /> <spring:message code="feedback"/>
    </a>

    <div id="share-container">
        <a href="http://www.twitter.com/icpcnews" title="http://www.twitter.com/icpcnews" target="_blank">
            <t:faIcon icon="twitter-square" />
        </a> &middot;
        <a href="http://www.facebook.com/icpcnews" title="http://www.facebook.com/icpcnews" target="_blank">
            <t:faIcon icon="facebook-square" />
        </a> &middot;
        <a href="http://www.youtube.com/user/ICPCNews" title="http://www.youtube.com/user/ICPCNews" target="_blank">
            <t:faIcon icon="youtube" />
        </a> &middot;
        <a href="http://www.instagram.com/icpcnews" title="http://www.instagram.com/icpcnews" target="_blank">
            <t:faIcon icon="instagram" />
        </a>
    </div>
</footer>

<div id="feedbackWrapper"></div>