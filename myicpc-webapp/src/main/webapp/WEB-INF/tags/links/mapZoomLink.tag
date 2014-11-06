<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="zoomTo" required="true" %>
<%@ attribute name="labelCode" required="true" %>
<a href="javascript:void(0);" onclick="zoomToArea('${zoomTo}'); return false;"><spring:message code="${labelCode}"/></a>
