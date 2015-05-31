<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateWithFixedSubmenu>
    <jsp:attribute name="headline">
        <spring:message code="nav.quest.challenges" />
    </jsp:attribute>
    <jsp:attribute name="title">
        <spring:message code="nav.quest.challenges" />
    </jsp:attribute>

    <jsp:attribute name="topContent">
        <%@ include file="/WEB-INF/views/quest/fragment/questInfo.jsp" %>
    </jsp:attribute>

    <jsp:attribute name="submenu">
        <%@ include file="/WEB-INF/views/quest/fragment/challengeSubmenu.jsp" %>
    </jsp:attribute>
    
    <jsp:attribute name="javascript">
        <script type="text/javascript">
            function loadChallengeContent(eventId) {
                $("#challengeContainer").html('<div class="inline-spinner"></div>');
                $.get( '<spring:url value="${contestURL}/quest/ajax/challenge/" />'+eventId, function( data ) {
                    $("#challengeContainer").html(data);
                });
                setFixedSubmenuHeight();
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
    </jsp:attribute>

    <jsp:body>
        <c:if test="${not empty challenges}">
            <div id="challengeContainer">
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
    </jsp:body>
</t:templateWithFixedSubmenu>
