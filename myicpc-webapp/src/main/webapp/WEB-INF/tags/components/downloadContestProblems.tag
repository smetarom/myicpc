<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty contestProblemPDFURL}">
    <div id="download-problems">
        <spring:message code="contest.problemPDF" arguments="${contest.shortName}"/>
        <a href="${contestProblemPDFURL}" target="_blank"><spring:message code="contest.problemPDF.link"/></a>
    </div>
</c:if>
