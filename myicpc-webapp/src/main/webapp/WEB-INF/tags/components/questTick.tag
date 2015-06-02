<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<%@ attribute name="state" required="true" %>
<%@ attribute name="note" required="true" %>

<c:choose>
    <c:when test="${state eq 'ACCEPTED'}">
        <t:faIcon icon="check" style="color: green"/>
    </c:when>
    <c:when test="${state eq 'PENDING'}">
        <span style="color: orange"><t:faIcon icon="clock-o"/></span>
    </c:when>
    <c:when test="${state eq 'REJECTED'}">
        <span style="color: red"><t:glyphIcon icon="remove"/></span>
    </c:when>
</c:choose>
