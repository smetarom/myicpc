package com.myicpc.service.scoreboard.eventFeed.dto.convertor;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class ProblemConverter extends AbstractSingleValueConverter {
    private ProblemRepository problemRepository;
    private Contest contest;

    public ProblemConverter(ProblemRepository problemRepository, Contest contest) {
        super();
        this.problemRepository = problemRepository;
        this.contest = contest;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(Problem.class);
    }

    @Override
    public Object fromString(String value) {
        try {
            return problemRepository.findBySystemIdAndContest(Long.parseLong(value), contest);
        } catch (NumberFormatException ex) {

            return null;
        }
    }
}
