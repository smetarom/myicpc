<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<table class="table table-striped table-hover table-menu">
    <tbody>
    <c:forEach var="challenge" items="${challenges}">
        <tr>
            <td>
                <c:if test="${param.isMobile eq 'true'}">
                    <a href="<spring:url value="${contestURL}/quest/challenge/${challenge.id}" />">
                        <strong>${challenge.name}</strong>
                        <div class="challenge-info-small no-link">#${challenge.hashtag} &middot; <spring:message code="Xpoints" arguments="${challenge.defaultPoints}" /></div>
                    </a>
                </c:if>
                <c:if test="${param.isMobile ne 'true'}">
                    <a href="#${challenge.hashtag}" onclick="loadChallengeContent(${challenge.id})">
                        <strong>${challenge.name}</strong>
                        <div class="challenge-info-small no-link">#${challenge.hashtag} &middot; <spring:message code="Xpoints" arguments="${challenge.defaultPoints}" /></div>
                    </a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>