<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateWithFixedSubmenu>
    <jsp:attribute name="title">
        ${pageHeadline}
    </jsp:attribute>

    <jsp:attribute name="headline">
        ${pageHeadline}
    </jsp:attribute>

    <jsp:attribute name="javascript">
		<script src="<c:url value='/js/myicpc/controllers/officialGallery.js'/>"></script>
        <script type="text/javascript">
            function loadEventContent(eventId) {
                $("#eventContainer").html('<div class="inline-spinner"></div>');
                $.get( '<spring:url value="${contestURL}/schedule/ajax/event/" />'+eventId, function( data ) {
                    var $eventContainer = $("#eventContainer")
                    $eventContainer.html(data);
                    angular.bootstrap($('#eventPhotoGallery'), ['officialGallery']);
                });
                setFixedSubmenuHeight();
            };

            $(function() {
                if (window.location.hash != '') {
                    loadEventContent(window.location.hash.substring(1));
                    $("#eventContainer").html('<div class="inline-spinner"></div>');
                }

                if (Modernizr.localstorage) {
                    localStorage["scheduleContest"] = '${contest.code}';
                    localStorage["schedule"] = '${not empty scheduleJSON ? util:escapeJavascript(scheduleJSON) : '[sasa]'}';
                }
            });

        </script>
        <%@ include file="/WEB-INF/views/schedule/fragment/scheduleRolesDialog.jsp"%>
    </jsp:attribute>

    <jsp:attribute name="submenu">
        <jsp:include page="/WEB-INF/views/schedule/fragment/scheduleTable.jsp" />
    </jsp:attribute>

    <jsp:body>
        <t:emptyLink id="editScheduleRolesBtn" styleClass="alert-link btn btn-primary pull-right"><spring:message code="schedule.editMySchedule.link" /></t:emptyLink>
        <br class="clear" />
        <div id="eventContainer">
            <div class="no-items-available">
                <spring:message code="schedule.noSelected" />
            </div>
        </div>
    </jsp:body>

</t:templateWithFixedSubmenu>
