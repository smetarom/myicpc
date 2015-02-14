<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<li><a href="https://twitter.com/intent/tweet?hashtags=${contest.hashtag}"><i class="fa fa-twitter"></i> <spring:message code="twitter" /></a></li>
<li><a href="javascript:shareFacebookUrl(document.URL)"><i class="fa fa-facebook-square"></i> <spring:message code="facebook" /></a></li>
<li><a href="javascript:shareGoogleUrl(document.URL)"><i class="fa fa-google-plus-square"></i> <spring:message code="google" /></a></li>
<li><a href="https://vine.co/"><i class="fa fa-vimeo-square"></i> <spring:message code="vine" /></a></li>
<li><a href="http://instagram.com/"><i class="fa fa-instagram"></i> <spring:message code="instagram" /></a></li>