package com.myicpc.model.teamInfo;

import com.myicpc.enums.TeamMemberRole;
import com.myicpc.model.IdGeneratedObject;

import javax.persistence.*;

/**
 * Contest where {@link TeamMember} participated or was involved
 *
 * @author Roman Smetana
 * @see TeamMember
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "AttendedContest_id_seq")
public class AttendedContest extends IdGeneratedObject {
    private static final long serialVersionUID = 3683825315347008447L;

    /**
     * Contest id from CM
     */
    private Long externalId;
    /**
     * Contest name
     */
    private String name;
    /**
     * Home page of the contest
     */
    private String homepageURL;
    /**
     * What year, the contest happened
     */
    private Integer year;
    /**
     * What was {@link TeamMemberRole} of {@link #teamMember} in contest
     */
    @Enumerated(EnumType.STRING)
    private TeamMemberRole teamMemberRole;

    /**
     * Who attended this contest
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamMemberId")
    private TeamMember teamMember;

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(final Long externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getHomepageURL() {
        return homepageURL;
    }

    public void setHomepageURL(final String homepageURL) {
        this.homepageURL = homepageURL;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    public TeamMemberRole getTeamMemberRole() {
        return teamMemberRole;
    }

    public void setTeamMemberRole(final TeamMemberRole teamMemberRole) {
        this.teamMemberRole = teamMemberRole;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(final TeamMember teamMember) {
        this.teamMember = teamMember;
    }
}
