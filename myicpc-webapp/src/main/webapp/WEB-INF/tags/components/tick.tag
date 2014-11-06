<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="condition" required="true" type="java.lang.Boolean" %>

<c:choose>
    <c:when test="${condition}">
        <span class="glyphicon glyphicon-ok"></span>
    </c:when>
    <c:otherwise>
        <span class="glyphicon glyphicon-remove"></span>
    </c:otherwise>
</c:choose>
