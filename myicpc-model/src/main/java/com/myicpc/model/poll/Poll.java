package com.myicpc.model.poll;

import com.myicpc.model.StartEndDateObject;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Represents a question, possible answers and actual users answers
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "Poll_id_seq")
public class Poll extends StartEndDateObject {
    private static final Logger logger = LoggerFactory.getLogger(Poll.class);
    private static final long serialVersionUID = 385801354549528941L;

    public enum PollType {
        /**
         * Select
         */
        SELECT("Selection"),
        /**
         * Radio selection
         */
        CHOICE("Choice");

        private String label;

        private PollType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public boolean isChoiceType() {
            return this == CHOICE;
        }

        public boolean isSelectType() {
            return this == SELECT;
        }
    }

    /**
     * Polling question
     */
    @NotNull
    @NotEmpty
    private String question;

    /**
     * Was the poll closed after it ended
     */
    private boolean opened;

    /**
     * Type of the answer
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private PollType pollType;

    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String description;

    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String conclusionMessage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "correctAnswerId")
    private PollOption correctAnswer;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
    private List<PollOption> options;

    @Transient
    private List<String> choiceStringList;
    @Transient
    private int numAnswers;
    @Transient
    private int numAnsweredOptions;
    @Transient
    private boolean answered;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }

    public PollType getPollType() {
        return pollType;
    }

    public void setPollType(final PollType pollType) {
        this.pollType = pollType;
    }

    public String getConclusionMessage() {
        return conclusionMessage;
    }

    public void setConclusionMessage(String conclusionMessage) {
        this.conclusionMessage = conclusionMessage;
    }

    public PollOption getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(PollOption correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<PollOption> getOptions() {
        return options;
    }

    public void setOptions(List<PollOption> options) {
        this.options = options;
    }

    public List<String> getChoiceStringList() {
        return choiceStringList;
    }

    public void setChoiceStringList(final List<String> choiceStringList) {
        this.choiceStringList = choiceStringList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(final boolean opened) {
        this.opened = opened;
    }

    public int getNumAnswers() {
        return numAnswers;
    }

    public void setNumAnswers(final int numAnswers) {
        this.numAnswers = numAnswers;
    }

    public int getNumAnsweredOptions() {
        return numAnsweredOptions;
    }

    public void setNumAnsweredOptions(int numAnsweredOptions) {
        this.numAnsweredOptions = numAnsweredOptions;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(final boolean answered) {
        this.answered = answered;
    }

    @Transient
    public boolean isClosed() {
        return endDate.before(new Date()) || !StringUtils.isEmpty(conclusionMessage) || correctAnswer != null;
    }

    @Transient
    public boolean isActive() {
        if (startDate != null && endDate != null) {
            Date date = new Date();
            return date.after(startDate) && date.before(endDate);
        }
        return false;
    }

    @Transient
    public String getFormattedResolveMessage() {
        StringBuilder sb = new StringBuilder();
        if (correctAnswer != null) {
            sb.append("<strong>Correct answer: </strong>").append(correctAnswer.getName()).append("<br />");
        }
        if (!StringUtils.isEmpty(conclusionMessage)) {
            sb.append("<p>").append(conclusionMessage).append("</p>");
        }
        return sb.toString();
    }
}
