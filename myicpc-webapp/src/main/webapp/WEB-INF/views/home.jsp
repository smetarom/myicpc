<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:template>

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

</t:template>
