//Angular controller for poll page
var pollApp = angular.module("pollApp", ['nvd3ChartDirectives']);
pollApp.controller('PollController', function ($scope) {
    // Polls data
    $scope.data = {};

    // Set data
    $scope.setData = function (data) {
        for (var pollId in data) {
            if ($scope.data[pollId] !== undefined && $scope.data[pollId][0] !== undefined) {
                $scope.data[pollId][0].values = _.sortBy($scope.data[pollId][0].values, function (obj) {
                    return obj.value * -1;
                });
            }
        }

        $scope.data = data;
        $scope.$apply();
    };

    // Returns data for a poll
    $scope.getChartData = function (pollId) {
        if ($scope.data[pollId] !== undefined && $scope.data[pollId][0] !== undefined) {
            $scope.data[pollId][0].values = _.sortBy($scope.data[pollId][0].values, function (obj) {
                return obj.value * -1;
            });
        }
        return $scope.data[pollId];
    };
    //Process a poll update on a new answer
    $scope.updatePoll = function (pollId, pollType, choiceId, choiceName) {
        var tempData = jQuery.extend(true, {}, $scope.data);
        if (pollType === 'SELECT') {
            if (tempData[pollId] !== undefined && tempData[pollId][0] !== undefined) {
                var exist = false;
                for (var i = 0; i < tempData[pollId][0].values.length; i++) {
                    if (tempData[pollId][0].values[i].key == choiceId) {
                        exist = true;
                        tempData[pollId][0].values[i].value = tempData[pollId][0].values[i].value + 1;

                    }
                }
                if (!exist) {
                    tempData[pollId][0].values.push({"key": choiceId, "name": choiceName, "series": 0, "value": 1});
                }
            }
        } else if (pollType === 'CHOICE') {
            if (tempData[pollId] !== undefined) {
                var exist = false;
                for (var i = 0; i < tempData[pollId].length; i++) {
                    if (tempData[pollId][i].key == choiceId) {
                        exist = true;
                        tempData[pollId][i].value = tempData[pollId][i].value + 1;
                        break;
                    }
                }
                if (!exist) {
                    tempData[pollId].push({"key": choiceId, "name": choiceName, "value": 1});
                }
            }
        }
        $scope.data = tempData;
        $scope.$apply();
    };

    var format = d3.format(',f');
    // Format value for chart
    $scope.valueFormatFunction = function () {
        return function (d) {
            return format(d);
        };
    };
    // Set tooltip for chart
    $scope.toolTipContentFunction = function () {
        return function (key, x, y, e, graph) {
            return  '<strong>' + e.point.name + '</strong>';
        };
    };
    // Set tooltip for chart on the phone view
    $scope.toolTipContentFunctionMobile = function () {
        return function (key, x, y, e, graph) {
            return  '<strong>' + x + ' (' + format(y) + ')</strong>';
        };
    };
    // Tooltip for pie chart
    $scope.toolTipContentFunctionPie = function () {
        return function (key, x, y, e, graph) {
            return  '<strong>' + key + ' (' + y.value + ')</strong>';
        };
    };
    // Assign color to data
    $scope.colorFunction = function () {
        return function (d, i) {
            return d.data.color;
        };
    };
    // Select value for X axis
    $scope.selectXFunction = function () {
        return function (d) {
            return d.name;
        };
    };
    // Select value for Y axis
    $scope.selectYFunction = function () {
        return function (d) {
            return d.value;
        };
    };
    // Select value for X axis
    $scope.xFunction = function () {
        return function (d) {
            return d.name;
        };
    };
    // Select value not longer than 25 characters for Y axis
    $scope.xFunctionShort = function () {
        return function (d) {
            return d.name.substring(0, 25);
        };
    };
    // Select value not longer than 30 characters for X axis
    $scope.xFunctionMobile = function () {
        return function (d) {
            return d.name.substring(0, 30);
        };
    };
    // Select value for Y axis
    $scope.yFunction = function () {
        return function (d) {
            return d.value;
        };
    };
    // Show labels only for integers
    $scope.yAxisTickFormatFunction = function () {
        return function (d) {
            return (d % 1 == 0) ? d : '';
        };
    };
});