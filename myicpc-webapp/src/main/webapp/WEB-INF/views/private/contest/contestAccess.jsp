<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>

    <jsp:attribute name="title">
		  <spring:message code="nav.admin.contestAccess"/>
	</jsp:attribute>

    <jsp:attribute name="breadcrumb">
		  <li class="active"><spring:message code="nav.admin.contestAccess"/></li>
	</jsp:attribute>

    <jsp:attribute name="headline">
		  <spring:message code="nav.admin.contestAccess"/>
    </jsp:attribute>

    <jsp:attribute name="controls">
        <t:button modalOpenId="addContestManager" styleClass="btn-hover"><t:faIcon icon="plus" /> <spring:message code="contestAccessAdmin.create" /></t:button>
        <t:modalWindow id="addContestManager">
            <jsp:attribute name="title">
                <spring:message code="contestAccessAdmin.create" />
            </jsp:attribute>
            <jsp:body>
                <t:plainForm action="" styleClass="form-inline text-left">
                    <select name="availableManagerId" class="form-control" required="required" id="availableManager">
                        <option value=""></option>
                        <c:forEach var="manager" items="${availableManagers}">
                            <option value="${manager.id}">
                                    ${manager.fullname}
                                <c:if test="${not empty manager.fullname}">-</c:if>
                                    ${manager.username}
                            </option>
                        </c:forEach>
                    </select>
                    <t:button type="submit" context="primary" styleClass="btn-sm"><spring:message code="add" /></t:button>
                </t:plainForm>
            </jsp:body>
        </t:modalWindow>
    </jsp:attribute>

    <jsp:body>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th><spring:message code="contestAccessAdmin.username" /></th>
                    <th><spring:message code="contestAccessAdmin.fullname" /></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="contestAccess" items="${contestAccesses}">
                <tr>
                    <td>${contestAccess.systemUser.username}</td>
                    <td>${contestAccess.systemUser.fullname}</td>
                    <td class="text-right">
                        <t:deleteButton url="/private/${contestCode}/access/${contestAccess.id}/delete" confirmMessageCode="contestAccessAdmin.delete.confirm" confirmMessageArgument="${contestAccess.systemUser.username}" />
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <script type="application/javascript">
            $('#addContestManager').on('shown.bs.modal', function (e) {
                $('#availableManager').chosen({width: "80%"});
            })
        </script>
    </jsp:body>
</t:templateAdmin>