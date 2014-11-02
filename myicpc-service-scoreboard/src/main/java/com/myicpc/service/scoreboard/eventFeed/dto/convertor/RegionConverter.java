package com.myicpc.service.scoreboard.eventFeed.dto.convertor;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Region;
import com.myicpc.repository.eventFeed.RegionRepository;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class RegionConverter extends AbstractSingleValueConverter {
    private RegionRepository regionRepository;
    private Contest contest;

    public RegionConverter(RegionRepository regionRepository, Contest contest) {
        super();
        this.regionRepository = regionRepository;
        this.contest = contest;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(Region.class);
    }

    @Override
    public Object fromString(String value) {
        try {
            return regionRepository.findByNameAndContest(value, contest);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
