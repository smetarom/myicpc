package com.myicpc.controller.converter;

import com.myicpc.model.EntityObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.repository.CrudRepository;

/**
 * General convertor, which converts string ID into {@link EntityObject}
 * 
 * @author Roman Smetana
 * 
 * @param <T>
 *            Entity object with id
 */
public abstract class GeneralConverter<T extends EntityObject> implements Converter<String, T> {
	/**
	 * Does the conversion
	 * 
	 * @param crudRepository
	 *            repository for {@link EntityObject}
	 * @param idString
	 *            ID as string
	 * @return {@link EntityObject} or null, when the string is not valid ID
	 */
	protected T doConvert(CrudRepository<T, Long> crudRepository, String idString) {
		try {
			Long id = Long.parseLong(idString);
			return crudRepository.findOne(id);
		} catch (NumberFormatException ex) {
			return null;
		}
	}
}
