<%@ tag language="java" pageEncoding="UTF-8" %>

<%@ attribute name="icon" required="true" %>
<%@ attribute name="style" %>

<span class="fa fa-${icon}" ${not empty style ? 'style="'.concat(style).concat('"') : ''}></span>
