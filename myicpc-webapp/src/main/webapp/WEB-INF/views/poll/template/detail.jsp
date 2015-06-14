<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<div ng-init="init()"></div>

<h3>{{poll.question}}</h3>

<%@ include file="/WEB-INF/views/poll/template/pollAnswerForm.jsp" %>
<%@ include file="/WEB-INF/views/poll/template/charts.jsp" %>

<table class="table table-striped" ng-show="selectOptions(poll).length">
    <thead>
        <tr>
            <th><spring:message code="poll.option" /></th>
            <th class="text-center"><spring:message code="poll.options.numberOfVotes" /></th>
        </tr>
    </thead>
    <tbody>
        <tr ng-repeat="option in selectOptions(poll)">
            <td>{{option.name}}</td>
            <td class="text-center">{{option.value}}</td>
        </tr>
    </tbody>
</table>