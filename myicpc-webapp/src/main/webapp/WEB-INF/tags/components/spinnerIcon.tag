<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="id" required="true" %>
<%@ attribute name="size" type="java.lang.Integer" %>
<%@ attribute name="hidden" type="java.lang.Boolean" %>

<c:set var="context" value="${(empty size) ? 12 : size}" />

<span id="${id}" class="fa fa-spinner fa-spin" style="font-size: ${size}px; ${hidden ? 'display: none;' : ''}"></span>
