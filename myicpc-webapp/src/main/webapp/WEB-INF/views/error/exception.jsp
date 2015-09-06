<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateError>
    <jsp:attribute name="head">
        <style type="text/css">
          body {
            background-color: #EF3C56;
          }
        </style>
    </jsp:attribute>

  <jsp:body>
    <div class="error-container center-block">
      <t:panelWithHeading>
        <div style="font-size: 4em;">
          <t:faIcon icon="times" />
        </div>
        <h1><spring:message code="error.exception.title" /></h1>

        <p style="font-size: 1.2em;">
          <spring:message code="error.exception.body.title" />
        </p>
        <br />
        <p>Please <a href="<spring:url value="${privateMode ? '/private/home' : '/'}" />">click here</a> to return to the home screen.</p>

        <p>If you are unable to proceed, please contact us.</p>

        <spring:url var="formAction" value="/feedback/send" />
        <form action="${formAction}" method="post" class="form-horizontal">
          <div class="modal-footer">
            <div class="form-group">
              <label for="feedbackEmail" class="col-lg-2 control-label"><spring:message code="feedback.email" />:</label>
              <div class="col-lg-10">
                <input type="email" name="feedbackEmail" class="form-control" id="feedbackEmail" placeholder="<spring:message code="feedback.email.placeholder" />">
              </div>
            </div>
            <div class="form-group">
              <label for="feedbackMsg" class="col-lg-2 control-label"><spring:message code="feedback.msg" />:</label>
              <div class="col-lg-10">
                <textarea name="feedbackMsg" class="form-control" id="feedbackMsg" rows="3" cols="50"></textarea>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <input type="hidden" name="forOption" id="thisPageOption" value="option1">
            <c:if test="${privateMode}">
              <input type="hidden" name="private" value="true">
            </c:if>
            <input type="hidden" name="exceptionMessage" id="exceptionMessage" value="${exception}">
            <button type="submit" class="btn btn-primary">
              <spring:message code="feedback.send" />
            </button>
          </div>
        </form>
      </t:panelWithHeading>
    </div>

    <script type="text/javascript">
      $(function() {
        $("#thisPageOption").val(window.location.href);
        $("#thisPageContainer").html(window.location.href);

        // clean cookies
        if (isCookieValid('followedTeams') === false) {
          setCookie('followedTeams', '', -1, '${ctx}');
        }
        if (isCookieValid('ignoreFeaturedNotifications') === false) {
          setCookie('ignoreFeaturedNotifications', '', -1, '${ctx}');
        }
        if (isCookieValid('scheduleRoles') === false) {
          setCookie('scheduleRoles', '', -1, '${ctx}');
        }
        if (isCookieValid('answeredPolls') === false) {
          setCookie('answeredPolls', '', -1, '${ctx}');
        }
        if (isCookieValid('quest-vote-round') === false) {
          setCookie('quest-vote-round', '', -1, '${ctx}');
        }
        if (isCookieValid('quest-voted-for') === false) {
          setCookie('quest-voted-for', '', -1, '${ctx}');
        }
      });
    </script>
  </jsp:body>
</t:templateError>
