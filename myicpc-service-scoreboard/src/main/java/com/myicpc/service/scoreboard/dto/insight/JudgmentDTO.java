package com.myicpc.service.scoreboard.dto.insight;

import java.io.Serializable;

/**
 * Represents a judgment from Event feed
 * 
 * @author Roman Smetana
 */
public class JudgmentDTO implements Serializable {
	/**
	 * Judgment code
	 */
	private String code;
	/**
	 * Judgment name
	 */
	private String name;
	/**
	 * Count of runs with this judgment
	 */
	private int count;

	public JudgmentDTO(final String code, final String name, final int count) {
		super();
		this.code = code;
		this.name = name;
		this.count = count;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(final int count) {
		this.count = count;
	}
}
