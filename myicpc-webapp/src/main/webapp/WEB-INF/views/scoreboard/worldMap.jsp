<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
        <%@ include file="/WEB-INF/views/includes/nvd3Dependencies.jsp" %>
        <script src="<c:url value='/js/myicpc/controllers/worldMap.js'/>"></script>
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
            </div>
        </div>

        <script type="application/javascript">
            $(function() {
                var ngController = angular.element($("#worldMap")).scope();
                ngController.renderMap('${r.contextPath}', {});
            });
        </script>
    </jsp:body>

</t:template>
