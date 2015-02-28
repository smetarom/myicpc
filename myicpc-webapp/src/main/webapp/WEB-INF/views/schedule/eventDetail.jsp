<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>

    <jsp:attribute name="title">
        ${event.name}
    </jsp:attribute>

    <jsp:attribute name="headline">
        ${event.name}
    </jsp:attribute>

    <jsp:body>
        <div class="container">
            <%@ include file="/WEB-INF/views/schedule/fragment/eventDetail.jsp"%>
        </div>
    </jsp:body>

</t:template>