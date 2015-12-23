package com.myicpc.service.notification;

import com.myicpc.model.ErrorMessage;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.ErrorMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service managing error messages from background jobs
 *
 * @author Roman Smetana
 */
@Service
public class ErrorMessageService {

    @Autowired
    private ErrorMessageRepository errorMessageRepository;

    public void createErrorMessage(ErrorMessage.ErrorMessageCause cause, Contest contest) {
        createErrorMessage(cause, contest, null);
    }

    public void createErrorMessage(ErrorMessage.ErrorMessageCause cause, Contest contest, String message) {
        ErrorMessage errorMessage = errorMessageRepository.findByCauseAndContest(cause, contest);
        if (errorMessage == null) {
            errorMessage = new ErrorMessage();
            errorMessage.setCause(cause);
            errorMessage.setContest(contest);
        }
        errorMessage.setMessage(message);
        errorMessage.setSolved(false);
        errorMessage.setTimestamp(new Date());

        errorMessageRepository.save(errorMessage);
    }

    public List<ErrorMessage> getContestErrorMessages(final Contest contest) {
        return errorMessageRepository.findByContestOrderByTimestampDesc(contest, null);
    }

    public List<ErrorMessage> getRecentContestErrorMessages(final Contest contest) {
        Pageable pageable = new PageRequest(0, 5);
        return errorMessageRepository.findByContestOrderByTimestampDesc(contest, pageable);
    }

    public List<ErrorMessage> getAllErrorMessages() {
        return errorMessageRepository.findAll(new Sort(Sort.Direction.DESC, "timestamp"));
    }

    public List<ErrorMessage> getRecentAllErrorMessages() {
        Pageable pageable = new PageRequest(0, 5, Sort.Direction.DESC, "timestamp");
        return errorMessageRepository.findAll(pageable).getContent();
    }
}
