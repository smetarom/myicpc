<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>
<tr>
    <td><t:topSubmenuLink labelCode="nav.scoreboard" url="${contestURL}/scoreboard"
                          icon="glyphicon glyphicon-list"/></td>
    <td><t:topSubmenuLink labelCode="nav.scorebar" url="${contestURL}/scorebar"
                          icon="glyphicon glyphicon-align-left"/></td>
</tr>
<tr>
    <td><t:topSubmenuLink labelCode="nav.teams" url="${contestURL}/teams" icon="fa fa-users"/></td>
    <td>
        <c:if test="${moduleSettings.moduleMap}">
            <t:topSubmenuLink labelCode="nav.map" url="${contestURL}/map" icon="glyphicon glyphicon-picture"/>
        </c:if>
    </td>
</tr>
<tr style="border-top: 1px solid white;">
    <td><t:topSubmenuLink labelCode="nav.insight" url="${contestURL}/insight" icon="glyphicon glyphicon-eye-open"/></td>
    <td>
        <c:if test="${moduleSettings.moduleInsightCode}">
            <t:topSubmenuLink labelCode="nav.insight.code" url="${contestURL}/code"
                              icon="fa fa-code"></t:topSubmenuLink>
        </c:if>
    </td>
</tr>