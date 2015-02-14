<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:template>
    <jsp:attribute name="title">
      <spring:message code="nav.teams" />
    </jsp:attribute>

    <jsp:attribute name="headline">
      <spring:message code="nav.teams" />
    </jsp:attribute>

	<jsp:body>
        <c:if test="${showSwitch}">
            <div class="col-sm-12" style="margin-bottom: 10px;">
                <div class="pull-right">
                    <c:if test="${view eq 'grid'}">
                        <a href="?view=list" class="btn btn-hover btn-default"><t:glyphIcon icon="th-list"/> <spring:message code="view.list" /></a>
                    </c:if>
                    <c:if test="${view eq 'list'}">
                        <a href="?view=grid" class="btn btn-hover btn-default"><t:glyphIcon icon="th-large"/> <spring:message code="view.grid" /></a>
                    </c:if>
                </div>
            </div>
        </c:if>
		<div class="clearfix">
            <c:if test="${view eq 'list'}">
                <%@ include file="/WEB-INF/views/scoreboard/fragment/teamsList.jsp"%>
            </c:if>
            <c:if test="${view ne 'list'}">
                <%@ include file="/WEB-INF/views/scoreboard/fragment/teamsGrid.jsp"%>
            </c:if>
		</div>
        
    </jsp:body>
</t:template>