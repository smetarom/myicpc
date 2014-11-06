<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="path" required="true" %>
<%@ attribute name="labelCode" required="true" %>
<%@ attribute name="id" %>
<%@ attribute name="type" %>
<%@ attribute name="required" type="java.lang.Boolean" %>
<%@ attribute name="hintCode" %>
<%@ attribute name="previewWiki" type="java.lang.Boolean" %>

<div class="form-group">
    <form:label path="${path}" class="col-sm-3 control-label">
        <spring:message code="${labelCode}"/>:${required ? '*' : ''} </form:label>
    <div class="col-sm-9">
        <c:if test="${required}">
            <form:input path="${path}" type="${type}" class="form-control" id="${id}" required="required"/>
        </c:if>
        <c:if test="${not required}">
            <form:input path="${path}" type="${type}" class="form-control" id="${id}"/>
        </c:if>
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
