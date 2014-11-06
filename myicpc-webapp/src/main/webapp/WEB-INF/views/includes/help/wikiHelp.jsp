<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<button type="button" onclick="previewWikiSyntax('<spring:url
        value="/private/preview-wiki-text"/>', $(this).parent().parent().find('input, textarea').first(), $(this).siblings('.wiki-preview'));"
        class="btn btn-default btn-xs pull-right"
        >
    <spring:message code="preview"/>
</button>
<spring:message code="wiki.markup.help"/>
<br class="clear"/>

<div class="wiki-preview"></div>