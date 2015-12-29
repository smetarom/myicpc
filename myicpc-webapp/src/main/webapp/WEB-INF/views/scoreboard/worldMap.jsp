<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="javascript">
        <%@ include file="/WEB-INF/views/includes/nvd3Dependencies.jsp" %>
        <script src="<c:url value='/js/myicpc/controllers/worldMap.js'/>"></script>
        <script type="application/javascript">
            $(function() {
                var teamCoordinates = ${not empty teamCoordinatesJSON ? teamCoordinatesJSON : '{}'};
                var teams = ${not empty teamJSON ? teamJSON : '[]'};
                var problems = ${not empty problemJSON ? problemJSON : '[]'};
                var mapConfigurations = ${not empty mapConfigurations ? mapConfigurations : '[]'};
                var ngController = angular.element($("#worldMap")).scope();
                var width = $("#mapContainer").width();
                ngController.init(teams, problems);
                var config = ngController.pickSuitableConfiguration(mapConfigurations, width);
                ngController.renderMap('${r.contextPath}', config, teamCoordinates);

                if (!$("body").hasClass("mobile")) {
                    var height = $(window).height() - 55;
                    if (height < 300) {
                        height = $("#mapContainer").height();
                    }
                    $("#mapSubmenu").height(height);
                }

                startSubscribe('${r.contextPath}', '${contest.code}', 'scoreboard', updateWorldMap, ngController);
            });
        </script>
    </jsp:attribute>
    <jsp:attribute name="title">
        <spring:message code="nav.map"/>
    </jsp:attribute>

    <jsp:body>
        <div ng-app="worldMap">
            <div id="worldMap" ng-controller="worldMapCtrl">
                <div class="col-sm-12 col-md-9 text-center zero-padding">
                    <div class="responsive" id="mapContainer">
                    </div>
                    <div id="mapPopoverPanel" class="panel panel-default hidden">
                        <div class="panel-heading">
                            <strong><spring:message code="noDataAvailable" /></strong>
                        </div>
                        <div class="panel-body">
                        </div>
                    </div>
                </div>
                <div class="col-sm-12 col-md-3" id="sidebar" ng-cloak>
                    <div id="mapSubmenu" style="overflow-y: auto">
                        <h3 style="text-underline: none;">{{activeComponent.name}}</h3>
                        <table class="table table-condensed striped-rows" ng-if="activeComponent">
                            <thead>
                            <tr>
                                <th><spring:message code="scoreboard.rankShort" /></th>
                                <th><spring:message code="scoreboard.teamName" /></th>
                            </tr>

                            </thead>
                            <tbody>
                            <tr id="selectedCountryScoreboard" ng-repeat="team in teams | filter:filterTeams | orderBy:['teamRank', 'teamName']" class="team_{{team.teamId}} cursor-pointer"
                                ng-mouseover="highlightTeamOnMap(team);" ng-mouseleave="dishighlightTeamOnMap(team)" id="team_{{team.teamExternalId}}">
                                <td>{{team.teamRank}}</td>
                                <td><a href='<spring:url value="${contestURL}/team/{{team.teamId}}" />'>{{team.teamName}}</a></td>
                            </tr>
                            </tbody>
                        </table>

                        <div class="scoreboardLegend" ng-hide="!activeComponent">
                            <strong><spring:message code="scoreboard.legend" />:</strong> <br />
                            <span class="label label-success">X - YY</span> - <spring:message code="scoreboard.legend.solved" />
                            <br />
                            <span class="label label-warning">?</span> - <spring:message code="scoreboard.legend.pending" />
                            <br />
                            <span class="label label-danger">X</span> - <spring:message code="scoreboard.legend.attempted" />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>

</t:template>
