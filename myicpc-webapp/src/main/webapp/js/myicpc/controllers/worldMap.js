// Generated by CoffeeScript 1.8.0
var drawMarkersMap;

drawMarkersMap = function() {
  var data, options, scoreboard, tableView, worldMap;
  data = new google.visualization.DataTable();
  data.addColumn('number', 'Latitude');
  data.addColumn('number', 'Longitude');
  data.addColumn('string', 'Team');
  data.addColumn('number', 'Rank');
  data.addRow([50.5, 14.25, 'First marker', 1]);
  data.addRow([50.5, 14.25, 'Second Marker', 2]);
  options = {
    displayMode: 'markers',
    height: 500,
    enableRegionInteractivity: true
  };
  worldMap = new google.visualization.GeoChart(document.getElementById('world-map'));
  worldMap.draw(data, options);
  tableView = new google.visualization.DataView(data);
  tableView.setColumns([3, 2]);
  scoreboard = new google.visualization.Table(document.getElementById('map-scoreboard'));
  return scoreboard.draw(tableView, {});
};

google.load('visualization', '1', {
  'packages': ['geochart', 'table']
});

google.setOnLoadCallback(drawMarkersMap);
