<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.12/angular.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.12/angular-sanitize.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.12/angular-route.min.js" defer></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/modernizr/2.7.1/modernizr.min.js" defer></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js" defer></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js" defer></script>
<script src="<c:url value="/js/jquery/jquery.atmosphere.min.js" />" defer></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.5.2/underscore-min.js" defer></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/handlebars.js/1.3.0/handlebars.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery.touchswipe/1.6.4/jquery.touchSwipe.min.js" defer></script>

<%-- MyICPC internal resources --%>
<script src="<c:url value='/js/myicpc/functions.js'/>"></script>

<%-- Feedback modal window --%>
<script type="application/javascript">
    $(function() {
        $("#feedbackLink").click(function() {
            $.get("<spring:url value="${contestURL}/feedback-form" />", function(data) {
                $("#feedbackWrapper").html(data);
                $('#feedbackModal').modal('show');
            });
        });
    });
</script>