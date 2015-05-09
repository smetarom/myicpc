<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:panelWithHeading>
  <jsp:attribute name="heading">
    <spring:message code="contestHomeAdmin.feedControl"/>
    <div class="pull-right">
      <a href="javascript:void(0);" onclick="loadEventFeedStatus()" title="<spring:message code="refresh"/>"><t:glyphIcon icon="refresh" /></a>
      &nbsp;
      <a href="javascript:void(0);" data-toggle="modal" data-target="#feedControlModal" title="<spring:message code="info"/>"><t:faIcon icon="info-circle" /></a>
    </div>
  </jsp:attribute>

  <jsp:body>
    <t:alert context="${isFeedRunning ? 'success' : 'danger'}"><spring:message code="${isFeedRunning ? 'admin.panel.feed.status.running' : 'admin.panel.feed.status.stopped'}" /></t:alert>

      <t:plainForm action="">
        <div class="col-sm-12 text-center">
            <spring:message var="confirmMsg" code="admin.panel.feed.reset.confirm" />
            <t:submitButton formaction="/private${contestURL}/feed/restart" onclick="return confirm('${confirmMsg}');" context="danger"><spring:message code="admin.panel.feed.reset" /></t:submitButton>

            <spring:message var="confirmMsg" code="admin.panel.feed.resume.confirm" />
            <t:submitButton formaction="/private${contestURL}/feed/resume" onclick="return confirm('${confirmMsg}');" context="warning"><spring:message code="admin.panel.feed.resume" /></t:submitButton>

            <spring:message var="confirmMsg" code="admin.panel.feed.stop.confirm" />
            <t:submitButton formaction="/private${contestURL}/feed/stop" onclick="return confirm('${confirmMsg}');"><spring:message code="admin.panel.feed.stop" /></t:submitButton>

            <spring:message var="confirmMsg" code="admin.panel.feed.clear.confirm" />
            <t:submitButton formaction="/private${contestURL}/feed/clear" onclick="return confirm('${confirmMsg}');"><spring:message code="admin.panel.feed.clear" /></t:submitButton>
          </div>
      </t:plainForm>
      </form>
  </jsp:body>
</t:panelWithHeading>

<t:modalWindow id="feedControlModal">
    <jsp:attribute name="title"><spring:message code="contestHomeAdmin.feedControl.help"/></jsp:attribute>
    <jsp:body>
        <h5><spring:message code="admin.panel.feed.reset" /></h5>
        <p><spring:message code="admin.panel.feed.reset.hint" /></p>

        <h5><spring:message code="admin.panel.feed.resume" /></h5>
        <p><spring:message code="admin.panel.feed.resume.hint" /></p>

        <h5><spring:message code="admin.panel.feed.stop" /></h5>
        <p><spring:message code="admin.panel.feed.stop.hint" /></p>

        <h5><spring:message code="admin.panel.feed.clear" /></h5>
        <p><spring:message code="admin.panel.feed.clear.hint" /></p>
    </jsp:body>
</t:modalWindow>