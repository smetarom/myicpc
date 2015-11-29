<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>
<%@ taglib prefix="util" uri="http://myicpc.baylor.edu/functions" %>

<tr>
    <td><t:topSubmenuLink labelCode="nav.scoreboard" url="${contestURL}/scoreboard"
                          icon="glyphicon glyphicon-list"/></td>
    <td><t:topSubmenuLink labelCode="nav.scorebar" url="${contestURL}/scorebar"
                          icon="glyphicon glyphicon-align-left"/></td>
</tr>
<tr>
    <td><t:topSubmenuLink labelCode="nav.teams" url="${contestURL}/teams" icon="fa fa-users"/></td>
    <td>
        <c:if test="${util:mapModuleEnabled(contest)}">
            <t:topSubmenuLink labelCode="nav.map" url="${contestURL}/map" icon="glyphicon glyphicon-picture"/>
        </c:if>
    </td>
</tr>
<tr>
    <td><t:topSubmenuLink labelCode="nav.insight" url="${contestURL}/insight" icon="glyphicon glyphicon-eye-open"/></td>
    <td>
        <c:if test="${util:codeInsightModuleEnabled(contest)}">
            <t:topSubmenuLink labelCode="nav.insight.code" url="${contestURL}/insight#/code"
                              icon="fa fa-code"></t:topSubmenuLink>
        </c:if>
    </td>
</tr>