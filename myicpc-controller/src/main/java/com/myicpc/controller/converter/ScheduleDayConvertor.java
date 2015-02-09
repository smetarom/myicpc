package com.myicpc.controller.converter;

import com.myicpc.model.schedule.ScheduleDay;
import com.myicpc.repository.schedule.ScheduleDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Convert ID to {@link ScheduleDay}
 * 
 * @author Roman Smetana
 */
@Component
public class ScheduleDayConvertor extends GeneralConverter<ScheduleDay> {

	@Autowired
	private ScheduleDayRepository scheduleDayRepository;

	@Override
	public ScheduleDay convert(final String idString) {
		return doConvert(scheduleDayRepository, idString);
	}
}
