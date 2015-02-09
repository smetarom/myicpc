package com.myicpc.service.validation;

import com.myicpc.model.schedule.EventRole;
import com.myicpc.model.schedule.ScheduleDay;
import com.myicpc.repository.schedule.EventRoleRepository;
import com.myicpc.repository.schedule.ScheduleDayRepository;
import com.myicpc.service.exception.BusinessValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Roman Smetana
 */
@Component
public class EventRoleValidator extends BusinessEntityValidator<EventRole> {
    @Autowired
    private EventRoleRepository eventRoleRepository;

    @Override
    public void validate(EventRole eventRole) throws BusinessValidationException {
        EventRole duplicated = eventRoleRepository.findByName(eventRole.getName());

        if (duplicated != null && !duplicated.getId().equals(eventRole.getId())) {
            throw new BusinessValidationException("eventRole.duplicatedName");
        }
    }
}
