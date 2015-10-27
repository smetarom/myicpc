<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>

    <jsp:attribute name="title">
		  <spring:message code="contestHomeAdmin.title"/>
	</jsp:attribute>

    <jsp:attribute name="breadcrumb">
		  <li class="active"><spring:message code="contestHomeAdmin.title"/></li>
	</jsp:attribute>

    <jsp:body>

        <div class="text-right">
            <t:button href="/private${contestURL}/edit" styleClass="btn-hover"><t:glyphIcon icon="pencil" /> <spring:message code="contestAdmin.edit" /></t:button>
            <t:button href="/private${contestURL}/access" styleClass="btn-hover"><t:glyphIcon icon="lock" /> <spring:message code="nav.admin.contestAccess" /></t:button>
            <t:button href="/private${contestURL}/delete" styleClass="btn-hover"><t:glyphIcon icon="remove" /> <spring:message code="contestAdmin.delete" /></t:button>
        </div>
        <br/>

        <div class="col-md-6 col-sm-12">
            <t:panelWithHeading>
                <jsp:attribute name="heading"><spring:message code="contestHomeAdmin.info"/></jsp:attribute>
                <jsp:body>
                    <table>
                        <tbody>
                            <t:labelTableRow label="contest">${contest.name}</t:labelTableRow>
                            <t:labelTableRow label="contest.code">${contest.code}</t:labelTableRow>
                            <t:labelTableRow label="contest.startTime"><fmt:formatDate value="${contest.startTime}" type="both"/></t:labelTableRow>
                        </tbody>
                    </table>
                    <div class="text-right">
                        <t:button href="/private${contestURL}/overview"><spring:message code="contestHomeAdmin.info.btn" /></t:button>
                    </div>
                </jsp:body>
            </t:panelWithHeading>
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
                    <div id="eventFeedControlPanel">
                    </div>
                    <t:plainForm action="" styleClass="clearfix">
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

                    <hr />

                    <p><spring:message code="admin.panel.feed.upload.hint" /></p>
                    <t:plainForm action="/private${contestURL}/feed/upload" styleClass="form-inline" fileUpload="true">
                        <input type="file" class="form-control" name="eventFeedFile" id="eventFeedFile" accept=".xml" required="required">
                        <t:button context="primary" type="submit"><spring:message code="admin.panel.feed.upload.button" /></t:button>
                    </t:plainForm>

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
            <div id="eventFeedControlPanel">

            </div>
        </div>
        <div class="col-md-6 col-sm-12">
            <t:panelWithHeading panelStyle="warning">
                <jsp:attribute name="heading"><spring:message code="warningAdmin.title"/></jsp:attribute>
                <jsp:body>
                    <c:if test="${empty warnings}">
                        <div class="no-items-available text-success">
                            <t:faIcon icon="thumbs-o-up"/> <spring:message code="warningAdmin.noWarnings"/>
                        </div>
                    </c:if>
                </jsp:body>
            </t:panelWithHeading>
        </div>

        <script type="application/javascript">
            var loadEventFeedStatus = function() {
                $("#eventFeedControlPanel").html('');
                $.get("<spring:url value="/private/${contestURL}/feed/status"/>", function(data) {
                    $("#eventFeedControlPanel").html(data);
                    $('[data-toggle="tooltip"]').tooltip();
                });
            }
            $(function() {
                loadEventFeedStatus();
            });
        </script>
    </jsp:body>
</t:templateAdmin>