package com.myicpc.service.contest;

import com.myicpc.model.contest.Contest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CMServiceTest {
    private static final String CONTEST_CODE = "CTU-OPEN-2013";

    @Mock
    private Contest contest;

    @Before
    public void setup() {
        when(contest.getCode()).thenReturn(CONTEST_CODE);
    }

//    @Test
//    public void testGetUniversitiesFromCM() throws Exception {
//
//    }
//
//    @Test
//    public void testGetTeamsFromCM() throws Exception {
//
//    }
//
//    @Test
//    public void testGetTeamCoordinatesCM() throws Exception {
//
//    }
//
//    @Test
//    public void testGetStaffMembersFromCM() throws Exception {
//
//    }
//
//    @Test
//    public void testGetSocialInfosFromCM() throws Exception {
//
//    }
}