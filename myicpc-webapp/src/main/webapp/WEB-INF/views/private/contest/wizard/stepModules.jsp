<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div class="clearfix">
    <div class="col-sm-6">
        <table class="table">
            <tbody>
            <t:labelTableRow label="module.map" styleClassBody="text-right">
                <form:checkbox path="moduleConfiguration.mapModule" class="moduleSwitch" data-size="small" />
                &nbsp;&nbsp;&nbsp; <a href="javascript:showModuleDescription('mapModule')"><t:glyphIcon icon="eye-open" /></a>
            </t:labelTableRow>
            <t:labelTableRow label="module.codeInsight" styleClassBody="text-right">
                <form:checkbox path="moduleConfiguration.codeInsightModule" class="moduleSwitch" data-size="small" />
                &nbsp;&nbsp;&nbsp; <a href="javascript:showModuleDescription('codeInsightModule')"><t:glyphIcon icon="eye-open" /></a>
            </t:labelTableRow>
            </tbody>
        </table>
    </div>

    <div id="moduleDescriptionWrapper" class="col-sm-6">
        <p class="noSelectedBig"><spring:message code="moduleAdmin.hint" /></p>
        <p id="mapModule" style="display: none;"><spring:message code="module.map.description" /></p>
        <p id="codeInsightModule" style="display: none;"><spring:message code="module.codeInsight.description" /></p>
    </div>
</div>

<script type="application/javascript">
    function showModuleDescription(id) {
        $("#moduleDescriptionWrapper p").hide();
        $("#"+id).show();
    }
</script>