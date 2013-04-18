package com.pgis.bus.data.repositories.orm;

import java.util.Collection;
import java.util.HashMap;

import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.repositories.IRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public interface IStringValuesRepository extends IRepository {
	void insert(StringValue value) throws RepositoryException;

	void remove(int string_key) throws RepositoryException;

	Collection<StringValue> get(int name_key) throws RepositoryException;

	void update(int string_key, Collection<StringValue> values) throws RepositoryException;

	public HashMap<String, StringValue> getToHashMap(int string_key) throws RepositoryException;

}
