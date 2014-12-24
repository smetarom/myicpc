package com.myicpc.service.scoreboard.dto.insight;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.model.eventFeed.TeamProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a detailed report for a language
 * 
 * @author Roman Smetana
 */
public class ReportByLanguage extends InsightReport {
	/**
	 * Language
	 */
	private String language;

	/**
	 * List of judgments with this language
	 */
	private List<JudgmentDTO> results = new ArrayList<JudgmentDTO>();

	/**
	 * Number of successful teams
	 */
	private int numProblemSolved;
	/**
	 * Number of teams, who tried
	 */
	private int numProblemSubmitted;
	/**
	 * Total number of submissions
	 */
	private int totalSubmissions;
	/**
	 * Percentage of submissions with this language out of all submissions
	 */
	private double usagePercentage;
	/**
	 * Number of teams, who used this language
	 */
	private int usedByNumTeams;
	/**
	 * First submission with this language, which solved a problem
	 */
	private TeamProblem firstSolution;
	/**
	 * First submission with this language, which tried to solve a problem
	 */
	private TeamProblem firstSubmission;

	public ReportByLanguage(final String language) {
		this.language = language;
	}

	/**
	 * Add judgment to the list
	 * 
	 * @param result
	 *            judgment
	 */
	public void addResult(final JudgmentDTO result) {
		results.add(result);
	}

	public void setNumProblemSolved(final int numProblemSolved) {
		this.numProblemSolved = numProblemSolved;
	}

	public void setNumProblemSubmitted(final int numProblemSubmitted) {
		this.numProblemSubmitted = numProblemSubmitted;
	}

	public void setTotalSubmissions(final int totalSubmissions) {
		this.totalSubmissions = totalSubmissions;
	}

	public void setUsagePercentage(final double usagePercentage) {
		this.usagePercentage = usagePercentage;
	}

	public void setFirstSolution(final TeamProblem firstSolution) {
		this.firstSolution = firstSolution;
	}

	public void setFirstSubmission(final TeamProblem firstSubmission) {
		this.firstSubmission = firstSubmission;
	}

	public void setUsedByNumTeams(final int usedByNumTeams) {
		this.usedByNumTeams = usedByNumTeams;
	}

	/**
	 * Create a JSON representation of overview report
	 * 
	 * JSON structure:
	 * 
	 * <pre>
	 * {
	 *   "name": "&lt;LANGUAGE_NAME&gt;",
	 *   "data": [
	 *     {
	 *       "key": "&lt;JUDGMENT_CODE&gt;",
	 *       "name": "&lt;JUDGMENT_NAME&gt;",
	 *       "value": &lt;NUMBER_OF_SUBMISSIONS&gt;,
	 *       "color": "&lt;HEX_COLOR_CODE&gt;"
	 *     }
	 *   ]
	 * }
	 * </pre>
	 * 
	 * @return JSON representation of overview report
	 */
	public JsonObject getSimpleReport() {
		JsonObject root = new JsonObject();
		root.addProperty("name", language);
		JsonArray arr = new JsonArray();
		for (JudgmentDTO j : results) {
			JsonObject o = new JsonObject();
			o.addProperty("key", j.getCode());
			o.addProperty("name", j.getName());
			o.addProperty("value", j.getCount());
			o.addProperty("color", getResultColors(j.getCode()));
			arr.add(o);
		}
		root.add("data", arr);
		return root;
	}

	/**
	 * Create a JSON representation of full report
	 * 
	 * JSON Structure:
	 * 
	 * <pre>
	 * {
	 *   "name": "&lt;LANGUAGE_NAME&gt;",
	 *   "data": [
	 *     {
	 *       "key": "&lt;JUDGMENT_CODE&gt;",
	 *       "name": "&lt;JUDGMENT_NAME&gt;",
	 *       "value": &lt;NUMBER_OF_SUBMISSIONS&gt;,
	 *       "color": "&lt;HEX_COLOR_CODE&gt;"
	 *     },
	 *     ...
	 *   ],
	 *   "numProblemSolved": &lt;NUMBER_OF_SOLVED_SUBMISSIONS&gt;,
	 *   "numProblemSubmitted": &lt;NUMBER_OF_SUBMITTED_BUT_NOT_SOLVED_SUBMISSIONS&gt;,
	 *   "totalSubmissions": &lt;TOTAL_NUMBER_OF_SUBMISSIONS&gt;,
	 *   "usagePercentage": &lt;PERCENTAGE_OF_SUBMISSION_USING_LANGUAGE&gt;,
	 *   "usedByNumTeams": &lt;NUMBER_OF_TEAMS_USING_LANGUAGE,
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
		JsonObject root = getSimpleReport();
		root.addProperty("numProblemSolved", numProblemSolved);
		root.addProperty("numProblemSubmitted", numProblemSubmitted);
		root.addProperty("totalSubmissions", totalSubmissions);
		root.addProperty("usagePercentage", usagePercentage);
		root.addProperty("usedByNumTeams", usedByNumTeams);
		if (firstSubmission != null) {
			root.add("firstSubmission", getJSONTeamProblem(firstSubmission));
		}

		if (firstSolution != null) {
			root.add("firstSolution", getJSONTeamProblem(firstSolution));
		}

		return root;
	}
}
