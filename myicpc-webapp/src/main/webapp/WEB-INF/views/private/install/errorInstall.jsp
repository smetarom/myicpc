<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateEmpty>

    <jsp:body>
        <div class="page-header">
            <h1>&nbsp;<spring:message code="installAdmin.title"/></h1>
        </div>

        <div class="col-sm-12">
            <div class="alert alert-danger" role="alert">
                <spring:message code="installAdmin.error" />
            </div>
        </div>

    </jsp:body>

</t:templateEmpty>