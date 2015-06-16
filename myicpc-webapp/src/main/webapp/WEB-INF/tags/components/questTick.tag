<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="state" required="true" %>
<%@ attribute name="note" required="true" %>

<c:choose>
    <c:when test="${state eq 'ACCEPTED'}">
        <spring:message var="title" code="quest.leaderboard.accepted" />
        <t:faIcon icon="check" style="color: green" title="${title}"/>
    </c:when>
    <c:when test="${state eq 'PENDING'}">
        <spring:message var="title" code="quest.leaderboard.pending" />
        <t:faIcon icon="clock-o" style="color: orange" title="${title}"/>
    </c:when>
    <c:when test="${state eq 'REJECTED'}">
        <spring:message var="title" code="questAdmin.submissions.reject.reason" />
        <t:glyphIcon icon="remove" style="color: red" title="${title}: ${note}" />
    </c:when>
</c:choose>
