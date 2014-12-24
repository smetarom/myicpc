package com.myicpc.service.contest

import com.myicpc.model.contest.Contest
import com.myicpc.repository.contest.ContestRepository
import com.myicpc.service.AbstractServiceTest
import com.myicpc.service.exception.ContestNotFoundException
import org.junit.BeforeClass
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.data.domain.Sort
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.mockito.Matchers.anyObject
import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.when

@Test
public class ContestServiceTest extends AbstractServiceTest {
    private static final String CONTEST_CODE = "CTU-OPEN-2013";

    @InjectMocks
    private ContestService contestService;

    @Mock
    private ContestRepository contestRepository;

    @BeforeClass
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetActiveContests() throws Exception {
        List<Contest> contests = [new Contest(), new Contest()];
        when(contestRepository.findAll((Sort) anyObject())).thenReturn(contests);

        List<Contest> result = contestService.getActiveContests();
        assert contests == result
    }

    @Test
    public void testGetContest() throws Exception {
        Contest contest = new Contest(code: CONTEST_CODE);
        when(contestRepository.findByCode(anyString())).thenReturn(contest);

        Contest receivedContest = contestService.getContest(CONTEST_CODE);
        assert contest == receivedContest
    }

    @Test(expectedExceptions = ContestNotFoundException.class)
    public void testGetContest_NonExistingContestCode() throws Exception {
        when(contestRepository.findByCode(anyString())).thenReturn(null);

        contestService.getContest("NonExisting");
    }

    @Test
    public void testSaveContest() throws Exception {

    }

    @Test
    public void testDeleteContest() throws Exception {

    }
}