<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="url" required="true" %>
<%@ attribute name="labelCode" %>

<a href="<spring:url value="${url}" />" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-pencil"></span>
    <spring:message code="${empty labelCode ? 'edit' : labelCode}"/></a>
