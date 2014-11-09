<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="messages">
    <c:if test="${not empty infoMsg}">
        <div class="alert alert-info alert-dismissible">
            <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            ${infoMsg}
        </div>
    </c:if>
    <c:if test="${not empty warnMsg}">
        <div class="alert alert-warning alert-dismissible">
            <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            ${warnMsg}
        </div>
    </c:if>
    <c:if test="${not empty errorMsg}">
        <div class="alert alert-danger alert-dismissible">
            <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            ${errorMsg}
        </div>
    </c:if>
</div>