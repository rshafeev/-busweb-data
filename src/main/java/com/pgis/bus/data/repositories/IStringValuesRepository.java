package com.pgis.bus.data.repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.pgis.bus.data.orm.StringValue;

public interface IStringValuesRepository extends IRepository {

	void updateStringValues(int string_key, Collection<StringValue> values)
			throws RepositoryException;

	public HashMap<String, StringValue> getStringValuesToHashMap(int string_key)
			throws RepositoryException;

	/**
	 * 
	 * @param value
	 * @return id of the new StringValue
	 * @throws RepositoryException
	 */
	int insertStringValue(StringValue value) throws RepositoryException;

	void deleteStringValues(int string_key) throws RepositoryException;
}
