package com.myicpc.service.validation;

import com.myicpc.model.schedule.EventRole;
import com.myicpc.repository.schedule.EventRoleRepository;
import com.myicpc.service.exception.BusinessValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Business validator for {@link EventRole}
 *
 * @author Roman Smetana
 */
@Component
public class EventRoleValidator extends BusinessEntityValidator<EventRole> {
    @Autowired
    private EventRoleRepository eventRoleRepository;

    @Override
    public void validate(EventRole eventRole) throws BusinessValidationException {
        EventRole duplicated = eventRoleRepository.findByNameAndContest(eventRole.getName(), eventRole.getContest());

        if (duplicated != null && !duplicated.getId().equals(eventRole.getId())) {
            throw new BusinessValidationException("eventRole.duplicatedName");
        }
    }
}
