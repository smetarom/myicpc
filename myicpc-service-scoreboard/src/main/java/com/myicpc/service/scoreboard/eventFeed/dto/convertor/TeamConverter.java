package com.myicpc.service.scoreboard.eventFeed.dto.convertor;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class TeamConverter extends AbstractSingleValueConverter {
    private TeamRepository teamRepository;
    private Contest contest;

    public TeamConverter(TeamRepository teamRepository, Contest contest) {
        super();
        this.teamRepository = teamRepository;
        this.contest = contest;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(Team.class);
    }

    @Override
    public Object fromString(String value) {
        try {
            return teamRepository.findBySystemIdAndContest(Long.parseLong(value), contest);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
