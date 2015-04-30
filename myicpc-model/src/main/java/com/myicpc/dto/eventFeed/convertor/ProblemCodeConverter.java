package com.myicpc.dto.eventFeed.convertor;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class ProblemCodeConverter extends AbstractSingleValueConverter {

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(String.class);
    }

    @Override
    public Object fromString(String value) {
        try {
            Long id = Long.parseLong(value);
            return Character.toString((char) (64 + id));
        } catch (NumberFormatException ex) {

            return null;
        }
    }
}
