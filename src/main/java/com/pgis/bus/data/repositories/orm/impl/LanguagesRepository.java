package com.pgis.bus.data.repositories.orm.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.Language;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.Repository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.ILanguagesRepository;

public class LanguagesRepository extends Repository implements ILanguagesRepository {
	private static final Logger log = LoggerFactory.getLogger(LanguagesRepository.class);

	public LanguagesRepository(IConnectionManager connManager) {
		super(connManager);
	}

	public Collection<Language> getAll() throws SQLException {
		Collection<Language> langs = null;
		Connection c = super.getConnection();
		try {
			// Statement sql = (Statement) conn.createStatement();
			String query = "select * from bus.languages";
			PreparedStatement ps = c.prepareStatement(query);
			ResultSet key = ps.executeQuery();
			langs = new ArrayList<Language>();

			while (key.next()) {
				Language lang = new Language();
				lang.id = LangEnum.valueOf(key.getString("id"));
				lang.name = key.getString("name");
				langs.add(lang);
			}
		} catch (SQLException e) {
			langs = null;
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
		return langs;
	}
}
