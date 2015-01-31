<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">

        <script type='text/javascript' src='https://www.google.com/jsapi'></script>
        <script src="<c:url value='/js/myicpc/controllers/worldMap.js'/>"></script>
    </jsp:attribute>
    <jsp:attribute name="title">
        <spring:message code="nav.scorebar"/>
    </jsp:attribute>

    <jsp:body>
        <script type='text/javascript'>

        </script>
        <div id="world-map-container">
            <div id="world-map" class="col-sm-9">s</div>
            <div id="map-scoreboard" class="col-sm-3"></div>
        </div>
    </jsp:body>

</t:template>
