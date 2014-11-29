/**
 * Angular controller for scoreboard views
 *
 * @param $scope
 */
function CustomScoreboardController($scope) {
    /**
     * Teams
     */
    $scope.teams = [];
    /**
     * Pinned teams
     */
    $scope.pinnedTeams = [];
    /**
     * Problems
     */
    $scope.problems = [];

    /**
     * Set teams
     */
    $scope.setTeams = function (teams) {
        $scope.teams = teams;
        $scope.$apply();
    };

    /**
     * Set teams and pinned teams
     */
    $scope.setAllTeams = function (teams, pinnedTeams) {
        $scope.teams = teams;
        $scope.pinnedTeams = pinnedTeams;
        $scope.$apply();
    };

    /**
     * Set problems
     */
    $scope.setProblems = function (problems) {
        $scope.problems = problems;
    };

    /**
     * Return minutes from seconds
     */
    $scope.formatTime = function (time) {
        return convertSecondsToMinutes(time);
    };

    /**
     * Is team followed
     */
    $scope.isTeamFollowed = function (team) {
        if (typeof team.followed === "undefined" || team.followed === false) {
            return false;
        }
        return true;
    };

    /**
     * Pin/unpin a team
     */
    $scope.pinTeam = function (team) {
        var followedTeamIds, index, value;
        if (team.followed === true) {
            followedTeamIds = $scope.parseFollowedTeamsCookie();
            index = followedTeamIds.indexOf(team.teamId);
            if (index > -1) {
                followedTeamIds.splice(index, 1);
            }
            $scope.setCookie("followedTeams", followedTeamIds.join(), 7);
            team.followed = false;
            $scope.pinnedTeams = _.filter($scope.pinnedTeams, function (t) {
                return t.teamId !== team.teamId;
            });
        } else {
            value = $scope.getCookie("followedTeams");
            if (typeof (value) !== 'undefined' && value !== null) {
                value += "," + team.teamId;
            } else {
                value = team.teamId;
            }
            $scope.setCookie("followedTeams", value, 7);
            team.followed = true;
            $scope.pinnedTeams.push(team);
        }
    };

    /**
     * Update rank for a team
     */
    $scope.updateRank = function (teamId, rank) {
        obj = _.find($scope.teams, function (obj) {
            return obj.teamId === teamId;
        });
        if (typeof obj === "undefined") {
            return;
        }
        obj.rank = rank;
    };

    /**
     * Update rank for pinned team
     */
    $scope.updatePinRank = function (teamId, rank) {
        obj = _.find($scope.pinnedTeams, function (obj) {
            return obj.teamId === teamId;
        });
        if (typeof obj === "undefined") {
            return;
        }
        obj.rank = rank;
    };

    /**
     * Update number of solved problems and total time for a team
     */
    $scope.updateNumSolvedAndTotalTime = function (teamId, numSolved, totalTime) {
        $scope._updateNumSolvedAndTotalTime($scope.teams, teamId, numSolved,
            totalTime);
    };

    /**
     * Update number of solved problems and total time for a pinned team
     */
    $scope.updatePinNumSolvedAndTotalTime = function (teamId, numSolved, totalTime) {
        $scope._updateNumSolvedAndTotalTime($scope.pinnedTeams, teamId,
            numSolved, totalTime);
    };
    /**
     * Update number of solved problems and total time
     */
    $scope._updateNumSolvedAndTotalTime = function (teams, teamId, numSolved, totalTime) {
        obj = _.find(teams, function (obj) {
            return obj.teamId === teamId;
        });
        if (typeof obj === "undefined") {
            return;
        }
        obj.nSolved = numSolved;
        obj.totalTime = totalTime;
    };

    /**
     * Update team problem for a team
     */
    $scope.updateTeamProblem = function (teamId, problemId, judged, solved, attempts, time, first) {
        $scope._updateTeamProblem($scope.teams, teamId, problemId, judged,
            solved, attempts, time, first);
    };

    /**
     * Update team problem for a pinned team
     */
    $scope.updatePinTeamProblem = function (teamId, problemId, judged, solved, attempts, time, first) {
        $scope._updateTeamProblem($scope.pinnedTeams, teamId, problemId,
            judged, solved, attempts, time, first);
    };
    /**
     * Update team problem
     */
    $scope._updateTeamProblem = function (teams, teamId, problemId, judged, solved, attempts, time, first) {
        team = _.find(teams, function (obj) {
            return obj.teamId === teamId;
        });
        if (typeof team === "undefined"
            || typeof team.teamProblems === "undefined") {
            return;
        }
        if (typeof team.teamProblems[problemId] === "undefined") {
            team.teamProblems[problemId] = {};
        }
        team.teamProblems[problemId].judged = judged;
        team.teamProblems[problemId].solved = solved;
        team.teamProblems[problemId].attempts = attempts;
        team.teamProblems[problemId].time = time;
        team.teamProblems[problemId].first = first;
    };

    /**
     * Has team solved the problem as the first?
     */
    $scope.isFirstSolvedSubmission = function (team, problemId) {
        if (typeof team.teamProblems[problemId] === "undefined") {
            return false;
        }
        return team.teamProblems[problemId].judged === true && team.teamProblems[problemId].solved === true && team.teamProblems[problemId].first === true;
    };
    /**
     * Has team solved the problem?
     */
    $scope.isSolvedSubmission = function (team, problemId) {
        if (typeof team.teamProblems[problemId] === "undefined") {
            return false;
        }
        return team.teamProblems[problemId].judged === true && team.teamProblems[problemId].solved === true && !team.teamProblems[problemId].first;
    };
    /**
     * Has team failed the problem?
     */
    $scope.isFailedSubmission = function (team, problemId) {
        if (typeof team.teamProblems[problemId] === "undefined") {
            return false;
        }
        return team.teamProblems[problemId].judged === true && team.teamProblems[problemId].solved === false;
    };
    /**
     * Has team submitted the problem?
     */
    $scope.isPendingSubmission = function (team, problemId) {
        if (typeof team.teamProblems[problemId] === "undefined") {
            return false;
        }
        return team.teamProblems[problemId].judged === false && !team.teamProblems[problemId].solved;
    };

    /**
     * Set cookie value
     */
    $scope.setCookie = function (c_name, value, exdays) {
        setCookie(c_name, value, exdays, "");
    };

    /**
     * Returns cookie value
     */
    $scope.getCookie = function (c_name) {
        var c_value = document.cookie,
            c_start = c_value.indexOf(" " + c_name + "="),
            c_end;
        if (c_start == -1) {
            c_start = c_value.indexOf(c_name + "=");
        }
        if (c_start == -1) {
            c_value = null;
        } else {
            c_start = c_value.indexOf("=", c_start) + 1;
            c_end = c_value.indexOf(";", c_start);
            if (c_end == -1) {
                c_end = c_value.length;
            }
            c_value = unescape(c_value.substring(c_start, c_end));
        }
        if (c_value) {
            c_value = c_value.replace(/"/g, '');
        }
        return c_value;
    };

    /**
     * Get followed teams IDs
     */
    $scope.parseFollowedTeamsCookie = function () {
        var str = $scope.getCookie("followedTeams"), followedTeamIds, i = 0;
        if (typeof str === "undefined" || str === null) {
            return [];
        }
        followedTeamIds = str.split(",");
        for (i = 0; i < followedTeamIds.length; i++) {
            followedTeamIds[i] = parseInt(followedTeamIds[i]);
        }
        return followedTeamIds;
    };
};