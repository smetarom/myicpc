package com.myicpc.enums;

import com.myicpc.commons.enums.GeneralEnum;

/**
 * @author Roman Smetana
 */
public enum FeedRunStrategyType implements GeneralEnum {
    POLLING("feed.strategy.type.polling", "Long-polling"),
    NATIVE("feed.strategy.type.native", "Computed by MyICPC"),
    UNSORTED_JSON("feed.strategy.type.unsorted", "Unsorted JSON scoreboard"),
    SORTED_JSON("feed.strategy.type.sorted", "Sorted JSON scoreboard");

    private String label;
    private String code;

    FeedRunStrategyType(String code, String label) {
        this.label = label;
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getName() {
        return this.toString();
    }
}
