package com.myicpc.service.scoreboard.insight;

import com.myicpc.model.contest.Contest;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.service.scoreboard.dto.insight.DashboardInsightDTO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for statistics calculation on insight dashboard
 *
 * @author Roman Smetana
 */
@Service
public class DashboardInsightService {

    /**
     * Entity manager
     */
    @PersistenceContext(name = "MyICPC")
    private EntityManager em;

    @Autowired
    private ProblemRepository problemRepository;

    private static final String Q_PROBLEM_STATISTICS =
            "SELECT " +
            "   problemid," +
            "   code," +
            "   sum(case when solved=true then 1 else 0 end) AS team_solved, " +
            "   count(teamid) AS team_attemped " +
            "FROM (SELECT teamid, " +
            "   tp.problemid, " +
            "   bool_or(tp.solved) AS solved " +
            "   FROM teamproblem tp " +
            "   JOIN team t ON t.id = tp.teamid AND t.contestid = ?1 " +
            "   group by teamid, problemid) AS result " +
            "JOIN problem p ON p.id = result.problemid " +
            "GROUP BY problemid, code " +
            "ORDER BY code";

    private static final String Q_TEAM_STATISTICS =
            "SELECT " +
            "   teamid, " +
            "   sum(case when solved=true then 1 else 0 end) AS team_solved, " +
            "   count(problemid) AS team_attemped " +
            "FROM (SELECT teamid, " +
            "   tp.problemid, " +
            "   bool_or(tp.solved) AS solved " +
            "   FROM teamproblem tp " +
            "   JOIN team t ON t.id = tp.teamid AND t.contestid = ?1 " +
            "   group by teamid, problemid) AS result " +
            "GROUP BY teamid";

    /**
     * Calculates the insight dashboard statistics
     *
     * @param contest contest
     * @return calculated statistics
     */
    @Transactional(readOnly = true)
    public DashboardInsightDTO generateStatistics(final Contest contest) {
        DashboardInsightDTO dashboardInsightDTO = new DashboardInsightDTO();
        calcucateProblemStatictics(dashboardInsightDTO, contest);
        calcucateTeamStatictics(dashboardInsightDTO, contest);

        return dashboardInsightDTO;
    }

    private void calcucateProblemStatictics(final DashboardInsightDTO dashboardInsightDTO, final Contest contest) {
        int problemCount = problemRepository.countByContest(contest).intValue();
        Map<String, DashboardInsightDTO.DashboardResult> map = new HashMap<>();
        List<Object[]> problemListResult = em.createNativeQuery(Q_PROBLEM_STATISTICS)
                .setParameter(1, contest.getId())
                .getResultList();
        for (Object[] array : problemListResult) {
            DashboardInsightDTO.DashboardResult problemResult = new DashboardInsightDTO.DashboardResult(
                    array[1].toString(),
                    NumberUtils.toInt(array[2].toString(), 0),
                    NumberUtils.toInt(array[3].toString(), 0)
            );
            map.put(problemResult.getLabel(), problemResult);
        }

        for (int i = 'A'; i < 'A' + problemCount; i++) {
            String letter = String.valueOf((char) i);
            DashboardInsightDTO.DashboardResult dashboardResult = map.get(letter);
            if (dashboardResult == null) {
                dashboardResult = DashboardInsightDTO.DashboardResult.emptyResult(letter);
            }
            dashboardInsightDTO.getCurrentProblemResult().add(dashboardResult);
        }
    }


    private void calcucateTeamStatictics(final DashboardInsightDTO dashboardInsightDTO, final Contest contest) {
        List<Object[]> teamListResult = em.createNativeQuery(Q_TEAM_STATISTICS)
                .setParameter(1, contest.getId())
                .getResultList();

        int problemCount = problemRepository.countByContest(contest).intValue() + 1;

        int[] teamsSolved = new int[problemCount];
        int[] teamsAttempted = new int[problemCount];

        for (Object[] array : teamListResult) {
            int solved = NumberUtils.toInt(array[1].toString());
            int attempted = NumberUtils.toInt(array[2].toString());
            teamsSolved[solved] += 1;
            teamsAttempted[attempted] += 1;
        }

        for (int i = 1; i < problemCount; i++) {
            DashboardInsightDTO.DashboardResult teamResult = new DashboardInsightDTO.DashboardResult(
                    String.valueOf(i),
                    teamsSolved[i],
                    teamsAttempted[i]
            );
            dashboardInsightDTO.getCurrentTeamResult().add(teamResult);
        }
    }
}
