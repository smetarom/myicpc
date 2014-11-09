<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="id" required="true" %>
<%@attribute name="title" fragment="true" %>
<%@attribute name="footer" fragment="true" %>

<div class="modal fade" id="${id}" tabindex="-1" role="dialog" aria-labelledby="${id}Label" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <c:if test="${not empty title}">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only"><spring:message code="close" /></span></button>
                    <h4 class="modal-title" id="${id}Label"><jsp:invoke fragment="title" /></h4>
                </div>
            </c:if>
            <div class="modal-body">
                <jsp:doBody />
            </div>
            <c:if test="${not empty footer}">
                <div class="modal-footer">
                    <jsp:invoke fragment="footer" />
                </div>
            </c:if>
        </div>
    </div>
</div>


