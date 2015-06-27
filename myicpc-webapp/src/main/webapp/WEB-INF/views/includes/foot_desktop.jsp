<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
  $(function () {
    $(".dropdown").hover(
            function () {
              $('.dropdown-menu', this).stop(true, true).slideDown("fast");
              $(this).toggleClass('open');
            },
            function () {
              $('.dropdown-menu', this).stop(true, true).slideUp("fast");
              $(this).toggleClass('open');
            }
    );

    var timediff = ${empty contestTime ? 0 : contestTime};

    function contestTime() {
      $(".time_holder").html(formatContestTime(timediff));
      timediff += 60;
    }

    contestTime();
    if (timediff < 18000) {
      setInterval(contestTime, 60 * 1000);
    }

    $(".notification-counter").click(function() {
        var $featuredNotificationContainer = $("#featured-notification-container");
        if (!$featuredNotificationContainer.is(":visible")) {
            $.get("<spring:url value="${contestURL}/notification/featured-panel" />", function(data) {
                $featuredNotificationContainer.html(data);
                $featuredNotificationContainer.slideDown();
                $(window).scrollTop(0);
            });
        } else {
            $featuredNotificationContainer.slideUp();
        }
    });
  });
</script>