<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="util" uri="http://myicpc.baylor.edu/functions" %>

<%@ attribute name="hasthags" %>
<%@ attribute name="url" %>
<%@ attribute name="text" %>
<%@ attribute name="linkClass" %>

<c:url value="https://twitter.com/intent/tweet" var="twitterURL">
    <c:param name="hashtags" value="${hasthags}"/>
    <c:param name="text" value="${util:escapeHTML(text)}"/>
    <c:param name="url" value="${url}"/>
</c:url>

<a href="${twitterURL}" class="${linkClass}">
    <jsp:doBody/>
</a>