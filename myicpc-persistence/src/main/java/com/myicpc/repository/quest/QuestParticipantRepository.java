package com.myicpc.repository.quest;

import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.model.teamInfo.ContestParticipantAssociation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * DAO repository for {@link QuestParticipant}
 *
 * @author Roman Smetana
 */
public interface QuestParticipantRepository extends PagingAndSortingRepository<QuestParticipant, Long> {
    List<QuestParticipant> findByContest(Contest contest);

    List<QuestParticipant> findByContestOrderByContestParticipantLastnameAsc(Contest contest);

    List<QuestParticipant> findByContestOrderByPointsDescContestParticipantFirstnameAsc(Contest contest);

    QuestParticipant findByContestAndContestParticipantTwitterUsernameIgnoreCase(Contest contest, String twitterUsername);

    QuestParticipant findByContestAndContestParticipantVineUsernameIgnoreCase(Contest contest, String vineUsername);

    QuestParticipant findByContestAndContestParticipantInstagramUsernameIgnoreCase(Contest contest, String instagramUsername);

    /**
     * Finds {@link QuestParticipant}s, which are linked to {@link ContestParticipant} with assigned
     * {@link ContestParticipantAssociation#contestParticipantRole} contained in {@code roles}
     *
     * @param roles   list of searched roles, the contest participant must have at least one of these
     * @param contest contest
     * @return participants, which have a subset of {@code roles}
     */
    @Query("SELECT DISTINCT qp " +
            "FROM QuestParticipant qp " +
            "   JOIN FETCH qp.contestParticipant tm " +
            "   JOIN tm.teamAssociations ta " +
            "WHERE ta.contestParticipantRole IN ?1 " +
            "   AND qp.contest = ?2 " +
            "ORDER BY qp.points DESC, tm.firstname ASC")
    List<QuestParticipant> findByRoles(List<ContestParticipantRole> roles, Contest contest);

}
