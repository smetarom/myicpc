<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="context" %>
<%@ attribute name="style" %>
<%@ attribute name="dismissible" type="java.lang.Boolean" %>

<c:set var="context" value="${(empty context) ? 'info' : context}" />

<div class="alert alert-${context}" role="alert" style="${style}">
  <c:if test="${dismissible}">
    <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
  </c:if>
  <jsp:doBody />
</div>