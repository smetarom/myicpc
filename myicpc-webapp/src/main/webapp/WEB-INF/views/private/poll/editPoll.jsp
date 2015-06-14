<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdminEdit>
	<jsp:attribute name="headline">
	    ${headlineTitle}
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
		<li><a href="<spring:url value="/private${contestURL}/polls" />"><spring:message code="nav.admin.polls" /></a></li>
		<li class="active">${headlineTitle}</li>
	</jsp:attribute>

	<jsp:body>
		<t:form action="/private${contestURL}/poll/update" entity="poll"
				resetFormButton="true" cancelFormURL="/private${contestURL}/polls">
		  	<jsp:attribute name="controls">
				<t:button type="submit" context="primary"><spring:message code="pollAdmin.create" /></t:button>
			</jsp:attribute>

			<jsp:body>
				<t:springInput labelCode="poll.question" path="question" required="true" />
				<t:springTextarea labelCode="poll.description" path="description" rows="3" />
				<t:springInput labelCode="poll.startDate" path="localStartDate" id="startDate" required="true" />
				<t:springInput labelCode="poll.endDate" path="localEndDate" id="endDate" required="true" />
				<t:springSelect labelCode="poll.representation" path="pollRepresentationType" options="${pollTypes}" itemValue="enumType" itemLabel="label" />
				<div class="form-group">
					<label class="control-label col-sm-3"><spring:message code="poll.choices" />: </label>
					<div class="col-sm-9">
						<div>
                            <spring:message code="poll.choices.predefined" />:
							<button class="btn btn-default" id="allTeamsBtn"><spring:message code="pollAdmin.edit.quick.allTeams" /></button>
							<button class="btn btn-default" id="allUniversitiesBtn"><spring:message code="pollAdmin.edit.quick.allUniversities" /></button>
							<button class="btn btn-default" id="allProblemsBtn"><spring:message code="pollAdmin.edit.quick.allProblems" /></button>
						</div>
						<br />
                        <span id="choices">
                            <c:forEach var="choice" items="${poll.choiceStringList}" varStatus="status">
                                <input name="choiceStringList[${status.index}]" class="choice" value="${choice}" />
                            </c:forEach>
                        </span>
                        <span class="glyphicon glyphicon-plus-sign pointer-cursor" onclick="POLL.addChoice($('#choices')); return false;"></span>
                        <span class="glyphicon glyphicon-minus-sign pointer-cursor" onclick="POLL.removeLastChoice($('#choices')); return false;"></span>
                    </div>
				</div>
			</jsp:body>
		</t:form>
		
		<script type="text/javascript">
			$(function() {
				$('#startDate').datetimepicker(datePickerOptions);
				$('#endDate').datetimepicker(datePickerOptions);

				$("#allTeamsBtn").click(function() {
					return POLL.addPredefinedOptions('<spring:url value="/private${contestURL}/poll/all-teams"/>');
				});
				$("#allUniversitiesBtn").click(function() {
					return POLL.addPredefinedOptions('<spring:url value="/private${contestURL}/poll/all-universities"/>');
				});
				$("#allProblemsBtn").click(function() {
					return POLL.addPredefinedOptions('<spring:url value="/private${contestURL}/poll/all-problems"/>');
				});
			});

            var POLL = {
                choiceCount : ${not empty choiceCount ? choiceCount : 0},
                /**
                 * Add option
                 */
                addChoice : function(container, value) {
                    value = value || "";
                    container.append('<input name="choiceStringList['+(this.choiceCount++)+']" value="'+value+'" class="choice-option" />');
                },
                addPredefinedOptions : function(url) {
                    $.getJSON(url, function( data ) {
                        $("#choices").empty();
                        for (var i = 0; i < data.length; i++) {
                            POLL.addChoice($("#choices"), data[i]);
                        }
                    });
                    return false;
                },
                /**
                 * remove last option
                 */
                removeLastChoice : function(container) {
                    $(container).children().last().remove();
                }
            };
		</script>
    </jsp:body>
</t:templateAdminEdit>