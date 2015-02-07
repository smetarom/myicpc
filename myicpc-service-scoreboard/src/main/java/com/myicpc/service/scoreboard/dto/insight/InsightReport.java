package com.myicpc.service.scoreboard.dto.insight;

import com.google.gson.JsonObject;
import com.myicpc.model.eventFeed.TeamProblem;

import java.util.HashMap;
import java.util.Map;

/**
 * General DTO for any insight report
 * 
 * @author Roman Smetana
 */
public abstract class InsightReport {
	private Map<String, String> colorMap;

	public InsightReport() {
		colorMap = new HashMap<>();
		colorMap.put("AC", "#006600");
		colorMap.put("WA", "#000000");
		colorMap.put("RTE", "#FF9900");
		colorMap.put("TLE", "#FF0033");
	}

	/**
	 * Get color for run judgment
	 * 
	 * @param resultCode
	 *            run judgment
	 * @return hex color code
	 */
	public String getResultColors(final String resultCode) {
		String color = colorMap.get(resultCode);
		if (color == null) {
			return "#0000FF";
		}
		return color;
	}

	/**
	 * Returns JSON representation of {@link TeamProblem} for Insight report
	 * 
	 * @param teamProblem
	 *            team submission
	 * @return JSON representation
	 */
	protected JsonObject getJSONTeamProblem(final TeamProblem teamProblem) {
		JsonObject teamProblemJSON = new JsonObject();
		teamProblemJSON.addProperty("teamId", teamProblem.getTeam().getExternalId());
		teamProblemJSON.addProperty("teamName", teamProblem.getTeam().getName());
		teamProblemJSON.addProperty("time", teamProblem.getFormattedTime());
		return teamProblemJSON;
	}
}
