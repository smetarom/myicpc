<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>
<t:springInput id="contestCode" labelCode="contest.code" path="code" required="true" hintCode="contest.code.hint"/>
<t:springInput id="wsToken" labelCode="contest.wsToken" path="webServiceSettings.wsCMToken"
               hintCode="contest.wsToken.hint"/>
<div class="form-group">
    <label class="col-sm-3 control-label"></label>

    <div class="col-sm-9">
        <button type="button" id="runWebServiceCheck" class="btn btn-warning">
            <spring:message code="contestAdmin.wsCheck.button"/>
        </button>
        <t:spinnerIcon id="webServiceCheckButton" size="18" hidden="true" />
        <div id="runWebServiceContainer"></div>
    </div>
</div>

<script type="text/javascript">
    $(function () {
        $("#runWebServiceCheck").click(function () {
            $("#webServiceCheckButton").show();
            $.get('<spring:url value="/private/contest/checkWebService" />', {contestCode: $("#contestCode").val(), wsToken: $("#wsToken").val()}, function (data) {
                $("#runWebServiceContainer").html(data);
            }).always(function() {
                $("#webServiceCheckButton").hide();
            });
        });
    });
</script>