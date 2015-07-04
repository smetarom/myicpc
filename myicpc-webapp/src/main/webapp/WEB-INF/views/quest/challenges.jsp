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
            function loadChallengeContent(challengeId) {
                $("#challengeContainer").html('<div class="inline-spinner"></div>');
                $.get( '<spring:url value="${contestURL}/quest/ajax/challenge/" />'+challengeId, function( data ) {
                    $("#challengeContainer").html(data);
                });
                setFixedSubmenuHeight();
            };

            $(function() {
                if (window.location.hash != '') {
                    $("#challengeContainer").html('<div class="inline-spinner"></div>');
                    $.get( '<spring:url value="${contestURL}/quest/ajax/challenge/" />' + window.location.hash.substring(1), function(data) {
                        $("#challengeContainer").html(data);
                    });
                }
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <c:if test="${not empty challenges}">
            <div class="pull-right">
                <t:button context="primary" modalOpenId="missingChallengesModal">
                    <t:glyphIcon icon="eye-open" /><spring:message code="quest.challenges.seeMissing" />
                </t:button>
            </div>
            <t:plainForm action="${contestURL}/quest/challenge/missing-challenges">
                <t:modalWindow id="missingChallengesModal">
                    <jsp:attribute name="title"><spring:message code="quest.challenges.missing.title" /></jsp:attribute>
                    <jsp:attribute name="footer">
                        <t:button context="default" dataDismiss="true"><spring:message code="close" /></t:button>
                        <t:button context="primary" type="submit"><spring:message code="submit" /></t:button>
                    </jsp:attribute>
                    <jsp:body>
                        <p><spring:message code="quest.challenges.missing.body" /></p>
                        <input name="twitterUsername" class="form-control" placeholder="<spring:message code="twitter" />">
                        <p class="text-center"><spring:message code="or" /></p>
                        <input name="vineUsername" class="form-control" placeholder="<spring:message code="vine" />">
                    </jsp:body>
                </t:modalWindow>
            </t:plainForm>
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
