package com.myicpc.controller.converter;

import com.myicpc.model.poll.PollOption;
import com.myicpc.repository.poll.PollOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Convert ID to {@link PollOption}
 * 
 * @author Roman Smetana
 */
@Component
public class PollOptionConvertor extends GeneralConverter<PollOption> {

	@Autowired
	private PollOptionRepository pollOptionConvertor;

	@Override
	public PollOption convert(final String idString) {
		return doConvert(pollOptionConvertor, idString);
	}
}
