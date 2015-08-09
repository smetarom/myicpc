<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateEmpty>
    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value='/css/myicpc/homepage.css'/>" type="text/css">
    </jsp:attribute>

    <jsp:body>
        <!-- Place the tag where you want the button to render -->
        <div
                class="g-interactivepost"
                data-clientid="802619831122.apps.googleusercontent.com"
                data-prefilltext="Engage your users today, create a Google+ page for your business."
                data-contenturl="http://myicpc.icpcnews.com/"
                data-contentdeeplinkid="/scoreboard"
                data-cookiepolicy="single_host_origin"
                data-calltoactionlabel="VISIT"
                data-calltoactionurl="http://plus.google.com/pages/create"
                data-calltoactiondeeplinkid="/pages/create"
                >
            Tell your friends
        </div>


        <br/>
        <div class="container">

            <div class="jumbotron">
                <h1>Vítejte v MyICPC</h1>
                <p>Tohle je ta nejlepší diplomka, co kdy viděla světlo světa.</p>
            </div>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Jméno soutěže</th>
                        <th>Odkaz</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="contest" items="${contests}">
                        <tr>
                            <td>${contest.name}</td>
                            <td><a href="<spring:url value="/${contest.code}"/>">Koukni se</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

        </div>
    </jsp:body>

</t:templateEmpty>
