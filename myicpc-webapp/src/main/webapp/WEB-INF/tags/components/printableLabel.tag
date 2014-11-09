<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="enum" type="com.myicpc.commons.enums.GeneralEnum" required="true" %>

<spring:message code="${enum.code}" text="${enum.label}" />


