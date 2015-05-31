<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>

<%@attribute name="title" fragment="true" %>
<%@attribute name="head" fragment="true" %>
<%@attribute name="javascript" fragment="true" %>
<%@attribute name="headline" fragment="true" %>
<%@attribute name="headlineRight" fragment="true" %>
<%@attribute name="submenu" fragment="true" %>
<%@attribute name="topContent" fragment="true" %>

<t:template>
    <jsp:attribute name="head">
		<jsp:invoke fragment="head"/>
	</jsp:attribute>

    <jsp:attribute name="title">
		<jsp:invoke fragment="title"/>
	</jsp:attribute>

    <jsp:attribute name="javascript">
        <script type="application/javascript">
            $(window).scroll(setFixedSubmenuHeight);
            $(window).resize(setFixedSubmenuHeight);
            $(setFixedSubmenuHeight);
        </script>

		<jsp:invoke fragment="javascript"/>
	</jsp:attribute>

	<jsp:attribute name="headline">
		<jsp:invoke fragment="headline"/>
	</jsp:attribute>

    <jsp:body>
        <jsp:invoke fragment="topContent"/>
        <div class="clearfix">
            <div class="col-sm-4" id="fixedSidebar">
                <jsp:invoke fragment="submenu"/>
            </div>
            <div class="col-sm-8" id="fixedContent">
                <jsp:doBody/>
            </div>
        </div>
    </jsp:body>
</t:template>
