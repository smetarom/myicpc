package com.myicpc.controller.converter;

import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Convert ID to {@link TeamInfo}
 * 
 * @author Roman Smetana
 */
@Component
public class TeamInfoConvertor extends GeneralConverter<TeamInfo> {

	@Autowired
	private TeamInfoRepository teamInfoRepository;

	@Override
	public TeamInfo convert(final String idString) {
		return doConvert(teamInfoRepository, idString);
	}
}
