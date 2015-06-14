<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div ng-repeat="poll in polls">
    <h3>{{poll.question}}</h3>


    <%@ include file="/WEB-INF/views/poll/template/pollAnswerForm.jsp" %>
    <%@ include file="/WEB-INF/views/poll/template/charts.jsp" %>



</div>