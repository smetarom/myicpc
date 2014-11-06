<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="activeItem" required="true" %>
<%@ attribute name="active" required="true" %>
<%@ attribute name="url" required="true" %>
<%@ attribute name="styleClass" %>
<%@ attribute name="disabled" type="java.lang.Boolean" %>

<li class="${active eq activeItem ? 'active' : ''} ${disabled ? 'disabled' : ''} ${styleClass}"><a
        href="<spring:url value="${url}" />">
    <jsp:doBody/>
</a></li>
