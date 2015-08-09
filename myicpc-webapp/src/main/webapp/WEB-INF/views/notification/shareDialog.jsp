<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div class="modal fade" id="shareNotificationDialog" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><spring:message code="share" /> ${notification.title}</h4>
            </div>
            <div class="modal-content">
                <table class="table">
                    <tr>
                        <td><t:facebookShare notification="${notification}" /></td>
                    </tr>
                    <tr>
                        <td><t:twitterShare notification="${notification}" /></td>
                    </tr>
                    <tr>
                        <td><t:googleShare notification="${notification}" /></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>