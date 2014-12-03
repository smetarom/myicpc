<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="helpTextCode" required="true" %>
<%@ attribute name="size" type="java.lang.Double" %>
<%@ attribute name="placement" %>

<c:set var="size" value="${(empty size) ? 1.5 : size}" />
<c:set var="placement" value="${(empty placement) ? 'right' : placement}" />

<span class="fa fa-info-circle help-icon" style="font-size: ${size * 100}%" data-toggle="tooltip" data-placement="${placement}" title="<spring:message code="${helpTextCode}" />"></span>
