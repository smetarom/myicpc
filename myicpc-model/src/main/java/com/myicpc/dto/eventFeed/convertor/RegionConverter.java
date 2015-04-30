package com.myicpc.dto.eventFeed.convertor;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Region;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public abstract class RegionConverter extends AbstractSingleValueConverter {
    private Contest contest;

    public RegionConverter(Contest contest) {
        super();
        this.contest = contest;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(Region.class);
    }

//    @Override
//    public Object fromString(String value) {
//        try {
//            return regionRepository.findByNameAndContest(value, contest);
//        } catch (NumberFormatException ex) {
//            return null;
//        }
//    }
}
