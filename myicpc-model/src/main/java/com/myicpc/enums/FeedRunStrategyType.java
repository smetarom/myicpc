package com.myicpc.enums;

/**
 * @author Roman Smetana
 */
public enum FeedRunStrategyType {
    NATIVE("MyICPC computed"), UNSORTED_JSON("Unsorted JSON scoreboard"), SORTED_JSON("Sorted JSON scoreboard");

    private String label;

    private FeedRunStrategyType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return this.toString();
    }
}
