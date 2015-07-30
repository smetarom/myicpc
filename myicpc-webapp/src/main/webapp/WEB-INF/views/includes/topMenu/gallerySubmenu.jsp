<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://myicpc.baylor.edu/tags" %>
<tr>
    <td><t:topSubmenuLink labelCode="nav.gallery.crowd" url="${contestURL}/gallery" icon="fa fa-camera"/></td>
    <td><t:topSubmenuLink labelCode="nav.gallery.official" url="${contestURL}/gallery/official" icon="fa fa-picture-o"/></td>
</tr>