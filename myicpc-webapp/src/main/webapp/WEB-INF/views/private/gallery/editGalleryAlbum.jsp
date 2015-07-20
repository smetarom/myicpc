<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdminEdit>
	<jsp:attribute name="headline">
	    ${headlineTitle}
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
		<li><a href="<spring:url value="/private${contestURL}/gallery/official" />"><spring:message code="nav.admin.gallery.official" /></a></li>
		<li class="active">${headlineTitle}</li>
	</jsp:attribute>

	<jsp:body>
		<t:form action="/private${contestURL}/gallery/official/update" entity="galleryAlbum"
				resetFormButton="true" cancelFormURL="/private${contestURL}/gallery/official">
		  	<jsp:attribute name="controls">
				<t:button type="submit" context="primary"><spring:message code="save" /></t:button>
			</jsp:attribute>

			<jsp:body>
				<t:springInput path="name" labelCode="galleryAlbum.name" />
				<t:springCheckbox path="published" labelCode="galleryAlbum.publish" disabled="${galleryAlbum.published}" />
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