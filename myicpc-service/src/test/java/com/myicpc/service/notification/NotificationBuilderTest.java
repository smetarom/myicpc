package com.myicpc.service.notification;

import com.google.common.collect.Lists;
import com.myicpc.model.EntityObject;
import com.myicpc.model.IdGeneratedContestObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.NotificationRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.when;

public class NotificationBuilderTest {
    private Contest contest;

    @Mock
    private NotificationRepository notificationRepository;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setUp() {
        contest = new Contest();
        contest.setId(13L);
    }

    @Test
    public void testNotificationBuilder() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        Notification notification = builder.build();

        Assert.assertNotNull(notification.getTimestamp());
    }

    @Test
    public void testNotificationBuilder_EntityObject() throws Exception {
        EntityObject entityObject = new Contest();
        entityObject.setId(1L);
        NotificationBuilder builder = new NotificationBuilder(entityObject);
        Notification notification = builder.build();

        Assert.assertEquals(entityObject.getId(), notification.getEntityId());
    }

    @Test
    public void testNotificationBuilder_IdGeneratedContestObject() throws Exception {
        IdGeneratedContestObject idGeneratedContestObject = new Team();
        idGeneratedContestObject.setId(1L);
        idGeneratedContestObject.setContest(contest);

        NotificationBuilder builder = new NotificationBuilder(idGeneratedContestObject);
        Notification notification = builder.build();

        Assert.assertEquals(idGeneratedContestObject.getId(), notification.getEntityId());
        Assert.assertEquals(idGeneratedContestObject.getContest(), notification.getContest());
    }

    @Test
    public void testNotificationBuilder_Existing() throws Exception {
        Notification notification1 = new Notification();
        notification1.setId(20L);
        when(notificationRepository.findByContestAndEntityIdAndNotificationType((Contest) anyObject(), anyLong(), (Notification.NotificationType) anyObject()))
                .thenReturn(Lists.newArrayList(notification1));

        IdGeneratedContestObject idGeneratedContestObject = new Team();
        idGeneratedContestObject.setId(1L);
        idGeneratedContestObject.setContest(contest);

        NotificationBuilder builder = new NotificationBuilder(idGeneratedContestObject, null, notificationRepository);
        Notification notification = builder.build();

        Assert.assertEquals(notification, notification1);
    }

    @Test
    public void testNotificationBuilder_NonPersisted() throws Exception {
        when(notificationRepository.findByContestAndEntityIdAndNotificationType((Contest) anyObject(), anyLong(), (Notification.NotificationType) anyObject()))
                .thenReturn(new ArrayList<Notification>());

        IdGeneratedContestObject idGeneratedContestObject = new Team();
        idGeneratedContestObject.setId(1L);
        idGeneratedContestObject.setContest(contest);

        NotificationBuilder builder = new NotificationBuilder(idGeneratedContestObject, null, notificationRepository);
        Notification notification = builder.build();

        Assert.assertNotNull(notification.getTimestamp());
        Assert.assertEquals(idGeneratedContestObject.getId(), notification.getEntityId());
        Assert.assertEquals(idGeneratedContestObject.getContest(), notification.getContest());
    }


    @Test
    public void testSetTitle() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setTitle("A title");
        Assert.assertEquals("A title", builder.build().getTitle());
    }

    @Test
    public void testSetBody() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setBody("A body");
        Assert.assertEquals("A body", builder.build().getBody());
    }

    @Test
    public void testSetNotificationType() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setNotificationType(Notification.NotificationType.ADMIN_NOTIFICATION);
        Assert.assertEquals(Notification.NotificationType.ADMIN_NOTIFICATION, builder.build().getNotificationType());
    }

    @Test
    public void testSetUrl() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setUrl("https://www.google.com");
        Assert.assertEquals("https://www.google.com", builder.build().getUrl());
    }

    @Test
    public void testSetTimestamp() throws Exception {
        Date timestamp = new Date();
        NotificationBuilder builder = new NotificationBuilder();
        builder.setTimestamp(timestamp);
        Assert.assertEquals(timestamp, builder.build().getTimestamp());
    }

    @Test
    public void testSetCode() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setCode("{\"key\":\"value\"}");
        Assert.assertEquals("{\"key\":\"value\"}", builder.build().getCode());
    }

    @Test
    public void testSetDisplayName() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setDisplayName("John Snow");
        Assert.assertEquals("John Snow", builder.build().getDisplayName());
    }

    @Test
    public void testSetProfilePictureUrl() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setProfilePictureUrl("https://www.google.com");
        Assert.assertEquals("https://www.google.com", builder.build().getProfilePictureUrl());
    }

    @Test
    public void testSetContest() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setContest(contest);
        Assert.assertEquals(contest, builder.build().getContest());
    }

    @Test
    public void testSetOffensive() throws Exception {
        // TODO waiting for offensive filter implementation
    }
}