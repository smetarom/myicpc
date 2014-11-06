<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateOffline>
    <jsp:body>
        <div id="offlineChallengesWrapper" class="table-responsive desktop" ng-app
             ng-controller="OfflineQuestController">
            <div ng-if="challenges">
                <div class="page-header">
                    <h2>
                        <spring:message code="quest"/>
                    </h2>
                </div>
                <div ng-repeat="challenge in challenges" class="col-sm-12 clearfix">
                    <h3>{{challenge.name}}</h3>

                    <p ng-hide="!challenge.endDate">
                        <strong><spring:message code="quest.deadline"/>: {{challenge.endDate}}</strong>
                    </p>

                    <p ng-hide="!challenge.hashtags">
                        <strong><spring:message code="quest.tweetWithHashtags"/>: {{challenge.hashtags}}</strong>
                    </p>

                    <p>{{challenge.description}}</p>

                </div>
            </div>
            <br class="clear"/>
        </div>

        <div id="noResult" class="container" style="border-width: 0; margin-top: 2em;">
            <div class="jumbotron">
                <h2 style="font-size: 1.9em;">
                    <spring:message code="offline.quest.notLoaded"/>
                </h2>

                <p>
                    <spring:message code="offline.quest.notLoaded.hint"/>
                </p>

                <p>
                    <a href="<c:url value="/" />" class="btn btn-primary btn-lg" role="button"><spring:message
                            code="offline.goHome"/></a>
                </p>
            </div>
        </div>


        <script type="text/javascript">
            function OfflineQuestController($scope) {
                $scope.challenges = [];

                $scope.setChallenges = function (challenges) {
                    $scope.challenges = challenges;
                    $scope.$apply();
                };
            }

            $(function () {
                var ngController = angular.element($("#offlineChallengesWrapper")).scope();
                if (Modernizr.localstorage) {
                    var challenges = localStorage["challenges"];
                    if (challenges !== undefined) {
                        ngController.setChallenges(JSON.parse(challenges));
                        $("#noResult").css("display", "none");
                    }
                }
            });
        </script>
    </jsp:body>
</t:templateOffline>