package com.myicpc.model.teamInfo;

import com.myicpc.model.IdGeneratedObject;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.List;

/**
 * A person, which is somehow involved in contest
 * <p/>
 * There are four kinds of {@link TeamMember}, depends on the role in the
 * contest
 *
 * @author Roman Smetana
 * @see TeamInfo
 * @see AttendedContest
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "TeamInfo_id_seq")
public class TeamMember extends IdGeneratedObject {
    private static final long serialVersionUID = -5521619641462388146L;

    /**
     * Person id from CM
     */
    private Long externalId;
    /**
     * The person's first name, that is, his given name.
     */
    private String firstname;
    /**
     * The person's last name, that is, his surname or family name.
     */
    private String lastname;

    @Column(unique = true)
    private String twitterUsername;
    @Column(unique = true)
    private String vineUsername;
    @Column(unique = true)
    private String instagramUsername;

    private String linkedinOauthToken;
    private String linkedinOauthSecret;

    private String profilePictureUrl;

    @OneToMany(mappedBy = "teamMember", cascade = CascadeType.ALL)
    private List<TeamMemberAssociation> teamAssociations;

    /**
     * The history of contests, which the {@link TeamMember} attended before
     */
    @OneToMany(mappedBy = "teamMember", cascade = CascadeType.ALL)
    private List<AttendedContest> attendedContests;

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(final Long externalId) {
        this.externalId = externalId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(final String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getTwitterUsername() {
        return twitterUsername;
    }

    public void setTwitterUsername(String twitterUsername) {
        if (!StringUtils.isEmpty(twitterUsername)) {
            if (twitterUsername.charAt(0) == '@' || twitterUsername.charAt(0) == '#') {
                twitterUsername = twitterUsername.substring(1);
            }
        }
        this.twitterUsername = twitterUsername;
    }

    public String getVineUsername() {
        return vineUsername;
    }

    public void setVineUsername(final String vineUsername) {
        this.vineUsername = vineUsername;
    }

    public String getInstagramUsername() {
        return instagramUsername;
    }

    public void setInstagramUsername(final String instagramUsername) {
        this.instagramUsername = instagramUsername;
    }

    public String getLinkedinOauthToken() {
        return linkedinOauthToken;
    }

    public void setLinkedinOauthToken(final String linkedinOauthToken) {
        this.linkedinOauthToken = linkedinOauthToken;
    }

    public String getLinkedinOauthSecret() {
        return linkedinOauthSecret;
    }

    public void setLinkedinOauthSecret(final String linkedinOauthSecret) {
        this.linkedinOauthSecret = linkedinOauthSecret;
    }

    public List<TeamMemberAssociation> getAssociations() {
        return teamAssociations;
    }

    public void setTeamInfos(final List<TeamMemberAssociation> asssociations) {
        this.teamAssociations = asssociations;
    }

    public List<AttendedContest> getAttendedContests() {
        return attendedContests;
    }

    public void setAttendedContests(final List<AttendedContest> attendedContests) {
        this.attendedContests = attendedContests;
    }

    /**
     * @return concatenation of {@link #firstname} and {@link #lastname}
     */
    @Transient
    public String getFullname() {
        return firstname + " " + lastname;
    }

    /**
     * @return concatenation of {@link #lastname} and {@link #firstname}
     */
    @Transient
    public String getOfficialFullname() {
        return lastname + " " + firstname;
    }

    @Transient
    public boolean isStaffMember() {
        return isTeamRole(new TeamMemberRoleCommand() {
            @Override
            public boolean hasTeamMemberRole(TeamMemberAssociation association) {
                return association.isStaffMember();
            }
        });
    }

    @Transient
    public boolean isContestant() {
        return isTeamRole(new TeamMemberRoleCommand() {
            @Override
            public boolean hasTeamMemberRole(TeamMemberAssociation association) {
                return association.isContestant();
            }
        });
    }

    @Transient
    public boolean isContestParticipant() {
        return isTeamRole(new TeamMemberRoleCommand() {
            @Override
            public boolean hasTeamMemberRole(TeamMemberAssociation association) {
                return association.isContestParticipant();
            }
        });
    }

    private boolean isTeamRole(TeamMemberRoleCommand command) {
        if (teamAssociations == null) {
            return false;
        }
        for (TeamMemberAssociation association : teamAssociations) {
            if (command.hasTeamMemberRole(association)) {
                return true;
            }
        }
        return false;
    }

    private static interface TeamMemberRoleCommand {
        boolean hasTeamMemberRole(TeamMemberAssociation association);
    }
}
