<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="labelCode" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="type" %>
<%@ attribute name="size" type="java.lang.Integer" %>
<%@ attribute name="required" type="java.lang.Boolean" %>

<label for="${id}"><spring:message code="${labelCode}"/>: </label>
<c:if test="${required}">
    <input type="${type}" class="form-control" id="${id}" size="${size}">
</c:if>
<c:if test="${not required}">
    <input type="${type}" class="form-control" id="${id}" size="${size}">
</c:if>
