<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="action" required="true" %>
<%@ attribute name="entity" required="true" %>
<%@ attribute name="vertical" type="java.lang.Boolean" %>
<%@ attribute name="resetFormButton" type="java.lang.Boolean" %>
<%@ attribute name="cancelFormURL" %>
<%@ attribute name="style" %>

<%@attribute name="controls" fragment="true" %>

<spring:url var="formAction" value="${action}"/>
<form:form class="form-horizontal" role="form" action="${formAction}" commandName="${entity}" style="${style}">
    <jsp:doBody/>

    <div class="form-group text-right">
        <c:if test="${resetFormButton}">
            <button type="reset" class="btn btn-hover btn-default"><spring:message code="reset"/></button>
        </c:if>
        <c:if test="${not empty cancelFormURL}">
            <a href="<spring:url value="${cancelFormURL}"/>" class="btn btn-hover btn-default"><spring:message code="cancel"/></a>
        </c:if>
        <jsp:invoke fragment="controls" />
    </div>
</form:form>