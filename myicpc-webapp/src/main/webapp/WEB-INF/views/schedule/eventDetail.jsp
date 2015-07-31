<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>

    <jsp:attribute name="title">
        ${event.name}
    </jsp:attribute>

    <jsp:attribute name="headline">
        ${event.name}
    </jsp:attribute>

    <jsp:attribute name="javascript">
		<script src="<c:url value='/js/myicpc/controllers/officialGallery.js'/>"></script>
    </jsp:attribute>

    <jsp:body>
        <div class="container" ng-app="officialGallery">
            <%@ include file="/WEB-INF/views/schedule/fragment/eventDetail.jsp"%>
        </div>
    </jsp:body>

</t:template>
