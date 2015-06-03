<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>
    <jsp:attribute name="head">
        <%@ include file="/WEB-INF/views/includes/nvd3Dependencies.jsp" %>
        <script src="<c:url value='/js/myicpc/controllers/insight.js'/>" defer></script>

    </jsp:attribute>
    <jsp:attribute name="title">
        <spring:message code="nav.insight"/>
    </jsp:attribute>
    <jsp:attribute name="headline">
        <span id="insightHeadline"><spring:message code="insight.problems" /></span>
    </jsp:attribute>

    <jsp:body>
        <c:if test="${submissionCount > 0}">
            <div id="insight" ng-app="insight">
                <t:secondLevelSubmenu isMobile="${sitePreference.mobile}">
                    <li class="dropdown">
                        <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><spring:message code="insight.nav.problems" /> <span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="#/problems"><spring:message code="insight.nav.problems.all" /></a></li>
                            <li class="divider"></li>
                            <c:forEach var="problem" items="${problems}">
                                <li><a href="#/problem/${problem.code}"><spring:message code="insight.nav.problem" arguments="${problem.code}"/></a></li>
                            </c:forEach>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><spring:message code="insight.nav.languages" /> <span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="#/languages"><spring:message code="insight.nav.languages.all" /></a></li>
                            <li class="divider"></li>
                            <c:forEach var="language" items="${languages}">
                                <li><a href="#/language/${language.name}"><spring:message code="insight.nav.language" arguments="${language.name}"/></a></li>
                            </c:forEach>
                        </ul>
                    </li>
                    <li><a href="#/code"><spring:message code="insight.nav.code" /></a></li>
                </t:secondLevelSubmenu>

                <div class="clearfix">
                    <div ng-view></div>
                </div>
            </div>
        </c:if>
        <c:if test="${submissionCount == 0}">
            <div class="no-items-available">
                <h2><spring:message code="insight.unavailable" /></h2>
            </div>
        </c:if>
    </jsp:body>

</t:template>
