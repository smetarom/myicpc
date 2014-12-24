<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<meta name="description"
      content="MyICPC provides a personalized view of an ICPC competition and encourages social engagement.  MyICPC has been used at regionals and World Finals."/>
<meta name="keywords"
      content="MyICPC, ICPC, ACM-ICPC, personalized view, programming competition, programming contest, ACM, World finals, regionals"/>
<meta name="author" content="Roman Smetana"/>
<meta name="application-name" content="MyICPC"/>

<link rel="Shortcut Icon" href="http://icpc.baylor.edu/img/icon-icpc-small.gif"/>

<%-- Public CDNs --%>
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.1.0/css/font-awesome.min.css" type="text/css">

<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.12/angular.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.12/angular-sanitize.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/modernizr/2.7.1/modernizr.min.js" defer></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js" defer></script>
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js" defer></script>
<script src="<c:url value="/js/jquery/jquery.atmosphere.min.js" />" defer></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.5.2/underscore-min.js" defer></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/handlebars.js/1.3.0/handlebars.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery.touchswipe/1.6.4/jquery.touchSwipe.min.js" defer></script>

<%-- Local resources --%>
<%--
<link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>" type="text/css">
<link rel="stylesheet" href="<c:url value='/css/font-awesome.min.css'/>" type="text/css">
<link rel="stylesheet" href="<c:url value='/css/nv.d3.min.css'/>" type="text/css">

<script src="<c:url value="/js/jquery/jquery.min.js" />"></script>
<script src="<c:url value="/js/angular/angular.min.js" />"></script>
<script src="<c:url value="/js/angular/angular-sanitize.min.js" />"></script>
<script src="<c:url value="/js/modernizr/modernizr.min.js" />" defer></script>
<script src="<c:url value="/js/jquery/jquery-ui.min.js" />" defer></script>
<script src="<c:url value="/js/bootstrap/bootstrap.min.js" />" defer></script>
<script src="<c:url value="/js/jquery/jquery.atmosphere.min.js" />" defer></script>
<script src="<c:url value="/js/underscore/underscore.min.js" />" defer></script>
<script src="<c:url value="/js/handlebars/handlebars.min.js" />"></script>
<script src="<c:url value="/js/jquery/jquery.touchSwipe.min.js" />" defer></script>
<script src="<c:url value="/js/d3/d3.min.js" />" defer></script>
<script src="<c:url value="/js/d3/nv.d3.min.js" />" defer></script>
--%>

<%-- MyICPC internal resources --%>
<link rel="stylesheet" href="<c:url value='/css/bootstrap-tour.min.css'/>" type="text/css">
<link rel="stylesheet" href="<c:url value='/css/myicpc/main.css'/>" type="text/css">
<link rel="stylesheet" href="<c:url value='/css/myicpc/style.css'/>" type="text/css">
<script src="<c:url value='/js/myicpc/functions.js'/>"></script>
<script src="<c:url value='/js/myicpc/myicpc-config.js'/>" defer></script>
<script src="<c:url value='/js/bootstrap/bootstrap-tour.min.js'/>" defer></script>

<!-- FACEBOOK -->
<script>window.fbAsyncInit = function () {
    FB.init({appId: "1418322161712690", xfbml: true, version: "v2.1"})
};
(function (e, a, f) {
    var c, b = e.getElementsByTagName(a)[0];
    if (e.getElementById(f)) {
        return
    }
    c = e.createElement(a);
    c.id = f;
    c.src = "//connect.facebook.net/en_US/sdk.js";
    b.parentNode.insertBefore(c, b)
}(document, "script", "facebook-jssdk"));</script>

<t:googleAnalytics/>

<!-- Twitter -->
<script>!function (d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/.test(d.location) ? 'http' : 'https';
    if (!d.getElementById(id)) {
        js = d.createElement(s);
        js.id = id;
        js.src = p + '://platform.twitter.com/widgets.js';
        fjs.parentNode.insertBefore(js, fjs);
    }
}(document, 'script', 'twitter-wjs');</script>
