<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Offline - MyICPC</title>
    <%@ include file="/WEB-INF/views/includes/headOffline.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<div class="container">
    <div class="jumbotron ${sitePreference.mobile ? 'mobile' : 'desktop' }">
        <h2 style="font-size: 1.9em;">
            <spring:message code="offline.error.title"/>
        </h2>
        <ul>
            <li><spring:message code="offline.error.reason1"/></li>
            <li><spring:message code="offline.error.reason2"/></li>
        </ul>
        <br/>

        <p>
            <a href="javascript: location.reload();" class="btn btn-primary btn-lg"><spring:message
                    code="offline.warning.link"/></a>
        </p>
    </div>

    <h2>
        <spring:message code="offline.meantime"/>
    </h2>

    <div class="content">
        <jsp:include page="/WEB-INF/views/fragment/offline/offlineButton.jsp">
            <jsp:param name="url" value="/scoreboard"/>
            <jsp:param name="icon" value="glyphicon glyphicon-th-list"/>
            <jsp:param name="label" value="nav.scoreboard"/>
        </jsp:include>

        <jsp:include page="/WEB-INF/views/fragment/offline/offlineButton.jsp">
            <jsp:param name="url" value="/schedule"/>
            <jsp:param name="icon" value="glyphicon glyphicon-calendar"/>
            <jsp:param name="label" value="nav.schedule"/>
        </jsp:include>

        <jsp:include page="/WEB-INF/views/fragment/offline/offlineButton.jsp">
            <jsp:param name="url" value="/quest"/>
            <jsp:param name="icon" value="glyphicon glyphicon-screenshot"/>
            <jsp:param name="label" value="nav.quest"/>
        </jsp:include>
    </div>

    <div class="modal fade" id="teamModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">
                        <spring:message code="offline.team.available"/>
                    </h4>
                </div>
                <div class="modal-body">...</div>
            </div>
        </div>
    </div>

    <script id="available-teams-template" type="text/x-jquery-tmpl">
	<table class="table table-striped">
		<tbody>
			{{#each this}}
				<tr>
					<td><a href="<spring:url value="/team"/>/{{id}}">{{name}}</a></td>
				</tr>
			{{/each}}
		</tbody>
	</table>

    </script>

    <script type="text/javascript">
        function showAvailableTeams() {
            var availableTeamsTemplate = compileHandlebarsTemplate("available-teams-template");
            if (Modernizr.localstorage) {
                var arr = new Array();
                for (var i = 0; i < localStorage.length; i++) {
                    var key = localStorage.key(i);
                    var intRegex = /^\d+$/;
                    if (intRegex.test(key.substring(4))) {
                        arr.push(JSON.parse(localStorage[key]));
                    }
                }
                $("#teamModal .modal-body").html(availableTeamsTemplate(arr));
            }
            $("#teamModal").modal('show');
        }

        $(function () {
        });
    </script>
</div>
</body>
</html>