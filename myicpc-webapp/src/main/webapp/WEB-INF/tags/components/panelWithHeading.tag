<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="panelStyle" %>
<%@ attribute name="showBody" type="java.lang.Boolean" %>

<%@attribute name="heading" fragment="true" %>
<%@attribute name="table" fragment="true" %>
<%@attribute name="footer" fragment="true" %>

<c:set var="panelStyle" value="${(empty panelStyle) ? 'default' : panelStyle}" />
<c:set var="showBody" value="${(empty showBody) ? true : showBody}" />

<div class="panel panel-${panelStyle}">
    <div class="panel-heading">
        <h3 class="panel-title"><jsp:invoke fragment="heading"/></h3>
    </div>
    <c:if test="${showBody}">
        <div class="panel-body">
            <jsp:doBody/>
        </div>
    </c:if>
    <jsp:invoke fragment="table"/>
    <c:if test="${not empty footer}">
        <div class="panel-footer">
            <jsp:invoke fragment="footer"/>
        </div>
    </c:if>
</div>
