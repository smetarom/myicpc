<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>
<tr>
    <td><t:topSubmenuLink labelCode="nav.quest" url="${contestURL}/quest" icon="glyphicon glyphicon-screenshot"/></td>
    <td><t:topSubmenuLink labelCode="nav.quest.challenges" url="${contestURL}/quest/challenges" icon="fa fa-trophy"/></td>
</tr>
<tr>
    <td><t:topSubmenuLink labelCode="nav.quest.leaderboard" url="${contestURL}/quest/leaderboard" icon="glyphicon glyphicon-list"/></td>
</tr>