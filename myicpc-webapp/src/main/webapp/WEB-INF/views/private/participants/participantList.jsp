<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateAdmin>
  <jsp:attribute name="title">
    <spring:message code="participantAdmin.title" />
  </jsp:attribute>

  <jsp:attribute name="breadcrumb">
        <li class="active"><spring:message code="participantAdmin.title"/></li>
  </jsp:attribute>

  <jsp:attribute name="headline">
    <spring:message code="participantAdmin.title" />
  </jsp:attribute>

  <jsp:body>
    <%@ include file="/WEB-INF/views/private/participants/fragment/participantsCreateModal.jsp"%>
    <t:button modalOpenId="createNewPerson" styleClass="btn-hover pull-right"><t:glyphIcon icon="plus" /> <spring:message code="participantAdmin.create" /></t:button>
    <!-- Nav tabs -->
    <ul class="nav nav-tabs">
      <li class="active"><a href="#allPeople" data-toggle="tab"><spring:message code="participantAdmin.all" /></a></li>
      <li><a href="#contestants" data-toggle="tab"><spring:message code="participantAdmin.contestants" /></a></li>
      <li><a href="#coaches" data-toggle="tab"><spring:message code="participantAdmin.coaches" /></a></li>
      <li><a href="#attendees" data-toggle="tab"><spring:message code="participantAdmin.attendees" /></a></li>
      <li><a href="#staff" data-toggle="tab"><spring:message code="participantAdmin.staff" /></a></li>
    </ul>

    <!-- Tab panes -->
    <div class="tab-content">
      <div class="tab-pane active" id="allPeople">
        <c:set var="participants" value="${allPeople}" />
        <%@ include file="/WEB-INF/views/private/participants/fragment/participantsTable.jsp"%>
      </div>
      <div class="tab-pane" id="contestants">
        <c:set var="participants" value="${contestants}" />
        <%@ include file="/WEB-INF/views/private/participants/fragment/participantsTable.jsp"%>
      </div>
      <div class="tab-pane" id="coaches">
        <c:set var="participants" value="${coaches}" />
        <%@ include file="/WEB-INF/views/private/participants/fragment/participantsTable.jsp"%>
      </div>
      <div class="tab-pane" id="attendees">
        <c:set var="participants" value="${attendees}" />
        <%@ include file="/WEB-INF/views/private/participants/fragment/participantsTable.jsp"%>
      </div>
      <div class="tab-pane" id="staff">
        <c:set var="participants" value="${staff}" />
        <%@ include file="/WEB-INF/views/private/participants/fragment/participantsTable.jsp"%>
      </div>
    </div>

    <script type="application/javascript">
      $(function() {
        $("#participantFilter select").chosen();
      });
    </script>

  </jsp:body>
</t:templateAdmin>