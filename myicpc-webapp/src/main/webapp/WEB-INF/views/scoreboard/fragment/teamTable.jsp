<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<c:if test="${not empty people}">
    <c:if test="${not empty param.titleCode}">
        <h4>
            <spring:message code="${param.titleCode}" />
        </h4>
    </c:if>
    <table class="table table-striped">
        <c:forEach var="person" items="${people}">
            <tr>
                <td><a href="${contestURL}/people/${person.id}">${person.fullname}</a></td>
            </tr>
        </c:forEach>
    </table>
</c:if>