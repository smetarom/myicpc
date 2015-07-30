<script type="text/javascript">
  $(function () {
    $("#main-schedule-link").click(function () {
      $("#main-schedule-submenu").slideToggle();
      $("#main-scoreboard-submenu").hide();
      $("#main-quest-submenu").hide();
      $("#main-gallery-submenu").hide();
      $("#main-misc-submenu").hide();
    });
    $("#main-scoreboard-link").click(function () {
      $("#main-scoreboard-submenu").slideToggle();
      $("#main-schedule-submenu").hide();
      $("#main-quest-submenu").hide();
      $("#main-gallery-submenu").hide();
      $("#main-misc-submenu").hide();
    });
    $("#main-quest-link").click(function () {
      $("#main-quest-submenu").slideToggle();
      $("#main-schedule-submenu").hide();
      $("#main-scoreboard-submenu").hide();
      $("#main-gallery-submenu").hide();
      $("#main-misc-submenu").hide();
    });
    $("#main-misc-link").click(function () {
      $("#main-misc-submenu").slideToggle();
      $("#main-quest-submenu").hide();
      $("#main-schedule-submenu").hide();
      $("#main-scoreboard-submenu").hide();
      $("#main-gallery-submenu").hide();
    });
    $("#main-gallery-link").click(function () {
      $("#main-gallery-submenu").slideToggle();
      $("#main-quest-submenu").hide();
      $("#main-schedule-submenu").hide();
      $("#main-scoreboard-submenu").hide();
      $("#main-misc-submenu").hide();
    });
  });
</script>