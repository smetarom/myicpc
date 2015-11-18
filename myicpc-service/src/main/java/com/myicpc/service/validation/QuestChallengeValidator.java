package com.myicpc.service.validation;

import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.repository.quest.QuestChallengeRepository;
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
public class QuestChallengeValidator extends BusinessEntityValidator<QuestChallenge> {
    private static final String HASHTAG_REGEXP = "[a-zA-Z0-9]*";

    @Autowired
    private QuestChallengeRepository challengeRepository;

    @Override
    public void validate(QuestChallenge challenge) throws BusinessValidationException {
        QuestChallenge duplicated = challengeRepository.findByHashtagSuffixAndContest(challenge.getHashtagSuffix(), challenge.getContest());

        if (duplicated != null && !duplicated.getId().equals(challenge.getId())) {
            throw new BusinessValidationException("quest.error.duplicatedHashtag");
        }

        if (StringUtils.isNotEmpty(challenge.getHashtagSuffix()) && !challenge.getHashtagSuffix().matches(HASHTAG_REGEXP)) {
            throw new BusinessValidationException("quest.error.invalidHashtag");
        }

    }
}
