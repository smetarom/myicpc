<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<c:if test="${not hideTitle}">
    <h2 style="margin-top: 0">${challenge.name} <small>#${challenge.hashtag}</small></h2>
</c:if>

<div class="col-sm-12">
    <div class="center-block" style="max-width: 500px">
        <p>${challenge.description}</p>
    </div>
    <div class="text-center">
        <t:button context="primary"><spring:message code="quest.participateThisChallenge" /></t:button>
    </div>
</div>
<br class="clear"/>
<br/>
<div class="col-sm-4">
    <h4><spring:message code="quest.status.accepted" /></h4>
</div>
<div class="col-sm-4">
    <h4><spring:message code="quest.status.pending" /></h4>
</div>
<div class="col-sm-4">
    <h4><spring:message code="quest.status.rejected" /></h4>
</div>

