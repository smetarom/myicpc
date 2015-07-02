package com.myicpc.dto;

import java.io.Serializable;

/**
 * Holder for translation where {@code code} is the translation key
 * and {@text} alternative text, if the translation if not present
 *
 * @author Roman Smetana
 */
public class TranslationDto implements Serializable {
    private static final long serialVersionUID = 4054119103768096908L;

    /**
     * translation key
     */
    private String code;
    /**
     * alternative text, if the translation if not present
     */
    private String text;

    public TranslationDto(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
