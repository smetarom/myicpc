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
