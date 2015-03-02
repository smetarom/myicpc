<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
	<jsp:attribute name="headline">
		<spring:message code="rssAdmin.title" />: ${rssFeed.name }
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li><a href="<spring:url value="/private${contestURL}/rss" />"><spring:message code="rssAdmin.title" /></a></li>
		<li class="active">${rssFeed.name }</li>
	</jsp:attribute>

    <jsp:body>
        <div class="wrapper">
            <h3>
                <spring:message code="rssFeed.messages" />
            </h3>

            <table class="table table-striped">
                <thead>
                <tr>
                    <th><spring:message code="rssMessage.name" /></th>
                    <th><spring:message code="rssMessage.originalPost" /></th>
                    <th><spring:message code="rssMessage.postedOn" /></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="message" items="${feedMessages}">
                    <tr>
                        <td>${message.name}</td>
                        <td><a href="${message.url}">${message.url}</a></td>
                        <td><fmt:formatDate type="both" value="${message.publishDate}" /></td>
                        <td>
                            <t:deleteButton url="/private${contestURL}/rss/deleteMessage/${message.id}" hideLabel="true" confirmMessageCode="rssAdmin.message.delete.confirm" />
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

    </jsp:body>
</t:templateAdmin>