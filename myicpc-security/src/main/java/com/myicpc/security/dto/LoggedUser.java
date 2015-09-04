package com.myicpc.security.dto;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.security.SystemUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Set;

/**
 * @author Roman Smetana
 */
public class LoggedUser extends User {
    private Set<Contest> contests;

    public LoggedUser(SystemUser systemUser, List<GrantedAuthority> authorities, Set<Contest> contests) {
        super(systemUser.getUsername(), systemUser.getPassword(), systemUser.isEnabled(), true, true, true, authorities);
        this.contests = contests;
    }

    public void addContest(final Contest contest) {
        contests.add(contest);
    }

    public Set<Contest> getContests() {
        return contests;
    }

    public boolean hasContest(Contest contest) {
        for (Contest c : contests) {
            if (c.getId().equals(contest.getId())) {
                return true;
            }
        }
        return false;
    }
}
