<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>--%>

<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>
<%@ taglib prefix="util" uri="http://myicpc.baylor.edu/functions"%>

<c:set var="ctx" value="${pageContext['request'].contextPath}"/>
<c:set var="serverURL"
       value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}"/>
<c:set var="r" value="${pageContext.request}"/>
