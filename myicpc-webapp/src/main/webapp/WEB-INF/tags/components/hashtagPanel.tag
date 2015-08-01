<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="id" required="true" %>
<%@ attribute name="contestURL" required="true" %>
<%@ attribute name="hashtag1" required="true" %>
<%@ attribute name="hashtag2" %>

<div class="input-group">
    <input id="tweetText${id}" type="text" class="form-control" placeholder="<spring:message code="hashtagPanel.twitter.placeholder" arguments="${hashtag1},${hashtag2}" />">
    <span class="input-group-btn">
        <a href="http://twitter.com/intent/tweet?hashtags=${hashtag1},${hashtag2}&text="
           onclick="appendInputValueToLinkHref(this, $('#tweetText${id}'));" class="btn btn-primary">
            <span class="fa fa-twitter"></span> Tweet
        </a>
    </span>
</div>
or use <a href="https://instagram.com/"><span class="fa fa-instagram"></span> Instagram</a>
and <a href="https://vine.co/"><span class="fa fa-vine"></span> Vine</a>
<hr class="divider"/>

<div id="hashtagPanelContainer${id}"></div>

<script type="application/javascript">
    $(function() {
        $.get("<spring:url value="${contestURL}/notification/hashtag-panel" />",
                {hashtag1: "%${hashtag1}%", hashtag2: "%${hashtag2}%"},
                function(data) {
                    $("#hashtagPanelContainer${id}").html(data);
                }
        );
    })
</script>


