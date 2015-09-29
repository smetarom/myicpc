package com.myicpc.model.teamInfo;

import com.myicpc.model.IdGeneratedObject;
import com.myicpc.model.contest.Contest;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import java.util.List;

/**
 * A person, which is somehow involved in contest
 * <p/>
 * There are four kinds of {@link ContestParticipant}, depends on the role in the
 * contest
 *
 * @author Roman Smetana
 * @see TeamInfo
 * @see AttendedContest
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "ContestParticipant_id_seq")
public class ContestParticipant extends IdGeneratedObject {
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

    @OneToMany(mappedBy = "contestParticipant", cascade = CascadeType.ALL)
    private List<ContestParticipantAssociation> teamAssociations;

    /**
     * The history of contests, which the {@link ContestParticipant} attended before
     */
    @OneToMany(mappedBy = "contestParticipant", cascade = CascadeType.ALL)
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

    public List<ContestParticipantAssociation> getTeamAssociations() {
        return teamAssociations;
    }

    public void setTeamAssociations(final List<ContestParticipantAssociation> associations) {
        this.teamAssociations = associations;
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
    public boolean isStaffMember(final Contest contest) {
        return isTeamRole(new ContestParticipantRoleCommand() {
            @Override
            public boolean hasContestParticipantRole(ContestParticipantAssociation association) {
                return association.isStaffMember(contest);
            }
        });
    }

    @Transient
    public boolean isContestant(final Contest contest) {
        return isTeamRole(new ContestParticipantRoleCommand() {
            @Override
            public boolean hasContestParticipantRole(ContestParticipantAssociation association) {
                return association.isContestant(contest);
            }
        });
    }

    @Transient
    public boolean isContestParticipant(final Contest contest) {
        return isTeamRole(new ContestParticipantRoleCommand() {
            @Override
            public boolean hasContestParticipantRole(ContestParticipantAssociation association) {
                return association.isContestParticipant(contest);
            }
        });
    }

    private boolean isTeamRole(ContestParticipantRoleCommand command) {
        if (teamAssociations == null) {
            return false;
        }
        for (ContestParticipantAssociation association : teamAssociations) {
            if (command.hasContestParticipantRole(association)) {
                return true;
            }
        }
        return false;
    }

    private interface ContestParticipantRoleCommand {
        boolean hasContestParticipantRole(ContestParticipantAssociation association);
    }
}
