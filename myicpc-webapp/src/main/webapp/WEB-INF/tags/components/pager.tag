<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="pager" required="true" type="org.springframework.data.domain.PageImpl" %>
<div class="text-center">
    <ul class="pagination">
        <c:if test="${not pager.firstPage}">
            <li><a href="?page=${pager.getNumber()}">&laquo;</a></li>
        </c:if>
        <c:forEach begin="${pager.getNumber() - 5 > 0 ? pager.getNumber() - 5 : 1}"
                   end="${pager.getNumber() + 5 < pager.getTotalPages() ? (pager.getNumber() + 5) : pager.getTotalPages()}"
                   var="page">
            <c:choose>
                <c:when test='${(page - 1) == pager.getNumber()}'>
                    <li class="active"><a>${page}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="?page=${page-1}">${page}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${not pager.lastPage}">
            <li><a href="?page=${pager.getNumber()+1}">&raquo;</a></li>
        </c:if>
    </ul>
</div>
