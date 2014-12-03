<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>

<%@attribute name="title" fragment="true" %>
<%@attribute name="headline" fragment="true" %>
<%@attribute name="breadcrumb" fragment="true" %>
<%@attribute name="controls" fragment="true" %>

<compress:html compressCss="true">
    <!DOCTYPE html>
    <html>
    <head>
        <title>
            <c:if test="${not empty title}">
                <jsp:invoke fragment="title" /> &middot;
            </c:if>
            MyICPC
        </title>
        <%@ include file="/WEB-INF/views/includes/headAdmin.jsp" %>
    </head>
    <body>
    <jsp:include page="/WEB-INF/views/private/includes/header_general.jsp"/>
    <div class="admin-header">
        <ol class="breadcrumb">
            <li><a href="<spring:url value="/private/home" />"><spring:message code="nav.admin.home"/></a></li>
            <jsp:invoke fragment="breadcrumb"/>
        </ol>

        <c:if test="${not empty headline}">
            <div>
                <h1>
                    <jsp:invoke fragment="headline"/>
                </h1>
            </div>
        </c:if>
    </div>
    <div id="body" class="wrapper clearfix">
        <c:if test="${not empty controls}">
            <div class="pull-right">
                <jsp:invoke fragment="controls" />
            </div>
            <br class="clear" />
        </c:if>

        <%@ include file="/WEB-INF/views/includes/messages.jsp" %>

        <jsp:doBody/>
    </div>
    <br/>
    <footer id="footer" class="text-center">
        <c:if test="${not empty currentDate}">
            <span class="glyphicon glyphicon-time"></span>
            <spring:message code="admin.systemTime"/>
            <fmt:formatDate value="${currentDate}" type="both" dateStyle="medium" timeStyle="medium"/> |
            <spring:message code="admin.contact"/>: smetarom@gmail.com
        </c:if>
    </footer>
    <script type="application/javascript">
        $(function () {
            $('[data-toggle="tooltip"]').tooltip();
        });
    </script>
    </body>
    </html>
</compress:html>