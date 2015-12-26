package com.myicpc.repository.quest

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseTearDown
import com.myicpc.enums.ContestParticipantRole
import com.myicpc.model.contest.Contest
import com.myicpc.repository.AbstractRepositoryTest
import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

/**
 * DbUnit tests for {@link QuestParticipantRepository}
 *
 * @author Roman Smetana
 */
@DatabaseSetup(["classpath:dbunit/contest/ContestRepositoryTest.xml", "classpath:dbunit/quest/QuestParticipantRepositoryTest.xml"])
@DatabaseTearDown("classpath:dbunit/quest/CleanQuestEntities.xml")
class QuestParticipantRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private QuestParticipantRepository questParticipantRepository;

    @Test
    @Transactional(readOnly = true)
    void testFindByRoles() {
        def roles = [ContestParticipantRole.COACH, ContestParticipantRole.CONTESTANT]
        def contest = new Contest(id: 1L)
        def participants = questParticipantRepository.findByRoles(roles, contest)

        Assert.assertEquals 3, participants.size()
        participants.each {
            Assert.assertEquals 1, it.contest.id
            Assert.assertTrue it.contestParticipant.teamAssociations.any {
                it.contestParticipantRole == roles[0] || it.contestParticipantRole == roles[1]
            }
        }
    }

    @Test
    void testFindByRolesNullRoles() {
        def roles = null
        def contest = new Contest(id: 1L)
        def participants = questParticipantRepository.findByRoles(roles, contest)
        Assert.assertTrue participants.isEmpty()
    }

    @Test
    void testFindByRolesNullContest() {
        def roles = [ContestParticipantRole.CONTESTANT]
        def contest = null
        def participants = questParticipantRepository.findByRoles(roles, contest)
        Assert.assertTrue participants.isEmpty()
    }
}
