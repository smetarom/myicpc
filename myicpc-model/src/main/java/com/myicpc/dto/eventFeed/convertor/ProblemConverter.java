package com.myicpc.dto.eventFeed.convertor;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public abstract class ProblemConverter extends AbstractSingleValueConverter {
    private Contest contest;

    public ProblemConverter(Contest contest) {
        super();
        this.contest = contest;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(Problem.class);
    }

//    @Override
//    public Object fromString(String value) {
//        try {
//            return problemRepository.findBySystemIdAndContest(Long.parseLong(value), contest);
//        } catch (NumberFormatException ex) {
//
//            return null;
//        }
//    }
}
