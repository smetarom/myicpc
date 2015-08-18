<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateHomepage>
    <jsp:body>
        <div class="page-header text-center home-page-header">
            <h1>MyICPC</h1>
        </div>
        <br/>
        <div class="container contest-container">
            <c:forEach var="contest" items="${contests}">
                <a href="<spring:url value="/${contest.code}"/>" class="display-block col-md-4 col-sm-6" >
                    <div class="panel panel-default contest-panel">
                        <div class="panel-body text-center">
                            <t:faIcon icon="trophy" />
                            <p>${contest.name}</p>
                            <hr class="divider" />
                            <fmt:formatDate var="contestStartTime" value="${contest.startTime}" type="both" timeStyle="short" />
                            <spring:message code="home.contest.startAt" arguments="${contestStartTime}" argumentSeparator=";" />
                        </div>
                    </div>
                </a>
            </c:forEach>
        </div>
    </jsp:body>

</t:templateHomepage>
