<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdminWithSubmenu>
    <jsp:attribute name="title">
		<spring:message code="rssAdmin.title" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="rssAdmin.title" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
        <li class="active"><spring:message code="rssAdmin.title" /></li>
	</jsp:attribute>

    <jsp:attribute name="controls">
        <a href='<spring:url value="/private${contestURL}/rss/refresh"/>' class="btn btn-default btn-hover"><span class="glyphicon glyphicon-refresh"></span> <spring:message code="rssAdmin.refreshAll" /></a>
    </jsp:attribute>

    <jsp:attribute name="submenu">
        <h4><spring:message code="rssAdmin.create.title" /></h4>

        <t:form action="" entity="rssFeed">
            <jsp:attribute name="controls">
                <t:button type="submit" context="primary"><spring:message code="rssAdmin.create.btn" /></t:button>
            </jsp:attribute>
            <jsp:body>
                <t:springInput path="name" labelCode="rssFeed.name" />
                <t:springInput path="url" labelCode="rssFeed.url" />
            </jsp:body>
        </t:form>
    </jsp:attribute>

    <jsp:body>
        <table class="table table-striped">
            <thead>
            <tr>
                <th><spring:message code="rssFeed.name" /></th>
                <th><spring:message code="rssFeed.url" /></th>
                <th class="text-center"><spring:message code="rssFeed.numMessages" /></th>
                <th class="text-center"><spring:message code="rssFeed.enabled" /></th>
                <th class="text-center"><spring:message code="rssFeed.lastPulledDate" /></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="rssFeed" items="${rssFeeds}">
                <tr>
                    <td><a href="<spring:url value="/private${contestURL}/rss/${rssFeed.id}" />">${rssFeed.name}</a></td>
                    <td>${rssFeed.url}</td>
                    <td class="text-center"><a href="<spring:url value="/private${contestURL}/rss/${rssFeed.id}" />">${rssFeed.messageCount}</a></td>
                    <td class="text-center"><spring:message code="${rssFeed.disabled ? 'no' : 'yes' }" /></td>
                    <td class="text-center"><fmt:formatDate type="both" value="${rssFeed.lastPulledDate}" /></td>
                    <td class="text-right nowrap">
                        <a href="<spring:url value="/private${contestURL}/rss/${rssFeed.id}/refresh"/>" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-refresh"></span> <spring:message code="refresh" /></a>
                        <a href="<spring:url value="/private${contestURL}/rss/${rssFeed.id}/activate"/>" class="btn btn-default btn-xs">
                            <c:choose>
                                <c:when test="${rssFeed.disabled}"><span class="glyphicon glyphicon-ok"></span> <spring:message code="activate" /></c:when>
                                <c:otherwise><span class="glyphicon glyphicon-ban-circle"></span> <spring:message code="disable" /></c:otherwise>
                            </c:choose>
                        </a>
                        <t:deleteButton url="/private${contestURL}/rss/${rssFeed.id}/delete" confirmMessageCode="rssAdmin.delete.confirm" confirmMessageArgument="${rssFeed.name}" />
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </jsp:body>
</t:templateAdminWithSubmenu>