package com.myicpc.enums;

import com.myicpc.commons.enums.GeneralEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Roman Smetana
 */
@SuppressWarnings("ALL")
public enum ContestParticipantRole implements GeneralEnum {
    /**
     * An <em>attendee</em>, or a person who attends a competition with the
     * team but who has none of the other {@link ContestParticipantRole}s. Attendees
     * do not perform in the competition. An attendee may be sometimes
     * called a guest.
     */
    ATTENDEE("role.attendee", "Attendee"),
    /**
     * A <em>contestant</em> on a team. This is one of the persons who
     * actually perform in the competition. A team generally has multiple
     * contestants.
     */
    CONTESTANT("role.contestant", "Contestant"),
    /**
     * A <em>coach</em> of a team. A team may or may not have multiple
     * coaches, according to the rules of the contest.
     */
    COACH("role.coach", "Coach"),
    /**
     * A <em>contestant</em> on a team.This person can substitute a contestant
     */
    RESERVE("role.reserve", "Reserve"),
    /**
     * Describes that a particular person is a staff member, for a
     * particular contest.
     */
    STAFF("role.staff", "Staff");

    /**
     * Language constant
     */
    private final String code;
    private final String label;

    /**
     * Mapping between role name and {@link ContestParticipantRole}
     */
    private static final Map<String, ContestParticipantRole> mapping;

    static {
        mapping = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        mapping.put("Contestant", ContestParticipantRole.CONTESTANT);
        mapping.put("Coach", ContestParticipantRole.COACH);
        mapping.put("Attendee", ContestParticipantRole.ATTENDEE);
        mapping.put("Reserve", ContestParticipantRole.RESERVE);
        mapping.put("Staff", ContestParticipantRole.STAFF);
    }

    ContestParticipantRole(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getLabel() {
        return label;
    }

    /**
     * @param role code of {@link ContestParticipantRole}
     * @return team member role
     */
    public static ContestParticipantRole getTeamRoleByCode(final String role) {
        return mapping.get(role);
    }

    public static List<ContestParticipantRole> getAllTeamRoles() {
        return Arrays.asList(ContestParticipantRole.values());
    }
}
