<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
	<jsp:attribute name="headline">
		<spring:message code="questAdmin.submissions.title" />
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <li><a href="<spring:url value="/private/quest" />"><spring:message code="nav.admin.quest" /></a></li>
		<li class="active"><spring:message code="questAdmin.submissions.title" /></li>
	</jsp:attribute>
	
	<jsp:body>
		<div class="well well-sm">
			<form:form class="form-inline" role="form" action="" commandName="submissionFilter">
			  <div class="form-group">
			    <form:label path="submissionState" class="control-label">
					<spring:message code="questAdmin.submissions.filter.state" />: </form:label>
			    <form:select path="submissionState" class="form-control">
			    	<form:option value="">
							<spring:message code="questAdmin.submissions.allStates" />
						</form:option>
		        	<form:options items="${submissionStates}" />
		        </form:select>
			  </div>
			  <div class="form-group">
			    <form:label path="challenge" class="control-label">
					<spring:message code="questAdmin.submissions.filter.challenge" />: </form:label>
				<form:select path="challenge" class="form-control">
		        	<form:option value="null">
						<spring:message code="questAdmin.submissions.allChallenges" />
					</form:option>
					<form:options items="${challenges}" itemValue="id" itemLabel="name" />
		        </form:select>
			  </div>
			  <div class="form-group">
			    <form:label path="participant" class="control-label">
					<spring:message code="questAdmin.submissions.filter.participant" />: </form:label>
		        <form:select path="participant" class="form-control">
		        	<form:option value="null">
						<spring:message code="questAdmin.submissions.allParticipants" />
					</form:option>
					<form:options items="${participants}" itemValue="id" itemLabel="contestParticipant.officialFullname" />
		        </form:select>
			  </div>
			  <button type="submit" class="btn btn-default">
					<spring:message code="questAdmin.submissions.filter" />
				</button>
			</form:form>
		</div>
		
		<div class="clearfix">
            <c:forEach var="submission" items="${submissions.getContent()}" varStatus="status">
                <div class="col-sm-4">
                    <div class="thumbnail">
                        <%@ include file="/WEB-INF/views/private/quest/fragment/submissionTemplate.jsp"%>
                        <br />
                        <t:plainForm action="/private${contestURL}/quest/submission/${submission.id}/accept" style="padding: 10px 10px; background-color: #dff0d8;">
                            <table style="width: 100%;">
                                <tbody>
                                <tr>
                                    <td style="width: 110px"><label for="questPoints"><spring:message code="questAdmin.submissions.questPoints" />:&nbsp;&nbsp;&nbsp;</label></td>
                                    <td>
                                        <div class="input-group">
                                            <input type="text" size="3" class="form-control" id="questPoints" name="questPoints" value="${submission.questPoints}">
                                              <span class="input-group-btn">
                                                <button type="submit" class="btn btn-success">
                                                    <i class="fa fa-check"></i> <spring:message code="questAdmin.submissions.approve" />
                                                </button>
                                              </span>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </t:plainForm>
                        <t:plainForm action="/private${contestURL}/quest/submission/${submission.id}/reject" style="padding: 10px 10px; background-color: #f2dede;">
                            <table style="width: 100%;">
                                <tbody>
                                    <tr>
                                        <td style="width: 110px"><label for="reasonToReject"><spring:message code="questAdmin.submissions.reject.reason" />:&nbsp;&nbsp;&nbsp;</label></td>
                                        <td>
                                            <div class="input-group">
                                              <input type="text" size="3" class="form-control" id="reasonToReject" name="reasonToReject" value="${submission.reasonToReject}">
                                              <span class="input-group-btn">
                                                <button type="submit" class="btn btn-danger">
                                                            <i class="fa fa-ban"></i> <spring:message code="questAdmin.submissions.reject" />
                                                        </button>
                                              </span>
                                            </div>
                                            </td>
                                    </tr>
                                </tbody>
                            </table>
                        </t:plainForm>
                    </div>
                </div>
                <c:if test="${util:isNLine(status, 2, 3)}">
                    <br class="clear" />
                </c:if>
            </c:forEach>
            <c:if test="${empty submissions.getContent()}">
                <div class="no-items-available">
                    <spring:message code="questAdmin.submissions.noResult" />
                </div>
            </c:if>
		</div>
		
		<%--<t:pager pager="${submissions}" />--%>
    </jsp:body>
</t:templateAdmin>