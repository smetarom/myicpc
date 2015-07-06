$(() ->
  $leaderboard = $("#mainLeaderboard")
  $scrollTable = $leaderboard.find(".scroller")
  $fixedRows = $leaderboard.find(".fixed-rows table")
  $nonFixedColumns = $leaderboard.find(".non-fixed-header table")
  scrollbarWidth = 15

  top = $leaderboard.offset().top
  bottom = $("#footer").offset().top
  header = $(".non-fixed-header").outerHeight(true)
  tableHeight = bottom - top - scrollbarWidth
  if (tableHeight < 350)
    tableHeight = 350

  $leaderboard.height(tableHeight)
  $nonFixedColumns.parent().width($leaderboard.width() - scrollbarWidth)
  $fixedRows.parent().height($leaderboard.height() - scrollbarWidth)

  $scrollTable.scroll(() ->
    $fixedRows.css('top', ($scrollTable.scrollTop()*-1))
    $nonFixedColumns.css('left', ($scrollTable.scrollLeft() * -1))
  )
)