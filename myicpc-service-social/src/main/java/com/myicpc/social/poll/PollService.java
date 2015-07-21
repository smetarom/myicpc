package com.myicpc.social.poll;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.poll.Poll;
import com.myicpc.model.poll.PollOption;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.poll.PollOptionRepository;
import com.myicpc.repository.poll.PollRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.notification.NotificationBuilder;
import com.myicpc.service.publish.PublishService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Management service for {@link Poll}
 *
 * @author Roman Smetana
 */
@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PublishService publishService;

    /**
     * Get all available {@link Poll.PollRepresentationType}s
     *
     * @return all {@link Poll.PollRepresentationType} list
     */
    public List<Poll.PollRepresentationType> getPollTypes() {
        return Arrays.asList(Poll.PollRepresentationType.values());
    }

    /**
     * Creates notifications for new opened polls
     * <p/>
     * Finds all new opened polls and creates notifications
     * for these new polls
     *
     * @param contest contest
     * @return created notifications
     */
    @Transactional
    public List<Notification> createNotificationsForNewPolls(Contest contest) {
        List<Poll> polls = pollRepository.findAllNonpublishedStartedPolls(new Date(), contest);
        List<Notification> notifications = new ArrayList<>();
        for (Poll poll : polls) {
            poll.setPublished(true);

            NotificationBuilder builder = new NotificationBuilder(poll);
            builder.setTitle(poll.getQuestion());
            builder.setBody(getPollChartOptionsJSON(poll, true).toString());
            builder.setEntityId(poll.getId());
            builder.setNotificationType(NotificationType.POLL_OPEN);
            builder.setContest(contest);
            Notification notification = notificationRepository.save(builder.build());
            notifications.add(notification);
            publishService.broadcastNotification(notification, contest);
        }
        return notifications;
    }

    /**
     * Finds a poll by ID and init poll options
     *
     * @param id poll ID
     * @return found poll or null if does not exist
     */
    @Transactional(readOnly = true)
    public Poll findEditPollById(Long id) {
        Poll poll = pollRepository.findOne(id);
        if (poll == null) {
            return null;
        }
        List<String> options = new ArrayList<>();
        for (PollOption option : poll.getOptions()) {
            options.add(option.getName());
        }
        poll.setChoiceStringList(options);
        return poll;
    }

    /**
     * Sets correct answer, if defined and close the poll
     *
     * @param poll poll to close
     * @return saved closed poll, or null if poll does not exist
     */
    @Transactional
    public Poll resolvePoll(Poll poll) {
        Poll managed = pollRepository.findOne(poll.getId());
        if (managed == null) {
            return null;
        }
        managed.setCorrectAnswer(poll.getCorrectAnswer());
        managed.setConclusionMessage(poll.getConclusionMessage());
        managed.setOpened(false);
        managed.setEndDate(new Date());
        return pollRepository.save(managed);
    }

    /**
     * Saves poll changes and publishes the changes to live channel
     *
     * @param poll poll to save
     * @return saved poll
     */
    @Transactional
    public Poll updatePoll(final Poll poll) {
        synchronizePollOptions(poll);
        Poll managed = pollRepository.save(poll);
        if (managed.isPublished()) {
            // TODO publish update via WS
        }
        return managed;
    }

    /**
     * Add new {@link PollOption}s and remove {@link PollOption}s, which
     * were deleted
     *
     * @param poll poll, where to synchronize poll options
     * @return poll with correct poll options
     */
    private Poll synchronizePollOptions(final Poll poll) {
        if (CollectionUtils.isEmpty(poll.getChoiceStringList())) {
            return poll;
        }
        Map<String, PollOption> pollOptions = new HashMap<>();
        if (!CollectionUtils.isEmpty(poll.getOptions())) {
            for (PollOption option : poll.getOptions()) {
                pollOptions.put(option.getName(), option);
            }
        }
        List<PollOption> newOptions = new ArrayList<>();
        for (String name : poll.getChoiceStringList()) {
            if (StringUtils.isEmpty(name)) {
                continue;
            }
            if (pollOptions.containsKey(name)) {
                newOptions.add(pollOptions.get(name));
                pollOptions.remove(name);
            } else {
                newOptions.add(new PollOption(name, poll));
            }
        }
        poll.setOptions(newOptions);
        pollRepository.flush();
        for (PollOption option : pollOptions.values()) {
            pollOptionRepository.delete(option);
        }
        return poll;
    }

    /**
     * Add a poll vote to the poll
     *
     * @param pollId   poll ID
     * @param optionId option ID
     * @return updated poll option with current votes
     */
    @Transactional
    public PollOption addVoteToPoll(final Long pollId, final Long optionId) {
        Poll poll = pollRepository.findOne(pollId);
        if (poll == null || !poll.isActive()) {
            return null;
        }

        PollOption option = pollOptionRepository.findOne(optionId);
        if (option == null) {
            return null;
        }
        option.setVotes(option.getVotes() + 1);
        option = pollOptionRepository.save(option);

        publishService.broadcastPollAnswer(poll, option);
        return option;
    }

    /**
     * Gets polls, which are open at {@code date}
     *
     * @param contest contest
     * @param date    date, when open polls are searched
     * @return collection of open polls
     */
    @Transactional(readOnly = true)
    public List<Poll> getOpenPollsWithOptions(Contest contest, Date date) {
        List<Poll> polls = pollRepository.findOpenPolls(contest, date);
        // fetch options early to prevent lazy loading exception
        for (Poll poll : polls) {
            poll.getOptions().size();
        }
        return polls;
    }

    /**
     * Encode {@code polls} to JSON representation
     * <p/>
     * It marks {@link Poll}s, which were answered based on
     * poll IDs in {@code answeredPolls}
     *
     * @param polls         list of polls to encode
     * @param answeredPolls poll IDs, which are answered
     * @return JSON representation of {@code polls}
     */
    public JsonArray getPollChartData(List<Poll> polls, Set<Long> answeredPolls) {
        JsonArray arr = new JsonArray();
        for (Poll poll : polls) {
            arr.add(createPollChartJSON(poll, answeredPolls));
        }
        return arr;
    }

    private JsonObject createPollChartJSON(final Poll poll, Set<Long> answeredPolls) {
        JsonObject pollObject = new JsonObject();
        pollObject.addProperty("id", poll.getId());
        pollObject.addProperty("question", poll.getQuestion());
        pollObject.addProperty("type", poll.getPollRepresentationType().toString());
        if (answeredPolls.contains(poll.getId())) {
            pollObject.addProperty("answered", true);
        }

        JsonArray options = new JsonArray();
        for (PollOption pollOption : poll.getOptions()) {
            JsonObject optionObject = new JsonObject();
            optionObject.addProperty("id", pollOption.getId());
            optionObject.addProperty("name", pollOption.getName());
            options.add(optionObject);
        }
        pollObject.add("options", options);

        JsonArray chartData = null;
        if (poll.getPollRepresentationType().isPieChart()) {
            chartData = getPollChartOptionsJSON(poll, false);
        } else if (poll.getPollRepresentationType().isBarChart()) {
            chartData = new JsonArray();
            JsonObject elem = new JsonObject();
            elem.addProperty("key", "data");

            elem.add("values", getPollChartOptionsJSON(poll, false));
            chartData.add(elem);
        }
        pollObject.add("chart", chartData);
        return pollObject;
    }

    private JsonArray getPollChartOptionsJSON(final Poll poll, boolean includeEmptyOptions) {
        JsonArray jsonArray = new JsonArray();
        for (PollOption option : poll.getOptions()) {
            if (!includeEmptyOptions && option.getVotes() == 0) {
                continue;
            }
            JsonObject opt = new JsonObject();
            opt.addProperty("key", option.getId());
            opt.addProperty("value", option.getVotes());
            opt.addProperty("name", option.getName());
            jsonArray.add(opt);
        }
        return jsonArray;
    }
}
