<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>

    <jsp:attribute name="title">
        ${venue.name}
    </jsp:attribute>

    <jsp:attribute name="headline">
        ${venue.name}
    </jsp:attribute>

    <jsp:body>
        <div class="container">
            <%@ include file="/WEB-INF/views/schedule/fragment/venueDetail.jsp"%>
        </div>
    </jsp:body>

</t:template>
