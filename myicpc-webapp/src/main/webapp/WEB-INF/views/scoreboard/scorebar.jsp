<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="title">
        <spring:message code="nav.scorebar"/>
    </jsp:attribute>
    <jsp:attribute name="javascript">
        <%@ include file="/WEB-INF/views/includes/nvd3Dependencies.jsp" %>
        <script src="<c:url value='/js/myicpc/controllers/scorebar.js'/>" defer></script>

        <script type="text/javascript">
            $(function() {
                var ngController = angular.element($("#scorebar")).scope();
                var teams = ${not empty teamJSON ? teamJSON : '[]'};
                var problemCount = ${not empty problemCount ? problemCount : 0};
                var teamCount = ${not empty teamCount ? teamCount : 0};
                ngController.init(teams, teamCount, problemCount);

                var width = $("#scorebar-container").width();
                ngController.render(width);

                startSubscribe('${r.contextPath}', '${contest.code}', 'scoreboard', updateScorebar, ngController);
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <t:downloadContestProblems/>
        <div ng-app="scorebar">
            <div id="scorebar" ng-controller="scorebarCtrl">
                <div class="col-sm-9" id="scorebar-container">
                    <div id="scorebar-legend">
                        <svg width="500" height="18">
                            <g class="canvas" transform="translate(0,0)">
                            </g>
                        </svg>
                    </div>

                    <div id="scorebar-chart">
                        <svg width="900" height="1000">
                            <g class="axis-canvas" transform="translate(120,0)">
                                <g class="canvas" transform="translate(0,20)">
                                </g>
                            </g>
                        </svg>
                    </div>
                </div>

            </div>
        </div>

    </jsp:body>

</t:template>
