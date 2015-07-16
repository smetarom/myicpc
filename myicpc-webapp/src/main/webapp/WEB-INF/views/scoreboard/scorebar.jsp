<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="title">
        <spring:message code="nav.scorebar"/>
    </jsp:attribute>
    <jsp:attribute name="javascript">
        <%@ include file="/WEB-INF/views/includes/nvd3Dependencies.jsp" %>
        <script src="<c:url value='/js/myicpc/controllers/scorebar.js'/>" defer></script>

        <script id="scorebar-popup-template" type="text/x-jquery-tmpl">
            <div class="panel panel-default">
              <div class="panel-heading">{{teamName}}</div>
              <div class="panel-body">
                <strong><spring:message code="scorebar.rank" />:</strong> {{rank}}<br/>
                <strong><spring:message code="scorebar.passed" />:</strong> {{solvedNum}} {{solved}}<br/>
                <strong><spring:message code="scorebar.failed" />:</strong> {{failedNum}} {{failed}}<br/>
              </div>
            </div>
        </script>

        <script type="text/javascript">
            $(function() {
                var ngController = angular.element($("#scorebar")).scope();
                var teams = ${not empty teamJSON ? teamJSON : '[]'};
                var problemCount = ${not empty problemCount ? problemCount : 0};
                var teamCount = ${not empty teamCount ? teamCount : 0};
                teams = sortTeamRanks(teams);
                ngController.init(teams, teamCount, problemCount);

                var width = $("#scorebar-container").width();
                ngController.render(width);

                startSubscribe('${r.contextPath}', '${contest.code}', 'scoreboard', updateScorebar, ngController);

                var teamPopupTemplate = compileHandlebarsTemplate("scorebar-popup-template");

                scorebarDisplayText = function(element) {
                    var s = element.id.substring(10);
                    var team = ngController.findById(parseInt(s));
                    d3.select("#passed-bar" + s).attr("class", "barhover");
                    d3.select("#failed-bar" + s).attr("class", "barhover");
                    d3.select("#neutrl-bar" + s).attr("class", "barhover");
                    d3.select("#workon-bar" + s).attr("class", "barhover");
                    d3.select("#bar-Ntitle" + s).style("fill", "blue");
                    var $scorebarPopup = $("#scorebar-popup");
                    formatProblemArray = function(problems) {
                        var letters = [];
                        for (var i = 0, len = problems.length; i < len; i++) {
                            letters.push(problems[i].code);
                        }
                        var str = letters.join(", ");
                        if (str.length > 0) {
                            str = "(" + str + ")";
                        }
                        return str;
                    };
                    $scorebarPopup.html(teamPopupTemplate({
                        rank: team.rank,
                        teamName: team.teamShortName,
                        solvedNum: team.solvedNum,
                        failedNum: team.failedNum,
                        solved: formatProblemArray(team.solved),
                        failed: formatProblemArray(team.failed)
                    }));
                    $scorebarPopup.position({
                        my: "left top-18",
                        at: "right+" + (ngController.getBarWidth(team) + 10) + " center",
                        of: element,
                        collision: "fit"
                    });
                    $scorebarPopup.css('visibility', 'visible');
                };

                scorebarHideText = function(element) {
                    var s = element.id.substring(10);
                    var team = ngController.findById(parseInt(s));
                    d3.select("#passed-bar" + s).attr("class", "passed");
                    d3.select("#failed-bar" + s).attr("class", "failed");
                    d3.select("#neutrl-bar" + s).attr("class", "neutral");
                    d3.select("#workon-bar" + s).attr("class", "workon");
                    d3.select("#bar-Ntitle" + s).style("fill", "black");
                    var $scorebarPopup = $("#scorebar-popup");
                    $scorebarPopup.css('visibility', 'hidden');
                }
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <t:downloadContestProblems/>
        <c:if test="${scorebarAvailable}">
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
            <div id="scorebar-popup" style="visibility: hidden;"></div>
        </c:if>
        <c:if test="${not scorebarAvailable}">
            <div class="no-items-available">
                <h2>
                    <spring:message code="scorebar.notAvailable" />
                </h2>
            </div>
        </c:if>

    </jsp:body>

</t:template>
