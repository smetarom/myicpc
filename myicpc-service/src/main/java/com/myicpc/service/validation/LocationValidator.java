package com.myicpc.service.validation;

import com.myicpc.model.schedule.Location;
import com.myicpc.repository.schedule.LocationRepository;
import com.myicpc.service.exception.BusinessValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Business validator for {@link Location}
 *
 * @author Roman Smetana
 */
@Component
public class LocationValidator extends BusinessEntityValidator<Location> {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public void validate(Location location) throws BusinessValidationException {
        Location duplicated = locationRepository.findByCodeAndContest(location.getCode(), location.getContest());

        if (duplicated != null && !duplicated.getId().equals(location.getId())) {
            throw new BusinessValidationException("location.duplicatedCode");
        }
    }
}
