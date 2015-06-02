<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<div class="modal fade" id="feedbackModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">
                    <spring:message code="feedback.title"/>
                </h4>
            </div>
            <t:plainForm action="/${contestCode}/feedback-form">
                <div class="modal-body">
                    <div class="form-group">
                        <label for="feedbackEmail" class="col-lg-2 control-label">
                            <spring:message code="feedback.email"/>:
                        </label>
                        <div class="col-lg-10">
                            <input type="email" name="feedbackEmail" class="form-control" id="feedbackEmail"
                                   placeholder="<spring:message code="feedback.email.placeholder" />">
                        </div>
                    </div>
                    <div class="form-group hidden">
                        <label class="col-lg-2 control-label">
                            Leave this empty:
                        </label>
                        <div class="col-lg-10">
                            <input name="url" class="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="feedbackMsg" class="col-lg-2 control-label">
                            <spring:message code="feedback.msg"/>:*
                        </label>
                        <div class="col-lg-10">
                            <textarea name="feedbackMsg" class="form-control" id="feedbackMsg" rows="3"
                                      cols="50" required="required"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <spring:message code="close"/>
                    </button>
                    <button type="submit" class="btn btn-primary">
                        <spring:message code="feedback.send"/>
                    </button>
                </div>
            </t:plainForm>
        </div>
    </div>
</div>