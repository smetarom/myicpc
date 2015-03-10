<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="questAdmin.votes.title" />
	</jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="questAdmin.votes.title" />
	</jsp:attribute>

	<jsp:attribute name="breadcrumb">
	    <li class="active"><spring:message code="questAdmin.votes.title" /></li>
	</jsp:attribute>

    <jsp:body>
        <div role="tabpanel" class="col-sm-12">
            <!-- Nav tabs -->
            <ul class="nav nav-pills" role="tablist">
                <li role="presentation" class="active"><a href="#in-progress" aria-controls="home" role="tab" data-toggle="pill"><spring:message code="questAdmin.votes.inProgress.title" /></a></li>
                <li role="presentation"><a href="#history" aria-controls="profile" role="tab" data-toggle="pill"><spring:message code="questAdmin.votes.winners.title" /></a></li>
            </ul>
            <br/>
            <!-- Tab panes -->
            <div class="tab-content">
                <div role="tabpanel" class="tab-pane active" id="in-progress">
                    <%@ include file="/WEB-INF/views/private/quest/fragment/votesInProgress.jsp"%>
                </div>
                <div role="tabpanel" class="tab-pane" id="history">
                    <%@ include file="/WEB-INF/views/private/quest/fragment/voteWinners.jsp"%>
                </div>
            </div>

        </div>
    </jsp:body>
</t:templateAdmin>