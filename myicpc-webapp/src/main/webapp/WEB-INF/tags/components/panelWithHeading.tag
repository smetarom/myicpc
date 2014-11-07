<%@ tag language="java" pageEncoding="UTF-8" %>
<%@attribute name="heading" fragment="true" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title"><jsp:invoke fragment="heading"/></h3>
    </div>
    <div class="panel-body">
        <jsp:doBody/>
    </div>
</div>
