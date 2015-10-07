package com.myicpc.commons.enums;

/**
 * Interface for {@code enum}, which is displayed in UI
 *
 * It defines the translation key({@link #getCode()}) and the default value ({@link #getLabel()}), if
 * the translation key is not found
 *
 * @author Roman Smetana
 */
public interface GeneralEnum {
    String getLabel();
    String getCode();
}
