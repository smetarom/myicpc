package com.myicpc.service.scoreboard.team;

import com.myicpc.model.contest.Contest;
import com.myicpc.service.webSevice.AbstractWSService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Roman Smetana
 */
@Service
public class TeamWSService extends AbstractWSService {
    /**
     * Get JSON data about universities from ICPC WS
     *
     * @return JSON data
     * @throws IOException
     */
    public String getUniversitiesFromCM(final Contest contest) throws IOException {
        return (String) connectCM("/ws/myicpc/contest/" + contest.getCode() + "/institutions", contest);
    }

    /**
     * Get JSON data about teams from ICPC WS
     *
     * @return JSON data
     * @throws IOException
     */
    public String getTeamsFromCM(final Contest contest) throws IOException {
        return (String) connectCM("/ws/myicpc/contest/" + contest.getCode() + "/teams", contest);
    }

    /**
     * Get JSON data about staff members from ICPC WS
     *
     * @return JSON data
     * @throws IOException
     */
    public String getStaffMembersFromCM(final Contest contest) throws IOException {
        return (String) connectCM("/ws/myicpc/contest/" + contest.getCode() + "/staff-members", contest);
    }

    /**
     * Get JSON data about social info from ICPC WS
     *
     * @return JSON data
     * @throws IOException
     */
    public String getSocialInfosFromCM(final Contest contest) throws IOException {
        return (String) connectCM("/ws/myicpc/contest/" + contest.getCode() + "/social-info", contest);
    }

    public String getContestParticipantProfileUrl(String publicURLKey) {
        if (StringUtils.isEmpty(publicURLKey)) {
            return null;
        }
        return "http://" + globalSettingsService.getGlobalSettings().getContestManagementSystemURL() + "/ICPCID/IMG/" + publicURLKey;
    }
}
