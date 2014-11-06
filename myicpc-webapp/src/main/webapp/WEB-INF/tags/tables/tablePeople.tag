<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="people" required="true" type="java.util.List" %>
<%@ attribute name="titleCode" %>

<c:if test="${not empty people}">
    <c:if test="${not empty titleCode}">
        <h4>
            <spring:message code="${titleCode}"/>
        </h4>
    </c:if>
    <table class="table">
        <c:forEach var="person" items="${people}">
            <tr>
                <td><a href='<spring:url value="/person/${person.id}" />'>${person.fullname}</a></td>
            </tr>
        </c:forEach>
    </table>
</c:if>