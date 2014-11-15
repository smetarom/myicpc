<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>
    <jsp:attribute name="breadcrumb">
		  <li class="active"><spring:message code="contestAdmin.delete.breadcrumb"/></li>
	</jsp:attribute>

    <jsp:attribute name="headline">
        <spring:message code="contestAdmin.delete.title" arguments="${contest.name}"/>
    </jsp:attribute>

    <jsp:body>
        <t:alert context="warning" style="color: black;">
            <spring:message code="contestAdmin.delete.hint" arguments="${contest.name}"/>

            <t:form action="/private${contestURL}/delete" entity="contest" cancelFormURL="/private${contestURL}" style="margin: 0 20px;">
                <jsp:attribute name="controls">
                    <t:button type="submit" context="danger"><spring:message code="contestAdmin.delete.btn"/></t:button>
                </jsp:attribute>
            </t:form>
        </t:alert>


    </jsp:body>
</t:templateAdmin>