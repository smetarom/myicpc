<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="titleCode" required="true" %>
<%@ attribute name="placement" %>

<c:set var="placement" value="${(empty placement) ? 'bottom' : placement}" />

<span data-toggle="tooltip" data-placement="${placement}" title="<spring:message code="${titleCode}" />"><jsp:doBody /></span>
