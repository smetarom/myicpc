<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="href" %>
<%@ attribute name="onclick" %>
<%@ attribute name="type" %>
<%@ attribute name="id" %>
<%@ attribute name="context" %>
<%@ attribute name="styleClass" %>
<%@ attribute name="modalOpenId" %>
<%@ attribute name="dataDismiss" type="java.lang.Boolean" %>
<%@ attribute name="disabled" type="java.lang.Boolean" %>

<c:set var="context" value="${(empty context) ? 'default' : context}" />

<c:if test="${empty modalOpenId}">
    <c:if test="${empty href}">
        <c:set var="type" value="${(empty type) ? 'button' : type}" />
        <button type="${type}"
                class="btn btn-${context} ${styleClass}"
                ${empty id ? '' : 'id="'.concat(id).concat('"')}
                ${dataDismiss ? 'data-dismiss="modal"' : ''}
                ${disabled ? 'disabled="disabled"' : ''}
                ${empty onclick ? '' : 'onclick="'.concat(onclick).concat('"')}>
            <jsp:doBody/>
        </button>
    </c:if>
    <c:if test="${not empty href}">
        <a href="<spring:url value="${href}" />"
           class="btn btn-${context} ${styleClass}"
            ${empty id ? '' : 'id="'.concat(id).concat('"')}
            ${empty onclick ? '' : 'onclick="'.concat(onclick).concat('"')}
            ${disabled ? 'disabled="disabled"' : ''}>
            <jsp:doBody/>
        </a>
    </c:if>
</c:if>

<c:if test="${not empty modalOpenId}">
    <button type="button" class="btn btn-${context} ${styleClass}" data-toggle="modal"
            data-target="#${modalOpenId}"
            ${disabled ? 'disabled="disabled"' : ''}
            ${empty onclick ? '' : 'onclick="'.concat(onclick).concat('"')}>
        <jsp:doBody />
    </button>
</c:if>