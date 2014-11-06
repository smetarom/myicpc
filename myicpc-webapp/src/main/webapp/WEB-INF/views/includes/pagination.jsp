<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="text-center">
    <ul class="pagination">
        <c:if test="${not pager.firstPage}">
            <li><a href="?page.page=${pager.number}">&laquo;</a></li>
        </c:if>
        <c:forEach begin="${(pager.number - 5) > 0 ? pager.number - 5 : 1}"
                   end="${(pager.number + 5) < pager.totalPages ? (pager.number + 5) : pager.totalPages}" var="page">
            <c:choose>
                <c:when test='${(page - 1) == pager.number}'>
                    <li class="active"><a>${page}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="?page.page=${page}">${page}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${not pager.lastPage}">
            <li><a href="?page.page=${pager.number+2}">&raquo;</a></li>
        </c:if>
    </ul>
</div>