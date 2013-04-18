package com.pgis.bus.data.repositories.orm;

import java.util.Collection;

import com.pgis.bus.data.orm.Language;
import com.pgis.bus.data.repositories.RepositoryException;

public interface ILanguagesRepository {

	Collection<Language> getAll() throws RepositoryException;

}
