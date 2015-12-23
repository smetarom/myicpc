package com.myicpc.dto.insight;

import com.myicpc.model.eventFeed.Judgement;

import java.io.Serializable;

/**
 * Represents a judgment from Event feed
 * 
 * @author Roman Smetana
 */
public class JudgmentDTO implements Serializable {
	private static final long serialVersionUID = 7674488169454101815L;
	/**
	 * Judgment code
	 */
	private String code;
	/**
	 * Judgment name
	 */
	private String name;
	/**
	 * Count of team submissions with this judgment
	 */
	private int count;

    /**
     * Constructor
     *
     * @param code judgment code
     * @param name judgment name
     * @param count count of team submissions with this judgment
     */
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
