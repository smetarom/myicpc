<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:labelTableRow label="module.${param.module}" styleClassBody="text-right">
  <form:checkbox path="moduleConfiguration.${param.module}Module" class="moduleSwitch" data-size="small" />
  &nbsp;&nbsp;&nbsp; <a href="javascript:showModuleDescription('${param.module}Module')"><t:glyphIcon icon="eye-open" /></a>
</t:labelTableRow>