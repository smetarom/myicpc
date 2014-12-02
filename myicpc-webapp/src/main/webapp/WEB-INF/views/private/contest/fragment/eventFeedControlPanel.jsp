<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:panelWithHeading>
  <jsp:attribute name="heading">
    <spring:message code="contestHomeAdmin.feedControl"/>
    <div class="pull-right"><a href="javascript:void(0);" onclick="loadEventFeedStatus()"><t:glyphIcon icon="refresh" /></a></div>
  </jsp:attribute>

  <jsp:body>
    <t:alert context="${isFeedRunning ? 'success' : 'danger'}"><spring:message code="${isFeedRunning ? 'admin.panel.feed.status.running' : 'admin.panel.feed.status.stopped'}" /></t:alert>

    <div class="clearfix">
      <div class="col-sm-6">
        <t:plainForm action="/private${contestURL}/feed/restart">
          <spring:message var="confirmMsg" code="admin.panel.feed.reset.confirm" />
          <t:button type="submit" context="danger" onclick="return confirm('${confirmMsg}');"><spring:message code="admin.panel.feed.reset" /></t:button>
          <t:helpIcon helpTextCode="admin.panel.feed.reset.hint" />
        </t:plainForm>
      </div>
      <div class="col-sm-6 text-right">
        <t:plainForm action="/private${contestURL}/feed/resume">
          <t:button type="submit" context="warning"><spring:message code="admin.panel.feed.resume" /></t:button>
          <t:helpIcon helpTextCode="admin.panel.feed.resume.hint" />
        </t:plainForm>
      </div>
    </div>

  </jsp:body>
</t:panelWithHeading>