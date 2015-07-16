<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>

<tr>
    <td><t:topSubmenuLink labelCode="nav.myschedule" url="${contestURL}/my-schedule"
                          icon="glyphicon glyphicon-user"/></td>
    <td><t:topSubmenuLink labelCode="nav.fullschedule" url="${contestURL}/schedule"
                          icon="glyphicon glyphicon-calendar"/></td>
</tr>
<tr>
    <td><t:topSubmenuLink labelCode="nav.venues" url="${contestURL}/venues" icon="glyphicon glyphicon-map-marker"/></td>
    <td></td>
</tr>