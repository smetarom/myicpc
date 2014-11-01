package com.myicpc.model.poll;

import com.myicpc.model.IdGeneratedObject;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "pollId"})})
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "PollOption_id_seq")
public class PollOption extends IdGeneratedObject {
    private static final long serialVersionUID = 3527757661176320411L;

    private String name;
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String resultMessage;
    private Integer votes = 0;

    public PollOption() {
        super();
    }

    public PollOption(String name, Poll poll) {
        super();
        this.name = name;
        this.poll = poll;
    }

    @ManyToOne
    @JoinColumn(name = "pollId")
    private Poll poll;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

}
