<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="formaction" required="true" %>
<%@ attribute name="formmethod" %>
<%@ attribute name="onclick" %>
<%@ attribute name="id" %>
<%@ attribute name="context" %>
<%@ attribute name="styleClass" %>

<c:set var="context" value="${(empty context) ? 'default' : context}" />

<button type="submit"
        class="btn btn-${context} ${styleClass}"
        formaction="<c:url value="${formaction}" />"
        ${empty formmethod ? 'post' : 'id="'.concat(formmethod).concat('"')}
        ${empty id ? '' : 'id="'.concat(id).concat('"')}
        ${empty onclick ? '' : 'onclick="'.concat(onclick).concat('"')}>
    <jsp:doBody/>
</button>
