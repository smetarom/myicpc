<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="href" %>
<%@ attribute name="onclick" %>
<%@ attribute name="type" %>
<%@ attribute name="id" %>
<%@ attribute name="context" %>
<%@ attribute name="styleClass" %>

<c:set var="context" value="${(empty context) ? 'default' : context}" />

<c:if test="${empty href}">
    <c:set var="type" value="${(empty type) ? 'button' : type}" />
    <button type="${type}"
            class="btn btn-${context} ${styleClass}"
            ${empty id ? '' : 'id="'.concat(id).concat('"')}
            ${empty onclick ? '' : 'onclick="'.concat(onclick).concat('"')}>
        <jsp:doBody/>
    </button>
</c:if>
<c:if test="${not empty href}">
    <a href="<spring:url value="${href}" />"
       class="btn btn-${context} ${styleClass}"
        ${empty id ? '' : 'id="'.concat(id).concat('"')}
        ${empty onclick ? '' : 'onclick="'.concat(onclick).concat('"')}>
        <jsp:doBody/>
    </a>
</c:if>