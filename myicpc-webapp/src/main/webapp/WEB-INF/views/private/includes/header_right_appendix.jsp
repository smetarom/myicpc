<li>
  <a href="<spring:url value="/private/settings" />"><t:glyphIcon icon="cog" /></a>
</li>
<li><a href="<spring:url value="/private/help" />"><t:faIcon icon="question"/></a></li>
<li class="dropdown">
  <a href="#" class="dropdown-toggle" data-toggle="dropdown"><sec:authentication property="principal.username"/> <b class="caret"></b></a>
  <ul class="dropdown-menu">
    <li><a href="<spring:url value="/private/profile" />"><t:glyphIcon icon="user" /> <spring:message code="nav.admin.profile"/></a>
    </li>
    <li class="divider"></li>
    <li><a href="<spring:url value="/private/logout" />"><t:glyphIcon icon="off" /> <spring:message code="logout"/></a></li>
  </ul>
</li>