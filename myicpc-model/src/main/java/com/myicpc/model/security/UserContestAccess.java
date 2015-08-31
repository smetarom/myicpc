package com.myicpc.model.security;

import com.myicpc.model.IdGeneratedObject;
import com.myicpc.model.contest.Contest;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * Represents a system user for security framework
 *
 * @author Roman Smetana
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"systemUserId", "contestId"}))
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "UserContestAccess_id_seq")
public class UserContestAccess extends IdGeneratedObject {
    private static final long serialVersionUID = -6332120681866780388L;

    @NotNull
    @ManyToOne
    @JoinColumn(name="systemUserId", nullable = false)
    private SystemUser systemUser;

    @NotNull
    @ManyToOne
    @JoinColumn(name="contestId", nullable = false)
    private Contest contest;

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }
}
