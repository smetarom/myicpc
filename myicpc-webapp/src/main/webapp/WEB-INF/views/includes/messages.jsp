<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="messages">
    <c:if test="${not empty infoMsg}">
        <div class="alert alert-info">${infoMsg}</div>
    </c:if>
    <c:if test="${not empty warnMsg}">
        <div class="alert alert-warning">${warnMsg}</div>
    </c:if>
    <c:if test="${not empty errorMsg}">
        <div class="alert alert-danger">${errorMsg}</div>
    </c:if>
</div>