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
                <td>${person.fullname}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>