package com.myicpc.service.validation;

import com.myicpc.model.schedule.Event;
import com.myicpc.repository.schedule.EventRepository;
import com.myicpc.service.exception.BusinessValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Roman Smetana
 */
@Component
public class EventValidator extends BusinessEntityValidator<Event> {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public void validate(Event event) throws BusinessValidationException {
        Event duplicated = eventRepository.findByCodeAndContest(event.getCode(), event.getContest());

        if (duplicated != null && !duplicated.getId().equals(event.getId())) {
            throw new BusinessValidationException("event.duplicatedCode");
        }
    }
}
