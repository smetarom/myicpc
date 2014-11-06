<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="path" required="true" %>
<%@ attribute name="labelCode" required="true" %>
<%@ attribute name="options" required="true" type="java.util.List" %>
<%@ attribute name="optionMap" required="true" type="java.util.Map" %>
<%@ attribute name="type" %>
<%@ attribute name="hintCode" %>
<%@ attribute name="previewWiki" type="java.lang.Boolean" %>

<div class="form-group">
    <form:label path="${path}" class="col-sm-3 control-label">
        <spring:message code="${labelCode}"/>:${required ? '*' : ''} </form:label>
    <div class="col-sm-9">
        <form:select path="${path}" class="form-control" multiple="true">
            <c:forEach var="item" items="${options}">
                <c:if test="${not empty optionMap[item.id]}">
                    <form:option value="${item.id}" label="${item}" selected="selected"/>
                </c:if>
                <c:if test="${empty optionMap[item.id]}">
                    <form:option value="${item.id}" label="${item}"/>
                </c:if>
            </c:forEach>
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
