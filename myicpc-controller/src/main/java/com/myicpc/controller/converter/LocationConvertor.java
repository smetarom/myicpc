package com.myicpc.controller.converter;

import com.myicpc.model.schedule.Location;
import com.myicpc.repository.schedule.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Convert ID to {@link Location}
 * 
 * @author Roman Smetana
 */
@Component
public class LocationConvertor extends GeneralConverter<Location> {

	@Autowired
	private LocationRepository locationRepository;

	@Override
	public Location convert(final String idString) {
		return doConvert(locationRepository, idString);
	}
}
