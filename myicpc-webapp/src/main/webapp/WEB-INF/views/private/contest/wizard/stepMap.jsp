<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div class="form-group">
    <label class="col-sm-3 control-label"><spring:message code="contest.module.map"/>: </label>

    <div class="col-sm-9">
        <input type="checkbox" id="mapModuleSwitch" data-size="small" checked="checked"/>
    </div>
    <div class="col-sm-offset-3 col-sm-9">
        TODO: explain map module
    </div>
</div>
<t:springTextarea id="teamCoordinates" labelCode="contest.map.teamCoordinates" path="mapConfiguration.teamCoordinates"
                  rows="5"/>
<div class="form-group">
    <div class="col-sm-offset-3 col-sm-9">
        <button type="button" id="webServiceTeamCoords" class="btn btn-warning"><spring:message
                code="contest.map.teamCoordinates.button"/></button>
    </div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label"><spring:message code="contest.map.zoomable"/>:* </label>

    <div class="col-sm-9">
        <label for="showTeamNamesTrue" class="normal-label">
            <form:radiobutton path="mapConfiguration.zoomableMap" id="showTeamNamesTrue" value="true"/>
            &nbsp;<spring:message code="contest.map.zoomable.true"/>
        </label>
        <br/>
        <label for="showTeamNamesFalse" class="normal-label">
            <form:radiobutton path="mapConfiguration.zoomableMap" id="showTeamNamesFalse" value="false"/>
            &nbsp;<spring:message code="contest.map.zoomable.false"/>
        </label>

        <div id="mapConfigContainer" class="responsive">

        </div>
    </div>
</div>
<form:hidden path="mapConfiguration.mapConfig" id="mapConfig"/>


<script type="text/javascript">
    function loadMapConfig() {
        $.get("<spring:url value="/private/contest/map/config" />", function (data) {
            $("#mapConfigContainer").html(data);

        });
    }

    $(function () {
        // TODO enable bootstrap switch
//	$("#mapModuleSwitch").bootstrapSwitch();

        if ($("#showTeamNamesFalse").is(":checked")) {
            loadMapConfig();
        }

        $("#showTeamNamesFalse").change(function () {
            $("#mapConfigContainer").show();
            loadMapConfig();
        });
        $("#showTeamNamesTrue").change(function () {
            $("#mapConfigContainer").hide();
            loadMapConfig();
        });

        $("#showTeamNamesTrue").closest("form").submit(function (event) {
            if ($("#showTeamNamesFalse").is(":checked")) {
                $("#mapConfig").val(getMapConfigResult());
            } else {
                $("#mapConfig").val('${defaultMapConfig}');
            }
        });
    })
</script>