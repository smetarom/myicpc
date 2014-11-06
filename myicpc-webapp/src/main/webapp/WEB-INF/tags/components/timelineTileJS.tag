<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<%@ attribute name="id" required="true" %>
<%@ attribute name="headerIcon" %>
<%@ attribute name="headerImage" %>
<%@ attribute name="headerImageTitle" %>
<%@ attribute name="footerLabel" required="true" %>
<%@ attribute name="footerLink" %>
<%@ attribute name="onlyRetweet" type="java.lang.Boolean" %>
<%@ attribute name="facebookTitle" %>
<%@ attribute name="facebookBody" %>
<%@ attribute name="facebookThumbnail" %>
<%@ attribute name="facebookURL" %>
<%@ attribute name="twitterHashtags" %>
<%@ attribute name="twitterText" %>
<%@ attribute name="twitterURL" %>
<%@ attribute name="featured" type="java.lang.Boolean" %>
<%@ attribute name="bannable" type="java.lang.Boolean" %>

<script id="${id}" type="text/x-jquery-tmpl">
<div class="timelineTile clearfix {{#if featured}}featuredTimelineTile{{/if}} notif-{{id}}" {{#if featured}}onclick="$(this).find('.media-content').slideToggle();"{{/if}}>
	<t:timelineAdminControlJS bannable="${bannable}"/>

	{{#if featured}}
		<div class="pull-right">
			<button type="button" class="close" onclick="$(this).parent().parent().find('.media-content').slideToggle(); event.stopPropagation();" aria-hidden="true" style="float: none; font-size: 1.1em;"><span class="glyphicon glyphicon-eye-open"></span></button> &middot;
			<a href="javascript:void(0)" onclick="appendIdToCookieArray('ignoreFeaturedNotifications', {{id}}, '${ctx}');$(this).parent().parent().hide();event.stopPropagation();" class="close" aria-hidden="true" style="float: none;">&times;</a>
		</div>
	{{/if}}
	
	<c:if test="${not empty headerIcon}">
    <div class="pull-left media-object ${headerIcon}" style="font-size: 2em; padding-top: 10px;"></div>
</c:if>
    <c:if test="${not empty headerImage}">
        <img class="pull-left media-object" src="<spring:url
            value="${headerImage}"/>" alt="${empty headerImageTitle ? '{{title}}' : headerImageTitle}" width="50" height="50">
    </c:if>
	<div class="media-body">

    <jsp:doBody/>

            <div class="footer">
                <c:if test="${not empty footerLink}">
        <a href="${empty footerLink ? 'javascript:void(0)' : footerLink}" target="_blank"><span class="glyphicon glyphicon-log-out"></span>
    </c:if>
    ${footerLabel}
    <c:if test="${not empty footerLink}">
        </a>
    </c:if>
			&middot; 
		    <span class="hidden-xs">{{timestamp}} &middot;</span> 
		    <c:if test="${onlyRetweet}">
        <a href="https://twitter.com/intent/retweet?tweet_id={{code.tweetId}}"><span class="glyphicon glyphicon-retweet"></span>
        <spring:message code="retweet"/></a>
    </c:if>
    <c:if test="${not onlyRetweet}">
        <c:if test="${empty facebookURL}">
            <a href="javascript:shareFacebook('${facebookTitle}', '${facebookBody}', '${facebookThumbnail}')"><i class="fa fa-facebook-square"></i>
            <spring:message code="facebook"/> </a> &middot;
        </c:if>
        <c:if test="${not empty facebookURL}">
            <a href="javascript:javascript:shareFacebookWithURL('${facebookTitle}', '${facebookBody}', '${facebookURL}', '${facebookThumbnail}')"><i class="fa fa-facebook-square"></i>
            <spring:message code="facebook"/> </a> &middot;
        </c:if>
        <a href="https://twitter.com/intent/tweet?hashtags=${twitterHashtags}&amp;url=${twitterURL}&amp;text=${twitterText}"><i class="fa fa-twitter"></i>
        <spring:message code="twitter"/></a>
    </c:if>
		</div>
	</div>
</div>

</script>
