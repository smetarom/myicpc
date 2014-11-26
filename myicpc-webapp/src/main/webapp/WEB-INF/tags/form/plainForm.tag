<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="action" required="true" %>
<%@ attribute name="styleClass" %>
<%@ attribute name="fileUpload" type="java.lang.Boolean" %>

<form class="form-horizontal ${styleClass}" method="post" action="<spring:url value="${action}"/>"  ${fileUpload ? 'enctype="multipart/form-data"' : ''}>
    <jsp:doBody/>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>