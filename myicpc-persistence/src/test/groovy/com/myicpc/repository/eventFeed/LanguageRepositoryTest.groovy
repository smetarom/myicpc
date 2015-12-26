package com.myicpc.repository.eventFeed

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseTearDown
import com.myicpc.model.contest.Contest
import com.myicpc.model.eventFeed.Language
import com.myicpc.repository.AbstractRepositoryTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.transaction.annotation.Transactional

/**
 * @author Roman Smetana
 */
@Transactional
@DatabaseSetup(["classpath:dbunit/contest/ContestRepositoryTest.xml", "classpath:dbunit/eventFeed/LanguageRepositoryTest.xml"])
@DatabaseTearDown("classpath:dbunit/CleanDatabase.xml")
class LanguageRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    void testFindByName() {
        Contest contest = contestRepository.findOne(1L);
        Language language = languageRepository.findByNameAndContest("Java", contest);
        assert language != null
        assert language.getId() == 3L
    }

    @Test
    void testFindByName_caseSensitive() {
        Contest contest = contestRepository.findOne(1L);
        Language language = languageRepository.findByNameAndContest("java", contest);
        assert language == null
    }

    @Test
    void testFindByNameIgnoreCase() {
        Contest contest = contestRepository.findOne(1L);
        Language language = languageRepository.findByNameIgnoreCaseAndContest("JAVA", contest);
        assert language != null
        assert language.getId() == 3L
    }

    @Test
    void testFindByContestOrderByName() {
        Contest contest = contestRepository.findOne(1L);
        List<Language> languageList = languageRepository.findByContestOrderByName(contest);
        assert languageList.size() == 3
        assert languageList[0].getName() == "C++"
        assert languageList[1].getName() == "Java"
        assert languageList[2].getName() == "Python"
    }
}
