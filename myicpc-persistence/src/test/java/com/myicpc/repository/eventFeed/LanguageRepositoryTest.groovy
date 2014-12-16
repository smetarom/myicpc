package com.myicpc.repository.eventFeed

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseTearDown
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
        Language language = languageRepository.findByName("Java");
        assert language != null
        assert language.getId() == 3
    }

    @Test
    void testFindByName_caseSensitive() {
        Language language = languageRepository.findByName("java");
        assert language == null
    }

    @Test
    void testFindByNameIgnoreCase() {
        Language language = languageRepository.findByNameIgnoreCase("JAVA");
        assert language != null
        assert language.getId() == 3
    }

    @Test(expected = DataIntegrityViolationException.class)
    void unique_name() {
        Language language = new Language(name: "Java");
        languageRepository.saveAndFlush(language);
    }

    @Test
    void testFindAllOrderByName() {
        List<Language> languageList = languageRepository.findAllOrderByName();
        assert languageList.size() == 4
        assert languageList[0].getName() == "C"
        assert languageList[1].getName() == "C++"
        assert languageList[2].getName() == "Java"
        assert languageList[3].getName() == "Python"
    }

    @Test
    void testGetLanguageReport() {
        // TODO implement after refactoring insight
    }
}
