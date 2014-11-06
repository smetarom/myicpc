<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateGeneralAdmin>
	<jsp:attribute name="headline">
		<spring:message code="dashboard"/>
	</jsp:attribute>

    <jsp:body>
        <div class="col-md-6 col-sm-12">
            <div class="panel panel-default">
                <div class="panel-heading"><h3 class="panel-title"><spring:message code="homeAdmin.contests"/></h3>
                </div>
                <table class="table">
                    <thead>
                    <tr>
                        <th><spring:message code="contest"/></th>
                        <th class="text-right"><spring:message code="contest.startDate"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><a href="<spring:url value="/private/${contest.code}" />">${contest.name} </a></td>
                        <td class="text-right"><fmt:formatDate value="${contest.startTime}" type="both"/></td>
                    </tr>
                    </tbody>
                </table>
                <div class="panel-footer text-right">
                    <a href="<spring:url value="/private/contest/create"/>" class="btn btn-primary"><span
                            class="glyphicon glyphicon-plus"></span> <spring:message
                            code="homeAdmin.createContest"/></a>
                </div>
            </div>
        </div>

        <script type="application/javascript">
            $(function () {
                $(".breadcrumb").hide();
                $(".page-header").hide();
            })
        </script>
    </jsp:body>
</t:templateGeneralAdmin>