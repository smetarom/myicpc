package com.myicpc.model.eventFeed;

import com.myicpc.model.IdGeneratedContestObject;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashMap;
import java.util.Map;

/**
 * Judgment color, which determine the graphical judgement in UI
 *
 * @author Roman Smetana
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code", "contestId"})})
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "JudgementColor_id_seq")
public class JudgementColor extends IdGeneratedContestObject {
    private static final long serialVersionUID = -7015200513682334152L;
    public static final String DEFAULT_COLOR = "#000000"; // black
    private static Map<String, String> defaultColors = new HashMap<>();

    static {
        defaultColors.put("AC", "#006600");
        defaultColors.put("WA", "#000000");
        defaultColors.put("RTE", "#FF9900");
        defaultColors.put("TLE", "#FF0033");
        defaultColors.put("CE", "#FFFF00");
        defaultColors.put("MLE", "#0000FF");
        defaultColors.put("OLE", "#00FFFF");
        defaultColors.put("IF", "#330066");
        defaultColors.put("PE", "#996633");
    }

    /**
     * Judgement name
     */
    private String code;
    /**
     * Color, which represents judgment in the graphical representation
     */
    private String color;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    /**
     * Returns default color for {@code judgementCode}
     *
     * It uses colors from {@link #defaultColors}. If not found, it uses {@link #DEFAULT_COLOR}
     *
     * @param judgementCode judgement code
     * @return default color for {@code judgementCode}
     */
    public static String getDefaultColor(String judgementCode) {
        String color = defaultColors.get(judgementCode);
        if (color == null) {
            return DEFAULT_COLOR;
        }
        return color;
    }
}
