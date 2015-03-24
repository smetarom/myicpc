package com.myicpc.service.contest

import com.myicpc.repository.contest.ContestRepository
import org.junit.BeforeClass
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

// TODO fix this
public class ContestServiceTest {
    private static final String CONTEST_CODE = "CTU-OPEN-2013";

    @InjectMocks
    private ContestService contestService;

    @Mock
    private ContestRepository contestRepository;

    @BeforeClass
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    public void testGetActiveContests() throws Exception {
//        List<Contest> contests = [new Contest(), new Contest()];
//        when(contestRepository.findAll((Sort) anyObject())).thenReturn(contests);
//
//        List<Contest> result = contestService.getActiveContests();
//        assert contests == result
//    }
//
//    @Test
//    public void testGetContest() throws Exception {
//        Contest contest = new Contest(code: CONTEST_CODE);
//        when(contestRepository.findByCode(anyString())).thenReturn(contest);
//
//        Contest receivedContest = contestService.getContest(CONTEST_CODE);
//        assert contest == receivedContest
//    }
//
//    @Test(expectedExceptions = ContestNotFoundException.class)
//    public void testGetContest_NonExistingContestCode() throws Exception {
//        when(contestRepository.findByCode(anyString())).thenReturn(null);
//
//        contestService.getContest("NonExisting");
//    }
//
//    @Test
//    public void testSaveContest() throws Exception {
//
//    }
//
//    @Test
//    public void testDeleteContest() throws Exception {
//
//    }
}