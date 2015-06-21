package com.myicpc.service.notification;

import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.model.social.Notification;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for bad word filter service
 * 
 * @author Roman Smetana
 */
@Component
public class BadWordsFilter {
	private static final Logger logger = LoggerFactory.getLogger(BadWordsFilter.class);
	private static final Set<String> swearWords;

	/**
	 * Preload swear words and checked notification types into memory
	 */
	static {
		swearWords = new HashSet<>();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				BadWordsFilter.class.getClassLoader().getResourceAsStream("badWordsDictionary.txt"),
				FormatUtils.DEFAULT_ENCODING))) {

			String line;
			while ((line = br.readLine()) != null) {
				swearWords.add(StringEscapeUtils.unescapeJava(line.replaceAll("[^A-Za-z0-9\\\\]", "")));
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Evaluates if the notification body contains swear words or not
	 * 
	 * @param notification
	 *            checked notification
	 * @return if the notification body contains swear words
	 */
	public static boolean isNotificationOffensive(final Notification notification) {
		return !getSwearWords(notification).isEmpty();
	}

	/**
	 * Get list of swear words contained in the notification body
	 * 
	 * @param notification
	 *            checked notification
	 * @return swear words contained in the notification
	 */
	public static Set<String> getSwearWords(final Notification notification) {
		Set<String> set = new HashSet<String>();

		if (StringUtils.isEmpty(notification.getBody())) {
			return set;
		}

		for (String swearWord : swearWords) {
			Pattern pattern = Pattern.compile("\\b" + swearWord + "\\b", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(notification.getBody());
			if (matcher.find()) {
				set.add(swearWord);
			}
		}

		return set;
	}
}
