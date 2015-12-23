<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
        <%@ include file="/WEB-INF/views/includes/nvd3Dependencies.jsp" %>
        <script src="<c:url value='/js/myicpc/controllers/problem.js'/>" defer></script>
        <style type="text/css">
            <c:forEach items="${judgmentColors}" var="color">
                .judgment-color.${color.key} {
                    color: ${color.value};
                }
            </c:forEach>
        </style>
    </jsp:attribute>
    <jsp:attribute name="title">
        ${problem.code}: ${problem.name}
    </jsp:attribute>
    <jsp:attribute name="headline">
        ${problem.code}: ${problem.name}
    </jsp:attribute>
    <jsp:attribute name="javascript">
        <script type="application/javascript">
            $(function() {
                var ngController = angular.element($("#problemController")).scope();
                var judgements = ${not empty judgementsJSON ? judgementsJSON : '{}'};
                ngController.setJudgements(judgements);

                startSubscribe('${r.contextPath}', '${contest.code}', 'problem/${problem.code}', updateProblemView, ngController);
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <div id="insight" ng-app="problem">
            <div id="problemController" ng-controller="problemCtrl">
                <t:secondLevelSubmenu isMobile="${sitePreference.mobile}">
                    <li ng-class="{active: activeTab == 'attempts'}"><a href="#/attempts"><spring:message code="problem.tab.byAttempts"/></a></li>
                    <li ng-class="{active: activeTab == 'teams'}"><a href="#/teams"><spring:message code="problem.tab.byTeams"/></a></li>
                    <li ng-class="{active: activeTab == 'overview'}"><a href="#/overview"><spring:message code="problem.tab.overview"/></a></li>
                </t:secondLevelSubmenu>
            </div>

            <div class="col-sm-9">
                <div ng-view></div>
            </div>
            <div class="col-sm-3">
                <t:hashtagPanel id="problemSocial" contestURL="${contestURL}" hashtag1="Problem${problem.code}" hashtag2="${contest.hashtag}" />
            </div>
        </div>
    </jsp:body>

</t:template>
