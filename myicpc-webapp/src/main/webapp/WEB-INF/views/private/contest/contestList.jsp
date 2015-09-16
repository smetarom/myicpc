<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateGeneralAdmin>
	<jsp:attribute name="title">
		<spring:message code="contestAdmin.list.title" />
	</jsp:attribute>

  <jsp:attribute name="headline">
		<spring:message code="contestAdmin.list.title" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="contestAdmin.list.title" /></li>
	</jsp:attribute>

	<jsp:attribute name="controls">
      <sec:authorize access="hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')">
        <a href="<spring:url value="/private/contest/create"/>" class="btn btn-hover btn-default"><t:glyphIcon icon="plus"/> <spring:message code="homeAdmin.contest.create"/></a>
      </sec:authorize>
	</jsp:attribute>

  <jsp:body>
    <c:if test="${empty contests}">
        <div class="no-items-available">
          <spring:message code="contestAdmin.list.noResult" />
        </div>
    </c:if>
    <c:if test="${not empty contests}">
        <table class="table table-striped">
          <thead>
          <tr>
            <th><spring:message code="contest" /></th>
            <th><spring:message code="contest.code" /></th>
            <th><spring:message code="contest.startDate" /></th>
            <th></th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="contest" items="${contests}">
            <tr>
              <td><a href="<spring:url value="/private/${contest.code}" />">${contest.name}</a></td>
              <td>${contest.code}</td>
              <td><fmt:formatDate value="${contest.startTime}" type="both"/></td>
              <td class="text-right">
                <t:editButton url="/private/${contest.code}/edit" />
                <t:deleteButton url="/private/${contest.code}/delete" />
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
    </c:if>
  </jsp:body>
</t:templateGeneralAdmin>