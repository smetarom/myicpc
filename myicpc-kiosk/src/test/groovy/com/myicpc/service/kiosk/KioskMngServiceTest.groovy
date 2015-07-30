package com.myicpc.service.kiosk

import com.myicpc.model.contest.Contest
import com.myicpc.model.kiosk.KioskContent
import com.myicpc.repository.kiosk.KioskContentRepository
import com.myicpc.service.publish.PublishService
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.VerificationModeFactory

import static org.mockito.Matchers.any
import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.*

/**
 * @author Roman Smetana
 */
class KioskMngServiceTest extends GroovyTestCase {
    @Mock
    private PublishService publishService

    @Mock
    private KioskContentRepository kioskContentRepository

    @InjectMocks
    private KioskMngService kioskMngService

    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    void testUpdateKioskContentActive() {
        def contest = mock(Contest.class)
        def kioskContent = mock(KioskContent.class)
        when(kioskContent.isActive()).thenReturn(true)
        when(kioskContent.getContest()).thenReturn(contest)

        kioskMngService.updateKioskContent(kioskContent)
        verify(kioskContentRepository).findByContest(any(Contest.class))
        verify(kioskContentRepository).save(any(List.class))
        verify(kioskContentRepository).save(any(KioskContent.class))
        verify(publishService).broadcastKioskPage(anyString())
    }

    void testUpdateKioskContentNotActive() {
        def kioskContent = mock(KioskContent.class)
        when(kioskContent.isActive()).thenReturn(false)
        when(kioskContent.getContest()).thenReturn(mock(Contest.class))
        kioskMngService.updateKioskContent(kioskContent)
        verify(kioskContentRepository, VerificationModeFactory.only()).save(kioskContent)
    }

    void testUpdateKioskContentNullKioskContent() {
        kioskMngService.updateKioskContent(null)
        Mockito.verifyZeroInteractions(kioskContentRepository)
    }

    void testUpdateKioskContentNullKioskContentContest() {
        def kioskContent = mock(KioskContent.class)
        when(kioskContent.getContest()).thenReturn(null)
        kioskMngService.updateKioskContent(kioskContent)
        Mockito.verifyZeroInteractions(kioskContentRepository)
    }
}
