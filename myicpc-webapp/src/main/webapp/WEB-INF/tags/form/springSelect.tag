<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="path" required="true" %>
<%@ attribute name="labelCode" required="true" %>
<%@ attribute name="options" required="true" type="java.util.List" %>
<%@ attribute name="itemLabel" %>
<%@ attribute name="itemValue" %>
<%@ attribute name="type" %>
<%@ attribute name="hintCode" %>
<%@ attribute name="previewWiki" type="java.lang.Boolean" %>
<%@ attribute name="required" type="java.lang.Boolean" %>
<%@ attribute name="defaultValue" type="java.lang.Boolean" %>
<%@ attribute name="defaultValueCode" %>

<div class="form-group">
    <form:label path="${path}" class="col-sm-3 control-label">
        <spring:message code="${labelCode}"/>:${required ? '*' : ''} </form:label>
    <div class="col-sm-9">
        <form:select path="${path}" class="form-control">
            <c:if test="${defaultValue}">
                <form:option value="${null}"><spring:message
                        code="${empty defaultValueCode ? 'selectOption' : defaultValueCode}"/></form:option>
            </c:if>
            <form:options items="${options}" itemValue="${itemValue}" itemLabel="${itemLabel}"/>
        </form:select>
    </div>
    <div class="col-sm-offset-3 col-sm-9">
        <c:if test="${not empty hintCode}">
            <spring:message code="${hintCode}"/>
        </c:if>
        <c:if test="${previewWiki}">
            <%@ include file="/WEB-INF/views/includes/help/wikiHelp.jsp" %>
        </c:if>
        <form:errors path="${path}" cssClass="formError"/>
    </div>
</div>
