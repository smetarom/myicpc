<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div ng-hide="questChallenges == null" ng-repeat="challenge in questChallenges" class="col-sm-12 clearfix">
  <h3>{{challenge.name}}</h3>

  <p ng-hide="!challenge.endDate">
    <strong><spring:message code="quest.deadline"/>: {{challenge.endDate}}</strong>
  </p>

  <p ng-hide="!challenge.hashtags">
    <strong><spring:message code="quest.tweetWithHashtags"/>: {{challenge.hashtags}}</strong>
  </p>

  <p>{{challenge.description}}</p>

</div>

<div ng-show="questChallenges == null" class="container">
    <br/>
    <div class="well">
        <h2 style="font-size: 1.9em;">
            <spring:message code="offline.quest.notLoaded"/>
        </h2>

        <p>
            <spring:message code="offline.quest.notLoaded.hint"/>
        </p>

        <p>
            <a href="<c:url value="/" />{{currentContest}}" class="btn btn-primary" role="button">
                <spring:message code="offline.goHome"/>
            </a>
        </p>
    </div>
</div>