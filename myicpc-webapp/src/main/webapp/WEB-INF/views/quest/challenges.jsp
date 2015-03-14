<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="headline">
        <spring:message code="nav.quest.challenges" />
    </jsp:attribute>
    <jsp:attribute name="title">
        <spring:message code="nav.quest.challenges" />
    </jsp:attribute>

    <jsp:body>
        <%@ include file="/WEB-INF/views/quest/fragment/questInfo.jsp" %>

        <c:if test="${not empty challenges}">
            <div class="col-sm-4 col-md-3">
                <%@ include file="/WEB-INF/views/quest/fragment/challengeSubmenu.jsp" %>
            </div>
            <div class="col-sm-8 col-md-9" id="challengeContainer">
                <div class="no-items-available">
                    <spring:message code="quest.challanges.noSelected" />
                </div>
            </div>

        </c:if>
        <c:if test="${empty challenges}">
            <div class="no-items-available">
                <spring:message code="quest.challenge.noResult" />
            </div>
        </c:if>

        <script type="text/javascript">
            function loadChallengeContent(eventId) {
                $("#challengeContainer").html('<div class="inline-spinner"></div>');
                $.get( '<spring:url value="${contestURL}/quest/ajax/challenge/" />'+eventId, function( data ) {
                    $("#challengeContainer").html(data);
                });
            };

            $(function() {
                if (window.location.hash != '') {
                    $("#challengeContainer").html('<div class="inline-spinner"></div>');
                    $.get( '<spring:url value="${contestURL}/quest/ajax/challenge/" />' + window.location.hash.substring(1), function( data ) {
                        $("#challengeContainer").html(data);
                    });
                }
            });
        </script>
    </jsp:body>
</t:template>
