<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:template>
	<jsp:body>
        <div class="page-header">
			<h2>
				<spring:message code="venues" />
			</h2>	
		</div>
		<br />
		<div class="col-sm-4 col-md-3">
			<table class="table table-striped">
				<thead>
					<tr>
						<th><spring:message code="venue.list" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="venue" items="${venues}">
							<tr>
								<td><a href="#${venue.code}" onclick="loadEventContent(${venue.id})">${venue.name}</a></td>
							</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="col-sm-8 col-md-9" id="venueContainer">
			<div class="noSelectedBig">
				<spring:message code="venue.noSelected" />
			</div>
		</div>
		
		<script type="text/javascript">
			function loadEventContent(eventId) {
				$("#venueContainer").html('<div class="inline-spinner"></div>');
				$.get( '<spring:url value="${contestURL}/venue/ajax/" />'+eventId, function( data ) {
					  $("#venueContainer").html(data);
				});	
			};
			
			$(function() {
				if (window.location.hash != '') {
					loadEventContent(window.location.hash.substring(1));
				}				
			});
		</script>
		<br class="clear" />
    </jsp:body>
</t:template>