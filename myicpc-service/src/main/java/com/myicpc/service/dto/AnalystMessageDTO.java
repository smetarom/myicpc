package com.myicpc.service.dto;

/**
 * @author Roman Smetana
 */
public class AnalystMessageDTO {
    private String title;
    private String message;

    public AnalystMessageDTO(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
