package com.myicpc.commons.utils;

import com.myicpc.commons.enums.GeneralEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Translates the translation key into the localized text
 *
 * @author Roman Smetana
 */
public class MessageUtils {
    private static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);
    /**
     * Bundle with translations
     */
    private static ResourceBundle messageBundle;

    static {
        try {
            messageBundle = ResourceBundle.getBundle("i18n.text");
        } catch (NullPointerException | MissingResourceException ex) {
            logger.error("File with translations not available.", ex);
        }
    }

    /**
     * Translates {@code enum} which implements {@link GeneralEnum}
     *
     * @param generalEnum enum to be translated
     * @return translated enum label
     */
    public static String translateEnum(GeneralEnum generalEnum) {
        String value;
        try {
            value = getMessage(generalEnum.getCode());
        } catch (Exception ex) {
            value = generalEnum.getLabel();
        }
        return value;
    }

    /**
     * Translates the translation key into the localized text
     *
     * @param key translation key
     * @return localized text
     * @throws MissingResourceException translation key not found
     */
    public static String getMessage(final String key) {
        return messageBundle.getString(key);
    }

    /**
     * Translates the translation key into the localized text
     *
     * It uses {@code defaultText}, if the translation key is not found
     *
     * @param key translation key
     * @param defaultText default text, if the translation key is not found
     * @return localized text
     */
    public static String getMessageWithDefault(final String key, final String defaultText) {
        try {
            return messageBundle.getString(key);
        } catch (MissingResourceException e) {
            return defaultText;
        }
    }

    /**
     * Translates the translation key into the localized text
     *
     * @param key translation key
     * @param locale locale of the user
     * @return localized text
     * @throws MissingResourceException translation key not found
     */
    public static String getMessage(final String key, final Locale locale) {
        return getMessage(key);
    }

    /**
     * Translates the translation key into the localized text
     *
     * @param key translation key
     * @param params parameters for translation key
     * @return localized text
     * @throws MissingResourceException translation key not found
     */
    public static String getMessage(final String key, final Object... params) {
        return MessageFormat.format(messageBundle.getString(key), params);
    }

    /**
     * Translates the translation key into the localized text
     *
     * @param key translation key
     * @param locale locale of the user
     * @param params parameters for translation key
     * @return localized text
     * @throws MissingResourceException translation key not found
     */
    public static String getMessage(final String key, Locale locale, final Object... params) {
        return getMessage(key, params);
    }

}
