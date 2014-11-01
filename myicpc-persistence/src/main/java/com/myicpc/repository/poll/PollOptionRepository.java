package com.myicpc.repository.poll;

import com.myicpc.model.poll.Poll;
import com.myicpc.model.poll.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
    @Query("SELECT po FROM PollOption po WHERE po.poll = ?1 AND po.votes > 0")
    List<PollOption> findAnsweredOptions(Poll poll);
}
