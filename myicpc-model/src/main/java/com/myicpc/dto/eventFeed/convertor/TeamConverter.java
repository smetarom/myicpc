package com.myicpc.dto.eventFeed.convertor;

import com.myicpc.model.eventFeed.Team;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public abstract class TeamConverter extends AbstractSingleValueConverter {

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(Team.class);
    }
}
