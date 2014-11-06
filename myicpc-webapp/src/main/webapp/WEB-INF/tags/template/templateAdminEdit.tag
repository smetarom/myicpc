<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<%@attribute name="headline" fragment="true" %>
<%@attribute name="breadcrumb" fragment="true" %>
<%@attribute name="submenu" fragment="true" %>

<t:templateAdmin>
	<jsp:attribute name="headline">
		<jsp:invoke fragment="headline"/>
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
	    <jsp:invoke fragment="breadcrumb"/>
	</jsp:attribute>


    <jsp:body>
        <div class="container">
            <jsp:doBody/>
        </div>
    </jsp:body>
</t:templateAdmin>