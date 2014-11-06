<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="bannable" type="java.lang.Boolean" %>

<sec:authorize access="hasRole('ROLE_NOTIFICATION')">
    <div class="text-right timeline-control">
        <a href="javascript:void(0)" onclick="if (confirm('<spring:message
                code="adminNotification.suspicious.delete.confirm"/>')) { deleteNotification(this, {{id}}) }"><span
                class="glyphicon glyphicon-remove"
                ></span> <spring:message code="adminNotification.suspicious.delete"/></a>
        <c:if test="${bannable}">
            <a href="javascript:void(0)" onclick="if (confirm('<spring:message
                    code="adminNotification.suspicious.ban.confirm"/>')) { banNotification(this, {{id}}) }">
                <span class="glyphicon glyphicon-ban-circle "></span> <spring:message
                    code="adminNotification.suspicious.ban"/>
            </a>
        </c:if>
    </div>
</sec:authorize>