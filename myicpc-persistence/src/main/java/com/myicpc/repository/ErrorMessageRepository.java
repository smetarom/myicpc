package com.myicpc.repository;

import com.myicpc.model.ErrorMessage;
import com.myicpc.model.contest.Contest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Roman Smetana
 */
public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {

    ErrorMessage findByCauseAndContest(ErrorMessage.ErrorMessageCause cause, Contest contest);

    List<ErrorMessage> findByContestOrderByTimestampDesc(Contest contest, Pageable pageable);
}
