package com.myicpc.repository.eventFeed;

import com.myicpc.model.eventFeed.Language;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language findByName(String name);

    Language findByNameIgnoreCase(String name);

    @Query("SELECT l FROM Language l ORDER BY l.name")
    List<Language> findAllOrderByName();

    /**
     * load all judgments and number of submissions for judgment if there is at
     * least one submission per judgment
     */
    @Query("SELECT NEW org.apache.commons.lang3.tuple.ImmutablePair(tp.resultCode, COUNT(tp)) " +
            "FROM TeamProblem tp " +
            "WHERE tp.language = ?1 AND tp.judged = true " +
            "GROUP BY tp.resultCode HAVING COUNT(tp) > 0")
    List<ImmutablePair<String, Long>> getLanguageReport(String language);
}
