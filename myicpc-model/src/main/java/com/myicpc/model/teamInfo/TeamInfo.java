package com.myicpc.model.teamInfo;

import com.myicpc.model.IdGeneratedContestObject;
import com.myicpc.model.eventFeed.Region;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

/**
 * A team synchronized from CM
 * <p/>
 * Because {@link com.myicpc.model.eventFeed.Team} is created at the begging of the Event feed, we need one
 * more entity to represents team in contest. These two entities are connected
 * through {@link #externalId}.
 * <p/>
 * {@link TeamInfo} is used before the contest, to prepare for contest. It holds
 * all settings and data, which are not received from Event feed.
 *
 * @author Roman Smetana
 * @see com.myicpc.model.eventFeed.Team
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"externalId", "contestId"}))
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "TeamInfo_id_seq")
public class TeamInfo extends IdGeneratedContestObject {
    private static final long serialVersionUID = 7252121195065071100L;

    /**
     * Reservation id from CM
     */
    private Long externalId;
    /**
     * Team id from contest system
     */
    private Long teamContestId;
    /**
     * Team name taken from CM
     * <p/>
     * Can be different from name from Event feed
     */
    private String name;
    /**
     * Shorter team name, some words from the team name are abbreviated and the
     * short name has length 40 max (
     * {@link com.myicpc.commons.utils.FormatUtils#getTeamShortName(String)})
     */
    private String shortName;
    /**
     * Team abbreviation for places, where is not enough space to print the team
     * name.
     */
    private String abbreviation;
    /**
     * Represents hashtag for that given team
     */
    private String hashtag;

    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String questionnaire;

    @ManyToOne
    @JoinColumn(name = "regionId")
    private Region region;

    /**
     * Team university
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "universityId")
    private University university;

    /**
     * Standings from CM from previous content in the same ICPC year
     */
    @OneToMany(mappedBy = "teamInfo", cascade = CascadeType.ALL)
    private List<RegionalResult> regionalResults;

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(final Long externalId) {
        this.externalId = externalId;
    }

    public Long getTeamContestId() {
        return teamContestId;
    }

    public void setTeamContestId(final Long teamContestId) {
        this.teamContestId = teamContestId;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(final String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(final String hashtag) {
        this.hashtag = hashtag;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(final String questionnaire) {
        this.questionnaire = questionnaire;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(final University university) {
        this.university = university;
    }

    public List<RegionalResult> getRegionalResults() {
        return regionalResults;
    }

    public void setRegionalResults(final List<RegionalResult> regionalResults) {
        this.regionalResults = regionalResults;
    }

    /**
     * In settings.properties is field contest.showTeamNames. If it's true,
     * {@link #name} is used, otherwise {@link University#getName()} is used
     * <p/>
     * In WF, teams are called with their university names, so that's why
     *
     * @return team name, which is the same like in the Event feed
     */
    @Transient
    public String getContestTeamName() {
        if (contest != null && contest.getContestSettings() != null) {
            if (contest.getContestSettings().isShowTeamNames()) {
                return name;
            } else {
                return university.getName();
            }
        } else {
            return name;
        }
    }
}
