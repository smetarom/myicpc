<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="blacklistAdmin.title" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="blacklistAdmin.title" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="blacklistAdmin.title" /></li>
	</jsp:attribute>

    <jsp:attribute name="controls">
        <button data-toggle="modal" data-target="#addUserBlacklistModal" class="btn btn-default btn-hover">
            <t:glyphIcon icon="plus"/> <spring:message code="blacklistAdmin.addUser" />
        </button>

        <t:plainForm action="/private/${contestURL}/blacklist">
            <t:modalWindow id="addUserBlacklistModal">
                <jsp:attribute name="title">
                    <spring:message code="blacklistAdmin.addUser" />
                </jsp:attribute>
                <jsp:attribute name="footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <spring:message code="close" />
                    </button>
                    <button type="submit" class="btn btn-primary">
                        <spring:message code="save" />
                    </button>
                </jsp:attribute>
                <jsp:body>
                    <div class="clearfix">
                        <div class="form-group">
                            <label for="username" class="col-sm-3 control-label"><spring:message code="blacklist.username" />:*</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="username" name="username" required="required">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="blacklistType" class="col-sm-3 control-label"><spring:message code="blacklist.socialMedia" />:*</label>
                            <div class="col-sm-9">
                                <select class="form-control" id="blacklistType" name="blacklistType">
                                    <c:forEach var="option" items="${blacklistTypes}">
                                        <option value="${option}">${option.label}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </jsp:body>
            </t:modalWindow>
        </t:plainForm>
    </jsp:attribute>

    <jsp:body>
        <table class="table table-striped">
            <thead>
            <tr>
                <th><spring:message code="blacklist.username" /></th>
                <th><spring:message code="blacklist.socialMedia" /></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="blacklist" items="${blacklists}">
                <tr>
                    <td>${blacklist.username}</td>
                    <td>${blacklist.socialMediaUserType.label}</td>
                    <td class="text-right">
                        <t:deleteButton url="/private${contestURL}/blacklist/removeUserFromBlacklist/${blacklist.id}" confirmMessageCode="blacklist.removeConfirm" confirmMessageArgument="${blacklist.username}"></t:deleteButton>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </jsp:body>
</t:templateAdmin>