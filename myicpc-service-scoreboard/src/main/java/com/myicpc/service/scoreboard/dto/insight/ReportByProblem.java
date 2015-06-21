package com.myicpc.service.scoreboard.dto.insight;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.dto.insight.InsightSubmissionDTO;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.TeamProblem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a detailed report for a {@link Problem}
 * 
 * @author Roman Smetana
 */
public class ReportByProblem extends InsightReport {
	/**
	 * Problem
	 */
	private Problem problem;
	/**
	 * List of judgments with this problem
	 */
	private final List<JudgmentDTO> results = new ArrayList<>();
	/**
	 * First submission, which tried to solve a problem
	 */
	private InsightSubmissionDTO firstSubmission;
	/**
	 * First submission, which solved a problem
	 */
	private InsightSubmissionDTO firstSolution;
	/**
	 * Average solution time
	 */
	private double averageSolutionTime;
	/**
	 * Number of successful teams
	 */
	private int numSolvedTeams;
	/**
	 * Number of teams, who tried
	 */
	private int numSubmittedTeams;
	/**
	 * Total number of submissions
	 */
	private int totalSubmissions;
	private Map<String, Integer> solvedLanguageMap = new HashMap<>();
	private Map<String, Integer> languageMap = new HashMap<>();

	public ReportByProblem(final Problem problem) {
		this.problem = problem;
	}

	public Problem getProblem() {
		return problem;
	}

	/**
	 * Add judgment to the report
	 * 
	 * @param result
	 *            judgment
	 */
	public void addResult(final JudgmentDTO result) {
		results.add(result);
	}

	public void setFirstSubmission(final InsightSubmissionDTO firstSubmission) {
		this.firstSubmission = firstSubmission;
	}

	public void setFirstSolution(final InsightSubmissionDTO firstSolution) {
		this.firstSolution = firstSolution;
	}

	public int getNumSolvedTeams() {
		return numSolvedTeams;
	}

	public void setNumSolvedTeams(final int numSolvedTeams) {
		this.numSolvedTeams = numSolvedTeams;
	}

	public int getNumSubmittedTeams() {
		return numSubmittedTeams;
	}

	public int getTotalSubmissions() {
		return totalSubmissions;
	}

	public void setTotalSubmissions(final int totalSubmissions) {
		this.totalSubmissions = totalSubmissions;
	}

	public double getAverageSolutionTime() {
		return averageSolutionTime;
	}

	public void setAverageSolutionTime(final double averageSolutionTime) {
		this.averageSolutionTime = averageSolutionTime;
	}

	public void setNumSubmittedTeams(final int numSubmittedTeams) {
		this.numSubmittedTeams = numSubmittedTeams;
	}

	public Map<String, Integer> getSolvedLanguageMap() {
		return solvedLanguageMap;
	}

	public void setSolvedLanguageMap(final Map<String, Integer> solvedLanguageMap) {
		this.solvedLanguageMap = solvedLanguageMap;
	}

	public Map<String, Integer> getLanguageMap() {
		return languageMap;
	}

	public void setLanguageMap(final Map<String, Integer> languageMap) {
		this.languageMap = languageMap;
	}

	public void setProblem(final Problem problem) {
		this.problem = problem;
	}

	/**
	 * Create a JSON representation of overview report
	 * 
	 * @return JSON representation of overview report
	 */
	public JsonObject getSimpleReport() {
		JsonObject problemReportJSON = new JsonObject();
		problemReportJSON.addProperty("code", problem.getCode());
		JsonArray arr = new JsonArray();
		for (JudgmentDTO judgment : results) {
			JsonObject o = new JsonObject();
			o.addProperty("key", judgment.getCode());
			o.addProperty("value", judgment.getCount());
			o.addProperty("color", getResultColors(judgment.getCode()));
			arr.add(o);
		}
		problemReportJSON.add("data", arr);
		return problemReportJSON;
	}

