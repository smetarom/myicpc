package com.myicpc.service.validation;

import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.repository.teamInfo.ContestParticipantRepository;
import com.myicpc.service.exception.BusinessValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Business validator for {@link QuestChallenge}
 *
 * @author Roman Smetana
 */
@Component
public class ContestParticipantValidator extends BusinessEntityValidator<ContestParticipant> {

    @Autowired
    private ContestParticipantRepository contestParticipantRepository;

    @Override
    public void validate(ContestParticipant participant) throws BusinessValidationException {
        if (StringUtils.isNotEmpty(participant.getTwitterUsername())) {
            ContestParticipant duplicatedTwitter = contestParticipantRepository.findByTwitterUsernameIgnoreCase(participant.getTwitterUsername());
            if (duplicatedTwitter != null && !duplicatedTwitter.getId().equals(participant.getId())) {
                throw new BusinessValidationException("participant.error.duplicated.twitter");
            }
        }

        if (StringUtils.isNotEmpty(participant.getVineUsername())) {
            ContestParticipant duplicatedVine = contestParticipantRepository.findByVineUsernameIgnoreCase(participant.getVineUsername());
            if (duplicatedVine != null && !duplicatedVine.getId().equals(participant.getId())) {
                throw new BusinessValidationException("participant.error.duplicated.vine");
            }
        }

        if (StringUtils.isNotEmpty(participant.getInstagramUsername())) {
            ContestParticipant duplicatedInstagram = contestParticipantRepository.findByInstagramUsernameIgnoreCase(participant.getInstagramUsername());
            if (duplicatedInstagram != null && !duplicatedInstagram.getId().equals(participant.getId())) {
                throw new BusinessValidationException("participant.error.duplicated.instagram");
            }
        }
    }
}
