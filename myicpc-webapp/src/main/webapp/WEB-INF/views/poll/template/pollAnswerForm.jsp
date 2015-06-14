<form class="form-inline" ng-if="!poll.answered">
  <select ng-model="poll.selectedOption"
          ng-init="poll.selectedOption = poll.options[0]"
          ng-options="option.name for option in poll.options | orderBy: 'name' track by option.id"
          required="required"
          class="form-control"></select>
  <button ng-click="submitAnswer(poll.id, poll.selectedOption.id)" class="btn btn-primary"><spring:message code="voteI" /></button>
</form>