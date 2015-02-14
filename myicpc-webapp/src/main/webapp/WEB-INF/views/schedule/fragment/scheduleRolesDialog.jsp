<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<div class="modal fade" id="scheduleRolesDialog" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">
					<spring:message code="schedule.editMySchedule.link" />
				</h4>
			</div>
			<div class="modal-body"></div>
		</div>
	</div>
</div>

<script type="text/javascript">
    processFollowTeamForm = function() {
		$( "#scheduleRolesForm" ).submit(function( event ) {
			 
		  // Stop form from submitting normally
		  event.preventDefault();

		  var $form = $( this ),
		    term = $form.find("#scheduleRolesSelect").val();
		 
		  // Send the data using post
		  var posting = $.post('<spring:url value="${contestURL}/schedule/addScheduleRole" />', {'roleId': term } );
		 
		  // Put the results in a div
		  posting.done(function( data ) {
			  window.location = '${serverURL}<spring:url value="/my-schedule" />';
		  });
		});	
		
		$(".removeScheduleRolesLink").click(function(event) {
			event.preventDefault();
			var roleId = $(this).attr("data-role-id");
			$.get( "<spring:url value="/ajax/settings/removeScheduleRole" />/"+roleId, function( data ) {
				window.location = '${serverURL}<spring:url value="/my-schedule" />';
			});
		});
	};
    showRolesDialog = function() {
        $.get("<spring:url value="${contestURL}/schedule/scheduleRoles" />", function(data) {
            $('#scheduleRolesDialog .modal-body').html(data);
            processFollowTeamForm();
        });
        $('#scheduleRolesDialog').modal('show');
    };
	$("#editScheduleRolesBtn").click(function() {
        showRolesDialog();
	});

    $(function() {
        <c:if test="${showRoleDialog}">
            showRolesDialog();
        </c:if>
    });
</script>