<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<div class="modal fade" id="scheduleRolesDialog" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title" id="myModalLabel">
					<spring:message code="settings.scheduleRoles" />
				</h4>
			</div>
			<div class="modal-body"></div>
		</div>
	</div>
</div>

<script type="text/javascript">
	function processFollowTeamForm() {
		$( "#scheduleRolesForm" ).submit(function( event ) {
			 
		  // Stop form from submitting normally
		  event.preventDefault();
		 
		  // Get some values from elements on the page:
		  var $form = $( this ),
		    term = $form.find("#scheduleRolesSelect").val();
		 
		  // Send the data using post
		  var posting = $.post('<spring:url value="/ajax/settings/addScheduleRole" />', {'roleId': term } );
		 
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
	}
	$("#editScheduleRolesBtn").click(function() {
		$.get("<spring:url value="/settings/scheduleRoles" />", function(data) {
			$('#scheduleRolesDialog .modal-body').html(data);
			processFollowTeamForm();			
		});
		$('#scheduleRolesDialog').modal('show');
	});
</script>