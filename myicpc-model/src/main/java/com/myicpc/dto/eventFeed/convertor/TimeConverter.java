package com.myicpc.dto.eventFeed.convertor;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import org.apache.commons.lang3.math.NumberUtils;

public class TimeConverter extends AbstractSingleValueConverter {

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(Integer.class);
    }

    @Override
    public Object fromString(String value) {
        String[] ss = value.split(":");
        if (ss.length == 3) {
            return 60 * 60 * NumberUtils.toInt(ss[0]) + 60 * NumberUtils.toInt(ss[1]) + NumberUtils.toInt(ss[2]);
        }
        return 0;
    }

}
