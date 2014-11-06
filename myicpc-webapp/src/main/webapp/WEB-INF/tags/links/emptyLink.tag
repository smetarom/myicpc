<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="id" %>
<%@ attribute name="modalId" %>
<%@ attribute name="isDropdown" type="java.lang.Boolean" %>
<%@ attribute name="style" %>
<%@ attribute name="styleClass" %>
<%@ attribute name="onclick" %>
<%@ attribute name="ngclick" %>

<a href="javascript:void(0)"
${empty id ? '' : 'id="'.concat(id).concat('"')}
${empty modalId ? '' : 'data-toggle="modal" data-target="#'.concat(modalId).concat('"')}
${empty onclick ? '' : 'onclick="'.concat(onclick).concat('"')}
${empty ngclick ? '' : 'ng-click="'.concat(ngclick).concat('"')}
${isDropdown ? 'data-toggle="dropdown"' : '' }
   class="${styleClass}"
   style="${style}">
    <jsp:doBody/>
</a>
