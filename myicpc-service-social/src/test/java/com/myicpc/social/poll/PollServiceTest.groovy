package com.myicpc.social.poll

import com.myicpc.enums.NotificationType
import com.myicpc.model.contest.Contest
import com.myicpc.model.poll.Poll
import com.myicpc.model.poll.PollOption
import com.myicpc.model.social.Notification
import com.myicpc.repository.poll.PollOptionRepository
import com.myicpc.repository.poll.PollRepository
import com.myicpc.repository.social.NotificationRepository
import com.myicpc.service.publish.PublishService
import org.junit.Assert
import org.mockito.AdditionalAnswers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.mockito.Matchers.any
import static org.mockito.Matchers.anyLong
import static org.mockito.Mockito.*

/**
 * Unit tests for {@link PollService}
 *
 * @author Roman Smetana
 */
class PollServiceTest extends GroovyTestCase {

    @Mock
    private PollRepository pollRepository;

    @Mock
    private PollOptionRepository pollOptionRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private PublishService publishService;

    @InjectMocks
    private PollService pollService;

    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    void testGetPollTypes() {
        def list = [Poll.PollRepresentationType.BAR_CHART, Poll.PollRepresentationType.PIE_CHART]
        Assert.assertEquals list, pollService.getPollTypes()
    }

    void testCreateNotificationsForNewPolls() {
        def contest = mock(Contest.class)
        def pollOption1 = new PollOption(id: 1L, name: "A", votes: 0)
        def pollOption2 = new PollOption(id: 2L, name: "B", votes: 0)
        def poll = new Poll(id: 1L, question: "Question?", published: false, contest: contest, options: [pollOption1, pollOption2])
        when(pollRepository.findAllNonpublishedStartedPolls(any(Date.class), any(Contest.class))).thenReturn([poll])
        when(notificationRepository.save(any(Notification.class))).then(AdditionalAnswers.returnsFirstArg())
        def notifications = pollService.createNotificationsForNewPolls(contest)

        Assert.assertEquals 1, notifications.size()
        Assert.assertEquals poll.getQuestion(), notifications[0].getTitle()
        Assert.assertEquals "[{\"key\":1,\"value\":0,\"name\":\"A\"},{\"key\":2,\"value\":0,\"name\":\"B\"}]", notifications[0].getBody()
        Assert.assertEquals contest, notifications[0].getContest()
        Assert.assertEquals poll.getId(), notifications[0].getEntityId()
        Assert.assertEquals NotificationType.POLL_OPEN, notifications[0].getNotificationType()

        verify(publishService, times(1)).broadcastNotification(any(Notification.class), any(Contest.class))
        verify(notificationRepository, times(1)).save(any(Notification.class))
    }

    void testCreateNotificationsForNewPollsNullContest() {
        pollService.createNotificationsForNewPolls(null)
        verifyZeroInteractions(publishService)
        verify(notificationRepository, times(0)).save(any(Notification.class))
    }

    void testFindEditPollById() {
        def pollOption1 = new PollOption(id: 1L, name: "A", votes: 0)
        def pollOption2 = new PollOption(id: 2L, name: "B", votes: 0)
        def choices = ["A", "B"]
        def poll = new Poll(id: 1L, question: "Question?", published: false, contest: mock(Contest.class), options: [pollOption1, pollOption2])
        when(pollRepository.findOne(anyLong())).thenReturn(poll)

        def foundPoll = pollService.findEditPollById(1)

        Assert.assertNotNull foundPoll
        Assert.assertEquals foundPoll.getChoiceStringList().size(), 2
        Assert.assertEquals choices, foundPoll.getChoiceStringList()
    }

    void testFindEditPollByIdNotExistingPoll() {
        when(pollRepository.findOne(anyLong())).thenReturn(null)
        def poll = pollService.findEditPollById(-1)
        Assert.assertNull poll
    }

    void testResolvePoll() {
        def poll = new Poll(id: 1L, question: "Question?", published: false, contest: mock(Contest.class))
        when(pollRepository.findOne(anyLong())).thenReturn(poll)
        when(pollRepository.save(any(Poll.class))).then(AdditionalAnswers.returnsFirstArg())

        def resolvedPoll = pollService.resolvePoll(poll)
        Assert.assertFalse resolvedPoll.isOpened()
    }

