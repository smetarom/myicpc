package com.myicpc.service.dto.filter;

import com.myicpc.enums.NotificationType;

import java.io.Serializable;

/**
 * Represents filter for notifications
 * 
 * @author Roman Smetana
 */
public class NotificationFilterDTO implements Serializable {
	private static final long serialVersionUID = 8524927210903814627L;

	/**
	 * Notification type
	 */
	private NotificationType notificationType;
	/**
	 * Notification title
	 */
	private String title;
	/**
	 * Notification body
	 */
	private String body;

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(final NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(final String body) {
		this.body = body;
	}
}
