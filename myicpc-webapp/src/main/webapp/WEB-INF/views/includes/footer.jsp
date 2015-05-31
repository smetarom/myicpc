<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<footer id="footer">
    <button type="button" class="btn btn-info pull-right btn-xs" data-toggle="modal" data-target="#feedbackModal">
        <t:faIcon icon="envelope-o" /> <spring:message code="feedback"/>
    </button>

    <div id="share-container">
        <a href="http://www.twitter.com/icpcnews" title="http://www.twitter.com/icpcnews" target="_blank">
            <t:faIcon icon="twitter-square" />
        </a> &middot;
        <a href="http://www.facebook.com/icpcnews" title="http://www.facebook.com/icpcnews" target="_blank">
            <t:faIcon icon="facebook-square" />
        </a> &middot;
        <a href="http://www.youtube.com/user/ICPCNews" title="http://www.youtube.com/user/ICPCNews" target="_blank">
            <t:faIcon icon="youtube" />
        </a> &middot;
        <a href="http://www.instagram.com/icpcnews" title="http://www.instagram.com/icpcnews" target="_blank">
            <t:faIcon icon="instagram" />
        </a>
    </div>
</footer>

<div class="modal fade" id="feedbackModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">
                    <spring:message code="feedback.title"/>
                </h4>
            </div>
            <spring:url var="formAction" value="/feedback/send"/>
            <form action="${formAction}" method="post" class="form-horizontal">
                <div class="modal-body">
                    <div class="form-group">
                        <label for="feedbackEmail" class="col-lg-2 control-label"><spring:message
                                code="feedback.email"/>:</label>

                        <div class="col-lg-10">
                            <input type="email" name="feedbackEmail" class="form-control" id="feedbackEmail"
                                   placeholder="<spring:message code="feedback.email.placeholder" />">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-2 control-label"><spring:message code="feedback.for"/>:</label>

                        <div class="col-lg-10">
                            <div class="radio">
                                <label> <input type="radio" name="forOption" id="thisPageOption" value="option1"
                                               checked> <spring:message code="feedback.for.thisPage"/> (<span
                                        id="thisPageContainer"></span>)
                                </label>
                            </div>
                            <div class="radio">
                                <label> <input type="radio" name="forOption" value="global"> <spring:message
                                        code="feedback.for.global"/>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="feedbackMsg" class="col-lg-2 control-label"><spring:message
                                code="feedback.msg"/>:</label>

                        <div class="col-lg-10">
                            <textarea name="feedbackMsg" class="form-control" id="feedbackMsg" rows="3"
                                      cols="50"></textarea>
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
            </form>
        </div>
    </div>
</div>