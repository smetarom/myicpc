package com.myicpc.social.poll;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

    public List<Poll.PollRepresentationType> getPollTypes() {
        return Arrays.asList(Poll.PollRepresentationType.values());
    }

    @Transactional
    public void createNotificationsForNewPolls(Contest contest) {
        List<Poll> polls = pollRepository.findAllNonpublishedStartedPolls(new Date(), contest);
        for (Poll poll : polls) {
            poll.setPublished(true);

            NotificationBuilder builder = new NotificationBuilder(poll);
            builder.setTitle(poll.getQuestion());
            builder.setBody(getPollChartOptionsJSON(poll).toString());
            builder.setEntityId(poll.getId());
            builder.setNotificationType(NotificationType.POLL_OPEN);
            builder.setContest(contest);
            Notification notification = notificationRepository.save(builder.build());
            publishService.broadcastNotification(notification, contest);
        }
    }

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

    @Transactional
    public void resolvePoll(Poll poll) {
        Poll managed = pollRepository.findOne(poll.getId());
        managed.setCorrectAnswer(poll.getCorrectAnswer());
        managed.setConclusionMessage(poll.getConclusionMessage());
        managed.setOpened(false);
        managed.setEndDate(new Date());
        managed = pollRepository.save(managed);

        // TODO improve
        // PublishService.broadcastNotification(notificationService.notificationForPollClose(poll));
    }

    @Transactional
    public void updatePoll(Poll poll) {
        synchronizePollOptions(poll);
        pollRepository.save(poll);
        if (poll.isPublished()) {
            // TODO publish update via WS
        }
    }

    /**
     * Add new {@link PollOption}s and remove {@link PollOption}s, which
     * were deleted
     *
     * @param poll
     */
    protected void synchronizePollOptions(final Poll poll) {
        if (CollectionUtils.isEmpty(poll.getChoiceStringList())) {
            return;
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
    }

    @Transactional
    public void addVoteToPoll(final Long pollId, final Long optionId) {
        Poll poll = pollRepository.findOne(pollId);
        if (poll == null || !poll.isActive()) {
            return;
        }

        PollOption option = pollOptionRepository.findOne(optionId);
        option.setVotes(option.getVotes() + 1);
        pollOptionRepository.save(option);

        publishService.broadcastPollAnswer(poll, option);
    }

    @Transactional(readOnly = true)
    public List<Poll> getOpenPollsWithOptions(Contest contest, Date date) {
        List<Poll> polls = pollRepository.findOpenPolls(contest, date);
        for (Poll poll : polls) {
            poll.getOptions().size();
        }
        return polls;
    }

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
            chartData = getPollChartOptionsJSON(poll);
        } else if (poll.getPollRepresentationType().isBarChart()) {
            chartData = new JsonArray();
            JsonObject elem = new JsonObject();
            elem.addProperty("key", "data");

            elem.add("values", getPollChartOptionsJSON(poll));
            chartData.add(elem);
        }
        pollObject.add("chart", chartData);
        return pollObject;
    }

    private JsonArray getPollChartOptionsJSON(final Poll poll) {
        JsonArray jsonArray = new JsonArray();
        for (PollOption option : poll.getOptions()) {
            if (option.getVotes() == 0) {
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
