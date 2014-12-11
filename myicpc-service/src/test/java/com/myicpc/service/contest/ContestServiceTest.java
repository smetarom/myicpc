package com.myicpc.service.contest;

import com.google.common.collect.Lists;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.service.exception.ContestNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ContestServiceTest {
    private static final String CONTEST_CODE = "CTU-OPEN-2013";

    @InjectMocks
    private ContestService contestService;

    @Mock
    private ContestRepository contestRepository;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetActiveContests() throws Exception {
        List<Contest> contests = Lists.newArrayList(new Contest(), new Contest());
        when(contestRepository.findAll((Sort) anyObject())).thenReturn(contests);

        List<Contest> result = contestService.getActiveContests();
        Assert.assertEquals(contests, result);
    }

    @Test
    public void testGetContest() throws Exception {
        Contest contest = new Contest();
        contest.setCode(CONTEST_CODE);
        when(contestRepository.findByCode(anyString())).thenReturn(contest);

        contestService.getContest(CONTEST_CODE);
    }

    @Test(expected = ContestNotFoundException.class)
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