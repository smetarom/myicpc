<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>
    <jsp:attribute name="breadcrumb">
		  <li class="active"><spring:message code="contestHomeAdmin.title"/></li>
	</jsp:attribute>

    <jsp:body>

        <div class="text-right">
            <t:button href="/private${contestURL}/edit" styleClass="btn-hover"><t:glyphIcon icon="pencil" /> <spring:message code="edit" /></t:button>
        </div>
        <br/>

        <div class="col-md-6 col-sm-12">
            <t:panelWithHeading>
                <jsp:attribute name="heading"><spring:message code="contestHomeAdmin.info"/></jsp:attribute>
                <jsp:body>
                    <table>
                        <tbody>
                            <t:labelTableRow label="contest">${contest.name}</t:labelTableRow>
                            <t:labelTableRow label="contest.code">${contest.code}</t:labelTableRow>
                            <t:labelTableRow label="contest.startTime"><fmt:formatDate value="${contest.startTime}" type="both"/></t:labelTableRow>
                        </tbody>
                    </table>
                    <div class="text-right">
                        <t:button href="/private${contestURL}/overview"><spring:message code="contestHomeAdmin.info.btn" /></t:button>
                    </div>
                </jsp:body>
            </t:panelWithHeading>
            <t:panelWithHeading>
                <jsp:attribute name="heading"><spring:message code="contestHomeAdmin.feedControl"/></jsp:attribute>

            </t:panelWithHeading>
        </div>
        <div class="col-md-6 col-sm-12">
            <t:panelWithHeading panelStyle="warning">
                <jsp:attribute name="heading"><spring:message code="warningAdmin.title"/></jsp:attribute>
                <jsp:body>
                    <c:if test="${empty warnings}">
                        <div class="no-items-available text-success">
                            <t:faIcon icon="thumbs-o-up"/> <spring:message code="warningAdmin.noWarnings"/>
                        </div>
                    </c:if>
                </jsp:body>
            </t:panelWithHeading>
        </div>
    </jsp:body>
</t:templateAdmin>