<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<%@attribute name="title" fragment="true" %>
<%@attribute name="headline" fragment="true" %>
<%@attribute name="breadcrumb" fragment="true" %>
<%@attribute name="submenu" fragment="true" %>
<%@attribute name="topContent" fragment="true" %>

<t:templateGeneralAdmin>
    <jsp:attribute name="title">
		<jsp:invoke fragment="title"/>
	</jsp:attribute>

	<jsp:attribute name="headline">
		<jsp:invoke fragment="headline"/>
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <jsp:invoke fragment="breadcrumb"/>
	</jsp:attribute>


    <jsp:body>
        <jsp:invoke fragment="topContent"/>
        <div class="clearfix">
            <div class="col-sm-4 sidebar-offcanvas" id="sidebar">
                <jsp:invoke fragment="submenu"/>
            </div>
            <div class="col-sm-8">
                <jsp:doBody/>
            </div>
        </div>
    </jsp:body>
</t:templateGeneralAdmin>