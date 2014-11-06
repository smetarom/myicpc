<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<c:forEach var="width" items="${widths}">
    <h3>
        <spring:message code="mapAdmin.maxWidth" arguments="${width}px"/>
    </h3>

    <form class="form-inline">
        <t:input labelCode="mapAdmin.ratio" id="ratio${width}" size="3"/>
        <t:input labelCode="mapAdmin.scale" id="scale${width}" size="10"/>
        <t:input labelCode="mapAdmin.translate" id="translate${width}" size="10"/>
        <t:input labelCode="mapAdmin.pointSize" id="point${width}" size="10"/>
        <button type="button" class="btn btn-default" onclick="readConfValues(${width})">
            <spring:message code="mapAdmin.rerender"/>
        </button>
    </form>

    <br/>

    <svg id="map${width}" style="border: 3px solid gray;"></svg>
</c:forEach>


<script src="<c:url value="/js/d3/d3.min.js" />" defer></script>
<script type="text/javascript">
    var confs = {};

    function renderMap(appPath, conf) {
        conf = $.extend({
            width: 1000,
            ratio: 0.65,
            scale: 150,
            translate: [ 490, 380 ]
        }, conf);
        conf.height = Math.round(conf.width * conf.ratio);

        var projection = d3.geo.mercator().scale(conf.scale).translate(conf.translate);
        var path = d3.geo.path().projection(projection);
        var svg = d3.select("#map" + conf.width).attr("width", conf.width).attr("height", conf.height);

        d3.json(appPath + "/static/maps/world-countries.json", function (collection) {
            feature = svg.selectAll("path").data(collection.features).enter().append("svg:path").attr("d", path).attr("id", function (d) {
                return d.id;
            });
        });
    }

    function readConfValues(width) {
        confs[width].ratio = parseFloat($("#ratio" + width).val());
        confs[width].scale = parseInt($("#scale" + width).val());
        confs[width].translate = $("#translate" + width).val().split(',');
        confs[width].circleSize = parseInt($("#point" + width).val());
        $("#map" + width).empty();
        renderMap('${r.contextPath}', confs[width]);
    }

    function prefillConfValues(width) {
        $("#ratio" + width).val(confs[width].ratio);
        $("#scale" + width).val(confs[width].scale);
        $("#translate" + width).val(confs[width].translate);
        $("#point" + width).val(confs[width].circleSize);
    }

    function getMapConfigResult() {
        var arr = [];
        for (c in confs) {
            arr.push(confs[c]);
        }
        return JSON.stringify(arr);
    }

    $(function () {
        var c = ${mapConfig};
        for (var i = 0; i < c.length; i++) {
            confs[c[i].width] = c[i];
            prefillConfValues(c[i].width);
            renderMap('${r.contextPath}', c[i]);
        }
    });
</script>