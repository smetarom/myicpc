package com.myicpc.service.kiosk

import com.myicpc.enums.NotificationType
import com.myicpc.model.contest.Contest
import com.myicpc.model.kiosk.KioskContent
import com.myicpc.model.social.Notification
import com.myicpc.repository.kiosk.KioskContentRepository
import com.myicpc.repository.social.NotificationRepository
import org.junit.Assert
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

import static org.mockito.Matchers.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

/**
 * Unit test for {@link KioskService}
 *
 * @author Roman Smetana
 */
class KioskServiceTest extends GroovyTestCase {
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private KioskContentRepository kioskContentRepository;

    @InjectMocks
    private KioskService kioskService;

    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    void testGetKioskNotifications() {
        Contest contest = mock(Contest.class)
        List<Notification> list = [mock(Notification.class), mock(Notification.class), mock(Notification.class)]
        Page<Notification> page = new PageImpl<>(list)
        when(notificationRepository.findByNotificationTypesOrderByIdDesc(any(List.class), any(Contest.class), any(PageRequest.class))).thenReturn(page)

        def notifications = kioskService.getKioskNotifications(contest)
        Assert.assertEquals list[0], notifications[2]
        Assert.assertEquals list[1], notifications[1]
        Assert.assertEquals list[2], notifications[0]
    }

    void testGetKioskNotificationsNullContest() {
        def notifications = kioskService.getKioskNotifications(null);
        Assert.assertTrue notifications.isEmpty()
    }

    void testGetKioskNotificationsJSON() {
        Contest contest = mock(Contest.class)

        Notification notification1 = mock(Notification.class)
        when(notification1.getNotificationType()).thenReturn(NotificationType.ADMIN_NOTIFICATION)
        when(notification1.getContest()).thenReturn(contest)

        Notification notification2 = mock(Notification.class)
        when(notification2.getNotificationType()).thenReturn(NotificationType.ADMIN_NOTIFICATION)
        when(notification2.getContest()).thenReturn(contest)

        List<Notification> list = [notification1, notification2]
        Page<Notification> page = new PageImpl<>(list)
        when(notificationRepository.findByNotificationTypesOrderByIdDesc(any(List.class), any(Contest.class), any(PageRequest.class))).thenReturn(page)

        def notificationsJSON = kioskService.getKioskNotificationsJSON(contest)
        Assert.assertEquals 2, notificationsJSON.size()
    }

    void testGetKioskNotificationsJSONNullContest() {
        def notificationsJSON = kioskService.getKioskNotificationsJSON(null)
        Assert.assertEquals 0, notificationsJSON.size()
    }

    void testGetActiveKioskContent() {
        List<KioskContent> list = [mock(KioskContent.class)]
        when(kioskContentRepository.findByActive(true)).thenReturn(list)

        def kioskContent = kioskService.getActiveKioskContent(mock(Contest.class))
        Assert.assertEquals list[0], kioskContent
    }

    void testGetActiveKioskContentNoActive() {
        List<KioskContent> list = []
        when(kioskContentRepository.findByActive(true)).thenReturn(list)

        def kioskContent = kioskService.getActiveKioskContent(mock(Contest.class))
        Assert.assertNull kioskContent
    }

    void testGetActiveKioskContentTwoKioskContents() {
        List<KioskContent> list = [mock(KioskContent.class), mock(KioskContent.class)]
        when(kioskContentRepository.findByActive(true)).thenReturn(list)

        def kioskContent = kioskService.getActiveKioskContent(mock(Contest.class))
        Assert.assertEquals list[0], kioskContent
    }

    void testGetActiveKioskContentNullContest() {
        def kioskContent = kioskService.getActiveKioskContent(null)
        Assert.assertNull kioskContent
    }
}
