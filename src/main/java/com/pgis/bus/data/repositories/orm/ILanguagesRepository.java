package com.pgis.bus.data.repositories.orm;

import java.sql.SQLException;
import java.util.Collection;

import com.pgis.bus.data.orm.Language;

public interface ILanguagesRepository {

	Collection<Language> getAll() throws SQLException;

}
