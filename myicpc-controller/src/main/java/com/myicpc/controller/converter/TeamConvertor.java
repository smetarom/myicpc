package com.myicpc.controller.converter;

import com.myicpc.model.eventFeed.Team;
import com.myicpc.repository.eventFeed.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Convert ID to {@link Team}
 * 
 * @author Roman Smetana
 */
@Component
public class TeamConvertor extends GeneralConverter<Team> {

	@Autowired
	private TeamRepository teamRepository;

	@Override
	public Team convert(final String idString) {
		return doConvert(teamRepository, idString);
	}
}
