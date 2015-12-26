package com.myicpc.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"cause", "contestId"})})
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "ErrorMessage_id_seq")
public class ErrorMessage extends IdGeneratedContestObject {
    private static final long serialVersionUID = 1806369015758931836L;

    public enum ErrorMessageCause {
        EVENT_FEED_CONNECTION_FAILED("Event feed connection failed"),
        EVENT_FEED_JSON_CONNECTION_FAILED("JSON event feed connection failed");

        private String name;

        private ErrorMessageCause(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private String message;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ErrorMessageCause cause;

    private boolean solved;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd H:mm:ss")
    private Date timestamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorMessageCause getCause() {
        return cause;
    }

    public void setCause(ErrorMessageCause cause) {
        this.cause = cause;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
