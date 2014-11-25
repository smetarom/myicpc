<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="label" required="true" %>
<%@ attribute name="rendered" type="java.lang.Boolean" %>
<%@ attribute name="style" %>
<%@ attribute name="styleClassBody" %>

<c:if test="${empty rendered or rendered}">
    <tr>
        <th style="min-width: 150px; padding-right: 10px; ${style}"><spring:message code="${label}"/>:</th>
        <td class="${styleClassBody}"><jsp:doBody /></td>
    </tr>
</c:if>


