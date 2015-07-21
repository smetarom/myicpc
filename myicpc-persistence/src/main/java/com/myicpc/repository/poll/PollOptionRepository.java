package com.myicpc.repository.poll;

import com.myicpc.model.poll.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DAO repository for {@link PollOption}
 *
 * @author Roman Smetana
 */
public interface PollOptionRepository extends JpaRepository<PollOption, Long> {

}
