<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateKiosk>
    
    <jsp:attribute name="javascript">
        <script src="<c:url value='/js/angular/masonry.pkgd.min.js'/>"></script>
        <script src="<c:url value='/js/angular/angular-masonry.min.js'/>"></script>
        <script src="<c:url value='/js/angular/imagesloaded.pkgd.min.js'/>"></script>
        <script src="<c:url value='/js/myicpc/controllers/kiosk.js'/>"></script>

        <script type="text/javascript">
            updateKioskPage = function(data) {
                if (data.type === 'modeChange') {
                    window.location.href='<spring:url value="${contestURL}/kiosk/calendar" />'
                }
            };

            $(function() {
                var ngController = angular.element($("#kiosk-content")).scope();
                var notifications = ${not empty notificationsJSON ? notificationsJSON : '[]'};
                ngController.init(notifications);

                setTimeout(function(){
                    scrollMeDown();
                }, 2000);
                setTimeout(function(){
                    ngController.refresh();
                }, 5000);

                startSubscribe('${r.contextPath}', '${contest.code}', 'notification', updateKioskFeed, ngController);
                startSubscribe('${r.contextPath}', '${contest.code}', 'kiosk', updateKioskPage, null);
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <div id="kiosk-content" class="clearfix" ng-app="kiosk" ng-controller="kioskFeedCtrl" ng-cloak>
            <div masonry="{'gutter': '.gutter-sizer', transitionDuration: '1.2s'}" preserve-order item-selector=".kiosk-tile">

                <div masonry-brick class="kiosk-tile" ng-repeat="notification in notifications">
                    <div class="gutter-sizer"></div>
                    <div ng-switch="notification.type" class="tile-content">
                        <c:forEach var="type" items="${availableNotificationTypes}">
                            <div ng-switch-when="${type.code}">
                                <t:kioskTile notificationType="${type}" />
                            </div>
                        </c:forEach>
                    </div>
                    <div ng-switch="notification.type" class="tile-content">
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:templateKiosk>
