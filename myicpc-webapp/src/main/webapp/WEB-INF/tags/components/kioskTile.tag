<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<%@ attribute name="notificationCode" required="true" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="icon" %>
<%@ attribute name="imageURL" %>
<%@ attribute name="imageTitle" %>
<%@ attribute name="styleClass" %>
<%@ attribute name="useDefaultBody" type="java.lang.Boolean" %>


<div ng-switch-when="${notificationCode}">
    <div class="tile-title ${styleClass}">
        <c:if test="${not empty icon}">
            <div class="pull-right ${icon}"></div>
        </c:if>
        <c:if test="${not empty imageURL}">
            <img alt="${imageTitle}" src="${imageURL}" width="48" height="48"/>
        </c:if>
        ${title}
    </div>
    <c:choose>
        <c:when test="${useDefaultBody}">
            <p class="tile-detail" ng-bind-html="trustedHTML(notification.body)"></p>
        </c:when>
        <c:otherwise>
            <div class="tile-detail">
                <jsp:doBody/>
            </div>
        </c:otherwise>
    </c:choose>
</div>
