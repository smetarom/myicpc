package com.myicpc.service.validation;

import com.myicpc.model.schedule.Location;
import com.myicpc.repository.schedule.LocationRepository;
import com.myicpc.service.exception.BusinessValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Roman Smetana
 */
@Component
public class LocationValidator extends BusinessEntityValidator<Location> {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public void validate(Location location) throws BusinessValidationException {
        Location duplicated = locationRepository.findByCode(location.getCode());

        if (duplicated != null && !duplicated.getId().equals(location.getId())) {
            throw new BusinessValidationException("location.duplicatedCode");
        }
    }
}
