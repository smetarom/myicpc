<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="url" required="true" %>
<%@ attribute name="icon" required="true" %>
<%@ attribute name="labelCode" required="true" %>

<a href="<spring:url value="${url}" />" class="block"><span class="${icon}"></span><br/> <spring:message
        code="${labelCode}"/></a>