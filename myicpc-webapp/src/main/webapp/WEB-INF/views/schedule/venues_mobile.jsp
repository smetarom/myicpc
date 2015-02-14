<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:template>
    <jsp:attribute name="title">
        <spring:message code="venues" />
    </jsp:attribute>

    <jsp:attribute name="headline">
        <spring:message code="venues" />
    </jsp:attribute>

	<jsp:body>
		<div class="clearfix">
            <jsp:include page="/WEB-INF/views/schedule/fragment/venuesTable.jsp">
                <jsp:param name="isMobile" value="true" />
            </jsp:include>
		</div>
    </jsp:body>
</t:template>