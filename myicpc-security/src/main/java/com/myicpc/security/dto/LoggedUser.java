package com.myicpc.security.dto;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.security.SystemUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Set;

/**
 * Representation of a logged user into MyICPC
 *
 * It extends data hold by {@link User} by a list of {@link Contest}s, where the user has access
 *
 * @author Roman Smetana
 */
public class LoggedUser extends User {
    private static final long serialVersionUID = 3762676759049804047L;

    private Set<Contest> contests;

    /**
     * Constructor
     *
     * @param systemUser system user
     * @param authorities user roles
     * @param contests list of accessible contest for user
     */
    public LoggedUser(SystemUser systemUser, List<GrantedAuthority> authorities, Set<Contest> contests) {
        super(systemUser.getUsername(), systemUser.getPassword(), systemUser.isEnabled(), true, true, true, authorities);
        this.contests = contests;
    }

    /**
     * Adds a {@link Contest} to accessible contest for user
     * @param contest
     */
    public void addContest(final Contest contest) {
        contests.add(contest);
    }

    public Set<Contest> getContests() {
        return contests;
    }

    /**
     * Checks if the user has access to {@code contest}
     *
     * @param contest contest
     * @return if has access to contest
     */
    public boolean hasContest(Contest contest) {
        for (Contest c : contests) {
            if (c.getId().equals(contest.getId())) {
                return true;
            }
        }
        return false;
    }
}
