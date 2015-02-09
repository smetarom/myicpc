package com.myicpc.controller.converter;

import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.repository.quest.QuestChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Convert ID to {@link QuestChallenge}
 * 
 * @author Roman Smetana
 */
@Component
public class QuestChallengeConvertor extends GeneralConverter<QuestChallenge> {

	@Autowired
	private QuestChallengeRepository questChallengeRepository;

	@Override
	public QuestChallenge convert(final String idString) {
		return doConvert(questChallengeRepository, idString);
	}
}
