package com.myicpc.dto.eventFeed.convertor;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public abstract class TeamConverter extends AbstractSingleValueConverter {
    private Contest contest;

    public TeamConverter(Contest contest) {
        super();
        this.contest = contest;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(Team.class);
    }

//    @Override
//    public Object fromString(String value) {
//        try {
//            return teamRepository.findBySystemIdAndContest(Long.parseLong(value), contest);
//        } catch (NumberFormatException ex) {
//            return null;
//        }
//    }
}
