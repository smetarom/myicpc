<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="pager" required="true" type="org.springframework.data.domain.PageImpl" %>
<div class="text-center">
    <ul class="pagination">
        <c:if test="${pager.number > 0}">
            <li><a href="?page=${pager.number - 1}">&laquo;</a></li>
        </c:if>
        <c:forEach begin="${pager.number - 5 > 0 ? pager.number - 5 : 1}"
                   end="${pager.number + 5 < pager.totalPages ? (pager.number + 5) : pager.totalPages}"
                   var="page">
            <c:choose>
                <c:when test='${(page - 1) == pager.number}'>
                    <li class="active"><a>${page}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="?page=${page-1}">${page}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${not pager.isLast()}">
            <li><a href="?page=${pager.number + 1}">&raquo;</a></li>
        </c:if>
    </ul>
</div>
