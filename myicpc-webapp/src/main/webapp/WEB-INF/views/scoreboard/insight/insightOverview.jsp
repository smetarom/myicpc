<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="title">
        <spring:message code="insight.dashboard"/>
    </jsp:attribute>
    <jsp:attribute name="headline">
        <span id="insightHeadline"><spring:message code="insight.dashboard" /></span>
    </jsp:attribute>
    <jsp:attribute name="javascript">
        <script type="application/javascript">
            (function insightPoll(){
               setTimeout(function(){
                    $.get("<spring:url value="${contestURL}/insight/ajax/overview" />", function(data) {
                      $("#insightDashboard").html( data );
                      insightPoll();
                    });
              }, 15000);
            })();
        </script>
    </jsp:attribute>

    <jsp:body>
        <div id="insightDashboard">
            <%@ include file="fragment/insightOverview.jsp" %>
        </div>
    </jsp:body>

</t:template>
