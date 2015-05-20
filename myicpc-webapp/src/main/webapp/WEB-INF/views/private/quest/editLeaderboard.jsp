<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdminEdit>
    <jsp:attribute name="title">
		${headlineTitle}
	</jsp:attribute>

	<jsp:attribute name="headline">
		${headlineTitle}
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li><a href="<spring:url value="/private${contestURL}/quest" />"><spring:message code="nav.admin.quest" /></a></li>
        <li class="active">${headlineTitle}</li>
	</jsp:attribute>
	
	<jsp:body>
        <t:form action="/private${contestURL}/quest/leaderboard/update" entity="leaderboard" resetFormButton="true" cancelFormURL="/private${contestURL}/quest/leaderboards">
            <jsp:attribute name="controls">
                <button type="submit" class="btn btn-primary">
                    <spring:message code="save" />
                </button>
            </jsp:attribute>
            <jsp:body>
                <t:springInput labelCode="quest.leaderboard.name" path="name" required="true" />
                <t:springInput labelCode="quest.leaderboard.urlCode" path="urlCode" required="true" />
                <div class="form-group">
                    <label class="col-sm-3 control-label">
                        <spring:message code="quest.leaderboard.roles"/>:
                    </label>
                    <div class="col-sm-9">
                            <c:forEach var="role" items="${roles}">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" name="roles" value="${role}" ${util:arrayContains(leaderboard.parsedRoles, role.toString()) ? 'checked="checked"' : ''}">
                                        <spring:message code="${role.code}" text="${role.label}" />
                                    </label>
                                </div>
                            </c:forEach>
                    </div>
                </div>
                <t:springCheckbox labelCode="quest.leaderboard.published" path="published" styleClass="checkboxSwitch" />
            </jsp:body>
        </t:form>

        <script type="application/javascript">
            $(function() {
                $.fn.bootstrapSwitch.defaults.size = 'small';
                $("input.checkboxSwitch").bootstrapSwitch();
            })
        </script>
    </jsp:body>
</t:templateAdminEdit>