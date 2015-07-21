package com.myicpc.repository.poll

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.myicpc.repository.AbstractRepositoryTest

/**
 * DbUnit test for {@link PollOptionRepository}
 *
 * @author Roman Smetana
 */
@DatabaseSetup("classpath:dbunit/poll/PollOptionRepositoryTest.xml")
class PollOptionRepositoryTest extends AbstractRepositoryTest {

}
