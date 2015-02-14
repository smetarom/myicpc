<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:template>
    <jsp:attribute name="title">
        <spring:message code="venues" />
    </jsp:attribute>

    <jsp:attribute name="headline">
        <spring:message code="venues" />
    </jsp:attribute>

	<jsp:body>
		<div class="col-sm-4 col-md-3">
            <jsp:include page="/WEB-INF/views/schedule/fragment/venuesTable.jsp" />
		</div>
		<div class="col-sm-8 col-md-9" id="venueContainer">
			<div class="no-items-available">
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