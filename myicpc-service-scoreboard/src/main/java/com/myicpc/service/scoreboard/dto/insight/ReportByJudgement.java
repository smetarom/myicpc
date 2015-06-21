package com.myicpc.service.scoreboard.dto.insight;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.myicpc.model.eventFeed.Judgement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Represents a report by judgment for every {@link LanguageDTO}
 * 
 * @author Roman Smetana
 */
public class ReportByJudgement extends InsightReport {
	/**
	 * Judgment
	 */
	private final Judgement judgement;
	/**
	 * List of languages with this run judgment
	 */
	private final List<LanguageDTO> languages = new ArrayList<LanguageDTO>();;

	public ReportByJudgement(final Judgement judgement) {
		this.judgement = judgement;
	}

	public List<LanguageDTO> getLanguages() {
		return languages;
	}

	/**
	 * Add language to the list
	 * 
	 * @param result
	 *            language
	 */
	public void addLanguage(final LanguageDTO result) {
		languages.add(result);
	}

	/**
	 * Create a JSON representation of full report
	 * 
	 * JSON structure in {@link #getFullNormalizedReport(java.util.Map)}
	 * 
	 * @return JSON representation of full report
	 */
	public JsonObject getFullReport() {
		return getFullNormalizedReport(null);
	}

	/**
	 * Create a JSON representation of full report, where value are mapped to
	 * percentages
	 * 
	 * Structure:
	 * 
	 * <pre>
	 * [
	 *   {
	 *     "key": "&lt;JUDGMENT_CODE&gt;",
	 *     "description": "&lt;JUDGMENT_NAME&gt;",
	 *     "color": "&lt;HEX_COLOR_CODE&gt;",
	 *     "values": [
	 *       [
	 *         "&lt;LANGUAGE_NAME&gt;",
	 *         &lt;NUMBER_OF_SUBMISSIONS&gt;
	 *       ]
	 *     ]
	 *   }
	 * ]
	 * </pre>
	 * 
	 * @param totalCount
	 *            map between languages and total submissions per language
	 * @return JSON representation of full report
	 */
	public JsonObject getFullNormalizedReport(final Map<String, Integer> totalCount) {
		if (languages.isEmpty()) {
			return null;
		}

		JsonObject root = new JsonObject();
		root.addProperty("key", judgement.getCode());
		root.addProperty("description", judgement.getName());
		root.addProperty("color", getResultColors(judgement.getCode()));
		JsonArray values = new JsonArray();
		boolean include = false;
		sortLanguages();

		for (LanguageDTO l : languages) {
			if (l.getCount() > 0) {
				include = true;
			}

			JsonArray arr = new JsonArray();
			arr.add(new JsonPrimitive(l.getName()));
			if (totalCount == null) {
				arr.add(new JsonPrimitive(l.getCount()));
			} else {
				if (totalCount.get(l.getName()) != 0) {
					arr.add(new JsonPrimitive(100 * l.getCount() / (double) totalCount.get(l.getName())));
				} else {
					arr.add(new JsonPrimitive(0));
				}
			}
			values.add(arr);
		}
		root.add("values", values);

		if (!include) {
			return null;
		}
		return root;
	}

	/**
	 * Sort languages by language name
	 */
	private void sortLanguages() {
		Collections.sort(languages, new Comparator<LanguageDTO>() {
			@Override
			public int compare(LanguageDTO o1, LanguageDTO o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}
}
