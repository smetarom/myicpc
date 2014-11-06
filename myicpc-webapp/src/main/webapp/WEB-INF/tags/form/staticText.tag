<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="labelCode" required="true" %>

<div class="form-group">
    <label class="col-sm-3 control-label"><spring:message code="${labelCode}"/>:</label>

    <div class="col-sm-9">
        <p class="form-control-static">
            <jsp:doBody/>
        </p>
    </div>
</div>