	/**
	 * Create a JSON representation of full report
	 * 
	 * JSON structure:
	 * 
	 * <pre>
	 * {
	 *   "code": "&lt;PROBLEM_LETTER_CODE&gt;",
	 *   "data": [
	 *     {
	 *       "key": "&lt;JUDGMENT_CODE&gt;",
	 *       "value": &lt;NUMBER_OF_SUBMISSIONS&gt;,
	 *       "color": "&lt;HEX_COLOR_CODE&gt;"
	 *     },
	 *     ...
	 *   ],
	 *   "numSolvedTeams": &lt;NUMBER_OF_TEAM_WHO_SOLVED&gt;,
	 *   "numSubmittedTeams": &lt;NUMBER_OF_TEAM_WHO_SUBMITTED_BUT_NOT_SOLVE&gt;,
	 *   "totalSubmissions": &lt;TOTAL_NUMBER_OF_SUBMISSIONS&gt;,
	 *   "averageSolutionTime": "&lt;AVERAGE_TIME_TO_SOLVE_PROBLEM&gt;",
	 *   "languages": [
	 *     {
	 *       "name": "&lt;LANGUAGE_CODE&gt;",
	 *       "count": &lt;NUMBER_OF_SUBMISSIONS_PER_PROBLEM&gt;
	 *     },
	 *     ...
	 *   ],
	 *   "solvedLanguages": [
	 *   	{
	 *       "name": "&lt;LANGUAGE_CODE&gt;",
	 *       "count": &lt;NUMBER_OF_SUBMISSIONS_PER_PROBLEM&gt;
	 *     },
	 *     ...
	 *   ],
	 *   "firstSubmission": {
	 *     "teamId": &lt;TEAM_ID_SUBMITTED_FIRST&gt;,
	 *     "teamName": "&lt;TEAM_NAME_SUBMITTED_FIRST&gt;",
	 *     "time": "&lt;SUBMISSION_TIME&gt;"
	 *   },
	 *   "firstSolution": {
	 *     "teamId": &lt;TEAM_ID_SOLVED_FIRST&gt;,
	 *     "teamName": "&lt;TEAM_NAME_SOLVED_FIRST&gt;",
	 *     "time": "&lt;SOLUTION_TIME&gt;"
	 *   }
	 * }
	 * </pre>
	 * 
	 * @return JSON representation of full report
	 */
	public JsonObject getFullReport() {
		JsonObject problemReportJSON = getSimpleReport();
		problemReportJSON.addProperty("numSolvedTeams", numSolvedTeams);
		problemReportJSON.addProperty("numSubmittedTeams", numSubmittedTeams);
		problemReportJSON.addProperty("totalSubmissions", totalSubmissions);
		problemReportJSON.addProperty("averageSolutionTime", FormatUtils.formatTimeToMinutes(averageSolutionTime));
		JsonArray langArr = new JsonArray();
		// populate language array with info about used languages
		for (String language : languageMap.keySet()) {
			JsonObject langObject = new JsonObject();
			langObject.addProperty("name", language);
			langObject.addProperty("count", languageMap.get(language));
			langArr.add(langObject);
		}
		problemReportJSON.add("languages", langArr);

		JsonArray solvedLangArr = new JsonArray();
		// populate solved language array with info about languages, which
		// solved the problem
		for (String language : solvedLanguageMap.keySet()) {
			JsonObject langObject = new JsonObject();
			langObject.addProperty("name", language);
			langObject.addProperty("count", solvedLanguageMap.get(language));
			solvedLangArr.add(langObject);
		}
		problemReportJSON.add("solvedLanguages", solvedLangArr);

		// add info about the first submission
		if (firstSubmission != null) {
			problemReportJSON.add("firstSubmission", getJSONTeamProblem(firstSubmission));
		}
		// add info about the first submission which solved a problem
		if (firstSolution != null) {
			problemReportJSON.add("firstSolution", getJSONTeamProblem(firstSolution));
		}

		return problemReportJSON;
	}
}
