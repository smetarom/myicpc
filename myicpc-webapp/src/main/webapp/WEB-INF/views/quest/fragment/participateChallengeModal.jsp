<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<t:modalWindow id="participateInChallenge">
    <jsp:attribute name="title">
        <spring:message code="quest.participate" />
    </jsp:attribute>

    <jsp:body>
        <t:alert>
            <spring:message code="quest.challenge.participate.hint1" />
            <strong>#${contest.hashtag}</strong>
            <strong id="challengeModalHashtag"></strong>
            <spring:message code="quest.challenge.participate.hint2" />
        </t:alert>

        <div class="clearfix text-center">
            <div class="col-sm-6 quest-participate">
                <a id="twitterParticipateInChallenge" href="#" class="block thumbnail twitter-color">
                    <t:faIcon icon="twitter" />
                    <span><spring:message code="twitter" /></span>
                </a>
            </div>
            <div class="col-sm-6 quest-participate">
                <a href="https://vine.co/" class="block thumbnail vine-color">
                    <t:faIcon icon="vine" />
                    <span><spring:message code="vine" /></span>
                </a>
            </div>
            <div class="col-sm-6 quest-participate">
                <a href="https://instagram.com/" class="block thumbnail instagram-color">
                    <t:faIcon icon="instagram" />
                    <span><spring:message code="instagram" /></span>
                </a>
            </div>
        </div>
    </jsp:body>

</t:modalWindow>

<script type="text/javascript">
	function showParticipateChallenge(hashtag, title) {
		$("#participateInChallengeLabel").html('<spring:message code="quest.participate" /> ' + title);
		<c:if test="${currentDevice.normal}">
			$("#twitterParticipateInChallenge").prop("href", 'http://twitter.com/intent/tweet?hashtags='+hashtag+',${contest.hashtag}');
		</c:if>
		<c:if test="${not currentDevice.normal}">
		 	$("#twitterParticipateInChallenge").prop("href", 'twitter://post?message=%23'+hashtag+' %23${contest.hashtag}');
		</c:if>
		$("#challengeModalHashtag").html('#' + hashtag);

		$("#participateInChallenge").modal('show');
	}
</script>