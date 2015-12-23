<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<table class="table table-striped">
  <thead>
  <tr>
    <th><spring:message code="errorMessage.timestamp" /></th>
    <th><spring:message code="errorMessage.cause" /></th>
    <th><spring:message code="errorMessage.message" /></th>
  </tr>
  </thead>
  <tbody>
  <c:forEach items="${errors}" var="error">
    <tr id="${error.id}">
      <td>
        <fmt:formatDate value="${error.timestamp}" type="both" />
      </td>
      <td>
          ${error.cause.name}
      </td>
      <td>
          ${error.message}
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>