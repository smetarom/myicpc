package com.myicpc.dto.insight;

/**
 * Represents a language from Event feed
 * 
 * @author Roman Smetana
 */
public class LanguageDTO {
	/**
	 * Language name
	 */
	private String name;
	/**
	 * Count of team submissions with this language
	 */
	private int count;

    /**
     * Constructor
     *
     * @param name language name
     * @param count count of team submissions with this language
     */
	public LanguageDTO(final String name, final int count) {
		super();
		this.name = name;
		this.count = count;
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
