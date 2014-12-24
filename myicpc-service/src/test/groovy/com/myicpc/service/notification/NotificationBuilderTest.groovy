package com.myicpc.service.notification

import com.google.common.collect.Lists
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
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setUp() {
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

        assert entityObject.getId() == notification.getEntityId()
    }

    @Test
    public void testNotificationBuilder_IdGeneratedContestObject() throws Exception {
        IdGeneratedContestObject idGeneratedContestObject = new Team(id: 1L, contest: contest);

        NotificationBuilder builder = new NotificationBuilder(idGeneratedContestObject);
        Notification notification = builder.build();

        assert idGeneratedContestObject.getId() == notification.getEntityId()
        assert idGeneratedContestObject.getContest() == notification.getContest()
    }

    @Test
    public void testNotificationBuilder_Existing() throws Exception {
        Notification notification1 = new Notification(id: 20L);
        when(notificationRepository.findByContestAndEntityIdAndNotificationType((Contest) anyObject(), anyLong(), (Notification.NotificationType) anyObject()))
                .thenReturn(Lists.newArrayList(notification1));

        IdGeneratedContestObject idGeneratedContestObject = new Team(id: 1L, contest: contest);

        NotificationBuilder builder = new NotificationBuilder(idGeneratedContestObject, null, notificationRepository);
        Notification notification = builder.build();

        assert notification == notification1
    }

    @Test
    public void testNotificationBuilder_NonPersisted() throws Exception {
        when(notificationRepository.findByContestAndEntityIdAndNotificationType((Contest) anyObject(), anyLong(), (Notification.NotificationType) anyObject()))
                .thenReturn(new ArrayList<Notification>());

        IdGeneratedContestObject idGeneratedContestObject = new Team(id: 1L, contest: contest);

        NotificationBuilder builder = new NotificationBuilder(idGeneratedContestObject, null, notificationRepository);
        Notification notification = builder.build();

        assert notification.getTimestamp() != null
        assert idGeneratedContestObject.getId() == notification.getEntityId()
        assert idGeneratedContestObject.getContest() == notification.getContest()
    }


    @Test
    public void testSetTitle() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setTitle("A title");
        assert builder.build().getTitle() == "A title"
    }

    @Test
    public void testSetBody() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setBody("A body");
        assert builder.build().getBody() == "A body"
    }

    @Test
    public void testSetNotificationType() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setNotificationType(Notification.NotificationType.ADMIN_NOTIFICATION);

        assert builder.build().getNotificationType() == Notification.NotificationType.ADMIN_NOTIFICATION
    }

    @Test
    public void testSetUrl() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setUrl("https://www.google.com");

        assert builder.build().getUrl() == "https://www.google.com"
    }

    @Test
    public void testSetTimestamp() throws Exception {
        Date timestamp = new Date();
        NotificationBuilder builder = new NotificationBuilder();
        builder.setTimestamp(timestamp);

        assert builder.build().getTimestamp() == timestamp
    }

    @Test
    public void testSetCode() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setCode("{\"key\":\"value\"}");

        assert builder.build().getCode() == "{\"key\":\"value\"}"
    }

    @Test
    public void testSetDisplayName() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setDisplayName("John Snow");

        assert builder.build().getDisplayName() == "John Snow"
    }

    @Test
    public void testSetProfilePictureUrl() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setProfilePictureUrl("https://www.google.com");

        assert builder.build().getProfilePictureUrl() == "https://www.google.com"
    }

    @Test
    public void testSetContest() throws Exception {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setContest(contest);

        assert builder.build().getContest() == contest
    }

    @Test
    public void testSetOffensive() throws Exception {
        // TODO waiting for offensive filter implementation
    }
}