package com.myicpc.controller.converter;

import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.repository.quest.QuestParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Convert ID to {@link QuestParticipant}
 * 
 * @author Roman Smetana
 */
@Component
public class QuestParticipantConvertor extends GeneralConverter<QuestParticipant> {

	@Autowired
	private QuestParticipantRepository questParticipantRepository;

	@Override
	public QuestParticipant convert(final String idString) {
		return doConvert(questParticipantRepository, idString);
	}
}
