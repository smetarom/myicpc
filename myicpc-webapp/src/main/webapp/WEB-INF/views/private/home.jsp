<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateGeneralAdmin>
	<jsp:attribute name="title">
		<spring:message code="dashboard"/>
	</jsp:attribute>

    <jsp:body>
        <div class="col-md-6 col-sm-12">
            <t:panelWithHeading showBody="false">
                <jsp:attribute name="heading"><spring:message code="homeAdmin.contests"/></jsp:attribute>
                <jsp:attribute name="table">
                    <c:if test="${not empty contests}">
                        <table class="table">
                            <thead>
                            <tr>
                                <th><spring:message code="contest"/></th>
                                <th class="text-right"><spring:message code="contest.startDate"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="contest" items="${contests}">
                                <tr>
                                    <td><a href="<spring:url value="/private/${contest.code}" />">${contest.name} </a></td>
                                    <td class="text-right"><fmt:formatDate value="${contest.startTime}" type="both"/></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                </jsp:attribute>
                <jsp:attribute name="footer">
                    <a href="<spring:url value="/private/contest/create"/>" class="btn btn-primary"><t:glyphIcon icon="plus"/> <spring:message
                            code="homeAdmin.contest.create"/></a>
                </jsp:attribute>
                <jsp:body>
                    <c:if test="${empty contests}">
                        <div class="no-items-available">
                            <spring:message code="homeAdmin.contest.noAvailable" />
                        </div>
                    </c:if>
                </jsp:body>
            </t:panelWithHeading>
        </div>

        <div class="col-md-6 col-sm-12">
            <t:panelWithHeading panelStyle="warning">
                <jsp:attribute name="heading"><spring:message code="warningAdmin.title"/></jsp:attribute>
                <jsp:body>
                    <c:if test="${empty warnings}">
                        <div class="no-items-available text-success">
                            <t:faIcon icon="thumbs-o-up" /> <spring:message code="warningAdmin.noWarnings" />
                        </div>
                    </c:if>
                </jsp:body>
            </t:panelWithHeading>
        </div>

        <script type="application/javascript">
            $(function () {
                $(".breadcrumb").hide();
                $(".page-header").hide();
            })
        </script>
    </jsp:body>
</t:templateGeneralAdmin>