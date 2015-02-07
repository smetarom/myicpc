<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<button type="button" class="btn btn-default" id="legendBtn">
    <spring:message code="insight.showLegend" />
</button>

<ul class="" style="margin-top: 10px; list-style: none; display: none;" id="legend">
    <c:forEach var="judgement" items="${judgements}">
        <li><span style="display: inline-block; width: 15px; height: 15px; background-color: ${judgement.color}"> </span> ${judgement.name} (${judgement.code})</li>
    </c:forEach>
</ul>

<script type="text/javascript">
    $(function() {
        $("#legendBtn").click(function() {
            $("#legend").slideToggle(800);
        });
    });
</script>