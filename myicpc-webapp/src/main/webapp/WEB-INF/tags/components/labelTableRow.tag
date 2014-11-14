<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="label" required="true" %>

<tr>
    <th style="min-width: 150px;"><spring:message code="${label}"/>:</th>
    <td><jsp:doBody /></td>
</tr>


