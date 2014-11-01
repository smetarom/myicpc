package com.myicpc.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Roman Smetana
 */
public enum TeamMemberRole {
    /**
     * An <em>attendee</em>, or a person who attends a competition with the
     * team but who has none of the other {@link TeamMemberRole}s. Attendees
     * do not perform in the competition. An attendee may be sometimes
     * called a guest.
     */
    ATTENDEE("role.attendee"),
    /**
     * A <em>contestant</em> on a team. This is one of the persons who
     * actually perform in the competition. A team generally has multiple
     * contestants.
     */
    CONTESTANT("role.contestant"),
    /**
     * A <em>coach</em> of a team. A team may or may not have multiple
     * coaches, according to the rules of the contest.
     */
    COACH("role.coach"),
    /**
     * A <em>contestant</em> on a team.This person can substitute a contestant
     */
    RESERVE("role.reserve"),
    /**
     * Describes that a particular person is a staff member, for a
     * particular contest.
     */
    STAFF("role.staff");

    /**
     * Language constant
     */
    private String code;

    /**
     * Mapping between role name and {@link TeamMemberRole}
     */
    private static Map<String, TeamMemberRole> mapping;

    static {
        mapping = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        mapping.put("Contestant", TeamMemberRole.CONTESTANT);
        mapping.put("Coach", TeamMemberRole.COACH);
        mapping.put("Attendee", TeamMemberRole.ATTENDEE);
        mapping.put("Staff", TeamMemberRole.STAFF);
    }

    private TeamMemberRole(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /**
     * @param role code of {@link TeamMemberRole}
     * @return team member role
     */
    public static TeamMemberRole getTeamRoleByCode(final String role) {
        return mapping.get(role);
    }

    public static List<TeamMemberRole> getAllTeamRoles() {
        return Arrays.asList(TeamMemberRole.values());
    }
};
