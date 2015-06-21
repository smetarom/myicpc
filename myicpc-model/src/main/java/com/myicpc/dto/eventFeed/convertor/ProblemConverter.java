package com.myicpc.dto.eventFeed.convertor;

import com.myicpc.model.eventFeed.Problem;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public abstract class ProblemConverter extends AbstractSingleValueConverter {

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(Problem.class);
    }
}
