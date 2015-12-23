package com.myicpc.service.scoreboard.dto.insight;

import com.google.gson.JsonObject;
import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.dto.insight.InsightSubmissionDTO;
import com.myicpc.model.eventFeed.JudgementColor;
import com.myicpc.model.eventFeed.TeamProblem;

import java.util.HashMap;
import java.util.Map;

/**
 * General DTO for any insight report
 * 
 * @author Roman Smetana
 */
public abstract class InsightReport {
	private final Map<String, String> colorMap;

	public InsightReport() {
		colorMap = new HashMap<>();
	}

	public InsightReport(Map<String, String> colorMap) {
		this.colorMap = colorMap;
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
			return JudgementColor.getDefaultColor(resultCode);
		}
		return color;
	}

	/**
	 * Returns JSON representation of {@link TeamProblem} for Insight report
	 * 
	 * @param insightSubmissionDTO
	 *            team submission
	 * @return JSON representation
	 */
	protected JsonObject getJSONTeamProblem(final InsightSubmissionDTO insightSubmissionDTO) {
		JsonObject teamProblemJSON = new JsonObject();
		teamProblemJSON.addProperty("teamId", insightSubmissionDTO.getTeamId());
		teamProblemJSON.addProperty("teamName", insightSubmissionDTO.getTeamName());
		teamProblemJSON.addProperty("time", FormatUtils.formatTimeToHoursMinutes(insightSubmissionDTO.getTime()));
		return teamProblemJSON;
	}
}
