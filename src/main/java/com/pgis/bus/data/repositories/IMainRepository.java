package com.pgis.bus.data.repositories;

import java.util.ArrayList;
import java.util.Collection;

import com.pgis.bus.data.orm.Language;

public interface IMainRepository {

	Collection<Language> getAllLanguages() throws RepositoryException;

}