    void testResolvePollNotExistingPoll() {
        def poll = new Poll(id: 1L, question: "Question?", published: false, contest: mock(Contest.class))
        when(pollRepository.findOne(anyLong())).thenReturn(null)

        def resolvedPoll = pollService.resolvePoll(poll)
        Assert.assertNull resolvedPoll
    }

    void testUpdatePollNewOptions() {
        def poll = new Poll(choiceStringList: ["A", "B"])
        when(pollRepository.save(any(Poll.class))).then(AdditionalAnswers.returnsFirstArg())

        def updatedPoll = pollService.updatePoll(poll)

        Assert.assertEquals 2, updatedPoll.getOptions().size()
        Assert.assertEquals "A", updatedPoll.options[0].name
        Assert.assertEquals "B", updatedPoll.options[1].name
    }

    void testUpdatePollChangedOptions() {
        def pollOption1 = new PollOption(id: 1L, name: "A", votes: 0)
        def pollOption2 = new PollOption(id: 2L, name: "B", votes: 0)
        def poll = new Poll(choiceStringList: ["A", "C"], options: [pollOption1, pollOption2])
        when(pollRepository.save(any(Poll.class))).then(AdditionalAnswers.returnsFirstArg())

        def updatedPoll = pollService.updatePoll(poll)

        Assert.assertEquals 2, updatedPoll.getOptions().size()
        Assert.assertEquals "A", updatedPoll.options[0].name
        Assert.assertEquals "C", updatedPoll.options[1].name
    }

    void testAddVoteToPoll() {
        def option = new PollOption(id: 1L, name: "A", votes: 3)
        def poll = mock(Poll.class)
        when(poll.isActive()).thenReturn(true)
        when(pollRepository.findOne(anyLong())).thenReturn(poll)
        when(pollOptionRepository.findOne(1L)).thenReturn(option)
        when(pollOptionRepository.save(any(PollOption.class))).then(AdditionalAnswers.returnsFirstArg())

        def updatedOption = pollService.addVoteToPoll(1L, 1L)
        Assert.assertEquals 4, updatedOption.votes
        verify(publishService, times(1)).broadcastPollAnswer(any(Poll.class), any(PollOption.class))
    }

    void testAddVoteToPollNotExistingPollOption() {
        def poll = mock(Poll.class)
        when(poll.isActive()).thenReturn(true)
        when(pollRepository.findOne(anyLong())).thenReturn(poll)
        when(pollOptionRepository.findOne(anyLong())).thenReturn(null)

        def updatedOption = pollService.addVoteToPoll(1L, 1L)
        Assert.assertNull updatedOption
    }

    void testAddVoteToPollNotExistingPoll() {
        when(pollRepository.findOne(anyLong())).thenReturn(null)

        def option = pollService.addVoteToPoll(1L, 2L)
        Assert.assertNull option
    }

    void testAddVoteToPollClosedPoll() {
        def poll = mock(Poll.class)
        when(poll.isActive()).thenReturn(false)
        when(pollRepository.findOne(anyLong())).thenReturn(poll)

        def option = pollService.addVoteToPoll(1L, 2L)
        Assert.assertNull option
    }

    void testGetOpenPollsWithOptions() {
        def polls = [new Poll(options: []), new Poll(options: []), new Poll(options: [])]
        when(pollRepository.findOpenPolls(any(Contest.class), any(Date.class))).thenReturn(polls)

        def returnedPolls = pollService.getOpenPollsWithOptions(mock(Contest.class), mock(Date.class))

        Assert.assertEquals polls.size(), returnedPolls.size()
        Assert.assertEquals polls, returnedPolls
    }

    void testGetOpenPollsWithOptionsNullDate() {
        def polls = pollService.getOpenPollsWithOptions(mock(Contest.class), null)
        Assert.assertTrue polls.isEmpty()
    }

    void testGetOpenPollsWithOptionsNullContest() {
        def polls = pollService.getOpenPollsWithOptions(null, mock(Date.class))
        Assert.assertTrue polls.isEmpty()
    }
}
