<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>
<tr>
    <td><t:topSubmenuLink labelCode="nav.quest" url="${contestURL}/quest" icon="glyphicon glyphicon-screenshot"/></td>
    <td><t:topSubmenuLink labelCode="nav.quest.challenges" url="${contestURL}/quest/challenges"
                          icon="fa fa-trophy"/></td>
</tr>
<tr>
    <td><a href="<spring:url value="${contestURL}/quest/leaderboard" />"><span
            class="glyphicon glyphicon-list"></span><br/> <spring:message code="nav.quest.leaderboard"/></a></td>
    <td><a href="<spring:url value="${contestURL}/techtrek" />"><span class="fa fa-text-width"></span><br/>
        <spring:message code="nav.techtrek"/></a></td>
</tr>