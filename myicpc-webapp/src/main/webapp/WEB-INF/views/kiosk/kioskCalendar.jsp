<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateKiosk>
    
    <jsp:attribute name="javascript">
        <script src="<c:url value='/js/angular/masonry.pkgd.min.js'/>"></script>
        <script src="<c:url value='/js/angular/angular-masonry.min.js'/>"></script>
        <script src="<c:url value='/js/angular/imagesloaded.pkgd.min.js'/>"></script>
        <script src="<c:url value='/js/myicpc/controllers/kiosk.js'/>"></script>

        <script type="text/javascript">
            $(function () {
                var ngController = angular.element($("#kiosk-content")).scope();
                var days = ${not empty scheduleJSON ? scheduleJSON : '[]'};
                ngController.init(days);

                setTimeout(function () {
                    scrollMeDown();
                }, 2000);
                setTimeout(function () {
                    ngController.refresh();
                }, 5000);

                startSubscribe('${r.contextPath}', '${contest.code}', 'notification', updateKioskFeed, ngController);
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <div id="kiosk-content" class="clearfix" ng-app="kiosk" ng-controller="kioskCalendarCtrl" ng-cloak>
            <div masonry="{'gutter': '.gutter-sizer', transitionDuration: '1.2s'}" preserve-order
                 item-selector=".kiosk-tile">

                <div masonry-brick class="kiosk-tile" ng-class="{today: day.isToday}" ng-repeat="day in days">
                    <div class="tile-content">
                        <div class="gutter-sizer"></div>
                        <div class="grid-sizer"></div>
                        <div class="item ng-scope">

                            <div class="tile-title myicpc" ng-class="{today: day.isToday}">
                                {{day.localDate}} - {{day.name}}
                            </div>
                            <div class="tile-detail">
                                <table class="table table-striped">
                                    <tr ng-repeat="event in day.events">
                                        <td style="width: 100px">
                                            {{event.localStartDate}} - {{event.localEndDate}}
                                        </td>
                                        <td>{{event.name}}</td>
                                        <td ng-if="day.isToday">
                                            {{event.venue}}
                                        </td>
                                        <td ng-if="day.isToday">
                                            {{event.roles}}
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </div>
    </jsp:body>
</t:templateKiosk>
