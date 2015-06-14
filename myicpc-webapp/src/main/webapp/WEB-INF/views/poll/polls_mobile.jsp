<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="title">
        <spring:message code="nav.polls" />
    </jsp:attribute>

    <jsp:attribute name="headline">
        <spring:message code="nav.polls" />
    </jsp:attribute>

    <jsp:attribute name="javascript">
        <%@ include file="/WEB-INF/views/includes/nvd3Dependencies.jsp" %>
        <script src="<c:url value='/js/myicpc/controllers/poll.js'/>" defer></script>
        <script type="application/javascript">
            $(function() {
                var ngController = angular.element($("#pollController")).scope();
                var polls = ${not empty pollData ? pollData : '[]'};
                ngController.init('${r.contextPath}', '${contest.code}', polls);
                startSubscribe('${r.contextPath}', '${contest.code}', 'poll', updatePollView, ngController);
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <div ng-app="poll" class="col-xs-12">
            <div id="pollController" ng-controller="pollsCtrl"></div>
            <div ng-view></div>
        </div>
    </jsp:body>

</t:template>
