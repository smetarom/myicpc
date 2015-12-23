<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="nav.admin.judgments" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="judgmentAdmin.title" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
        <li class="active"><spring:message code="judgmentAdmin.title" /></li>
	</jsp:attribute>

    <jsp:body>

        <div class="well well-sm">
            <spring:message code="judgmentAdmin.defaultColors" />
            <br/>
            <c:forEach items="${defaultColors}" var="defaultColor">
                <span style="display: inline-block; width: 15px; height: 15px; background-color: ${defaultColor.value}"> </span>
                ${defaultColor.key}
            </c:forEach>
        </div>

        <t:form action="" entity="newJudgementColor">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th><spring:message code="judgmentColor.code" /></th>
                    <th><spring:message code="judgmentColor.color" /></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="judgementColor" items="${judgementColors}">
                    <tr>
                        <td>${judgementColor.code}</td>
                        <td>${judgementColor.color}</td>
                        <td><t:deleteButton url="/private/${contestURL}/judgment/${judgementColor.id}/delete"
                                            confirmMessageCode="judgmentAdmin.delete.confirm"
                                            confirmMessageArgument="${judgementColor.code}" />
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td>
                            <form:input path="code" class="form-control" required="required"></form:input>
                        </td>
                        <td>
                            <form:input path="color" class="form-control" required="required"></form:input>
                        </td>
                        <td>
                            <t:button context="primary" type="submit">
                                <spring:message code="add" />
                            </t:button>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </t:form>
    </jsp:body>
</t:templateAdmin>