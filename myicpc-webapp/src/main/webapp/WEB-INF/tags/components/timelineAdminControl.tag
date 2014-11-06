<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="bannable" type="java.lang.Boolean" %>

<sec:authorize access="hasRole('ROLE_NOTIFICATION')">
    <div class="text-right">
        <spring:message code="adminNotification.suspicious.delete.confirm" var="deleteConfirm"/>
        <spring:message code="adminNotification.suspicious.ban.confirm" var="banConfirm"/>
        <t:emptyLink onclick="if (confirm('${deleteConfirm}')) { deleteNotification(this, ${notification.id}) }"><span
                class="glyphicon glyphicon-remove"></span> <spring:message code="adminNotification.suspicious.delete"/></t:emptyLink>
        <c:if test="${bannable}">
            <t:emptyLink onclick="if (confirm('${banConfirm}')) { banNotification(this, ${notification.id}) }"><span
                    class="glyphicon glyphicon-ban-circle "></span> <spring:message
                    code="adminNotification.suspicious.ban"/></t:emptyLink>
        </c:if>
    </div>
</sec:authorize>