package com.myicpc.service.notification

import com.google.common.collect.Lists
import com.myicpc.enums.NotificationType
import com.myicpc.model.EntityObject
import com.myicpc.model.IdGeneratedContestObject
import com.myicpc.model.contest.Contest
import com.myicpc.model.eventFeed.Team
import com.myicpc.model.social.Notification
import com.myicpc.repository.social.NotificationRepository
import com.myicpc.service.AbstractServiceTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.mockito.Matchers.anyLong
import static org.mockito.Matchers.anyObject
import static org.mockito.Mockito.when

public class NotificationBuilderTest extends AbstractServiceTest {
    private Contest contest;

    @Mock
    private NotificationRepository notificationRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        contest = new Contest(id: 13L);
    }

    @Test
    public void testNotificationBuilder() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        Notification notification = builder.build();

        Assert.assertNotNull(notification.getTimestamp());
    }

    @Test
    public void testNotificationBuilder_EntityObject() throws Exception {
        EntityObject entityObject = new Contest(id: 1L);
        NotificationBuilder builder = new NotificationBuilder(entityObject);
        Notification notification = builder.build();

        Assert.assertEquals(entityObject.getId(), notification.getEntityId());
    }

    @Test
    public void testNotificationBuilder_IdGeneratedContestObject() throws Exception {
        IdGeneratedContestObject idGeneratedContestObject = new Team(id: 1L, contest: contest);

        NotificationBuilder builder = new NotificationBuilder(idGeneratedContestObject);
        Notification notification = builder.build();

        Assert.assertEquals(idGeneratedContestObject.getId(), notification.getEntityId());
        Assert.assertEquals(idGeneratedContestObject.getContest(), notification.getContest());
    }

    @Test
    public void testNotificationBuilder_Existing() throws Exception {
        Notification sampleNotification = new Notification(id: 20L);

        when(notificationRepository.findByContestAndEntityIdAndNotificationType((Contest) anyObject(), anyLong(), (NotificationType) anyObject()))
                .thenReturn(Lists.newArrayList(sampleNotification));

        IdGeneratedContestObject idGeneratedContestObject = new Team(id: 1L, contest: contest);

        NotificationBuilder builder = new NotificationBuilder(idGeneratedContestObject, null, notificationRepository);
        Notification notification = builder.build();

        Assert.assertEquals(notification, sampleNotification);
    }

    @Test
    public void testNotificationBuilder_NonPersisted() throws Exception {
        when(notificationRepository.findByContestAndEntityIdAndNotificationType((Contest) anyObject(), anyLong(), (NotificationType) anyObject()))
                .thenReturn(new ArrayList<Notification>());

        IdGeneratedContestObject idGeneratedContestObject = new Team(id: 1L, contest: contest);

        NotificationBuilder builder = new NotificationBuilder(idGeneratedContestObject, null, notificationRepository);
        Notification notification = builder.build();

        Assert.assertNotNull(notification.getTimestamp());
        Assert.assertEquals(idGeneratedContestObject.getId(), notification.getEntityId());
        Assert.assertEquals(idGeneratedContestObject.getContest(), notification.getContest());
    }


    @Test
    public void testSetTitle() throws Exception {
        NotificationBuilder builder = new NotificationBuilder(title: "A title");
        Assert.assertEquals("A title", builder.build().getTitle());
    }

    @Test
    public void testSetBody() throws Exception {
        NotificationBuilder builder = new NotificationBuilder(body: "A body");
        Assert.assertEquals("A body", builder.build().getBody());
    }

    @Test
    public void testSetNotificationType() throws Exception {
        NotificationBuilder builder = new NotificationBuilder(notificationType: NotificationType.ADMIN_NOTIFICATION);
        Assert.assertEquals(NotificationType.ADMIN_NOTIFICATION, builder.build().getNotificationType());
    }

    @Test
    public void testSetUrl() throws Exception {
        NotificationBuilder builder = new NotificationBuilder(url: "https://www.google.com");
        Assert.assertEquals("https://www.google.com", builder.build().getUrl());
    }

    @Test
    public void testSetTimestamp() throws Exception {
        Date timestamp = new Date();
        NotificationBuilder builder = new NotificationBuilder(timestamp: timestamp);
        Assert.assertEquals(timestamp, builder.build().getTimestamp());
    }

    @Test
    public void testSetCode() throws Exception {
        NotificationBuilder builder = new NotificationBuilder(code: "{\"key\":\"value\"}");
        Assert.assertEquals("{\"key\":\"value\"}", builder.build().getCode());
    }

    @Test
    public void testSetDisplayName() throws Exception {
        NotificationBuilder builder = new NotificationBuilder(authorName: "John Snow");
        Assert.assertEquals("John Snow", builder.build().getAuthorName());
    }

    @Test
    public void testSetProfilePictureUrl() throws Exception {
        NotificationBuilder builder = new NotificationBuilder(profilePictureUrl: "https://www.google.com");
        Assert.assertEquals("https://www.google.com", builder.build().getProfilePictureUrl());
    }

    @Test
    public void testSetContest() throws Exception {
        NotificationBuilder builder = new NotificationBuilder(contest: contest);
        Assert.assertEquals(contest, builder.build().getContest());
    }

    @Test
    public void testSetOffensive() throws Exception {
        // TODO waiting for offensive filter implementation
    }
}