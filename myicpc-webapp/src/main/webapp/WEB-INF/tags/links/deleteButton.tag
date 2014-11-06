<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="url" required="true" %>
<%@ attribute name="labelCode" %>
<%@ attribute name="hideLabel" type="java.lang.Boolean" %>
<%@ attribute name="confirmMessageCode" %>
<%@ attribute name="confirmMessageArgument" %>

<c:if test="${empty confirmMessageCode}">
    <a href="<spring:url value="${url}" />" class="btn btn-default btn-xs"><span
            class="glyphicon glyphicon-trash"></span> <span class="${hideLabel ? 'hidden' : ''}"><spring:message
            code="${empty labelCode ? 'delete' : labelCode}"
            /></span></a>
</c:if>
<c:if test="${not empty confirmMessageCode}">
    <a href="<spring:url value="${url}" />"
       onclick="return confirm('<spring:message code="${confirmMessageCode}" arguments="${confirmMessageArgument}"/>');"
       class="btn btn-default btn-xs"><span
            class="glyphicon glyphicon-trash"
            ></span> <span class="${hideLabel ? 'hidden' : ''}"><spring:message
            code="${empty labelCode ? 'delete' : labelCode}"/></span></a>
</c:if>
