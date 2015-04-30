package com.myicpc.dto.eventFeed.convertor;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.util.Date;

public class TimestampConverter extends AbstractSingleValueConverter {

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(Date.class);
    }

    @Override
    public Object fromString(String value) {
        try {
            Double d = Double.parseDouble(value) * 1000;
            return new Date(d.longValue());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

}
