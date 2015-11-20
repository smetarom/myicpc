<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div ng-repeat="challenge in questChallenges" class="col-sm-12 clearfix">
  <h3>{{challenge.name}}</h3>

  <p ng-hide="!challenge.endDate">
    <strong><spring:message code="quest.deadline"/>: {{challenge.endDate}}</strong>
  </p>

  <p ng-hide="!challenge.hashtags">
    <strong><spring:message code="quest.tweetWithHashtags"/>: {{challenge.hashtags}}</strong>
  </p>

  <p>{{challenge.description}}</p>

</div>