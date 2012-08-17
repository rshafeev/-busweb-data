package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.orm.Language;
import com.pgis.bus.data.repositories.IMainRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public class MainRepository extends Repository implements IMainRepository {
	private static final Logger log = LoggerFactory.getLogger(MainRepository.class);

	public MainRepository() {
		connection = null;
		isClosed = true;
		isCommited = true;
	}

	public MainRepository(Connection c, boolean isClosed, boolean isCommited) {
		this.connection = c;
		this.isClosed = isClosed;
		this.isCommited = isCommited;
	}

	
	public Collection<Language> getAllLanguages() throws RepositoryException {
		Collection<Language> langs = null;
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		try {
			// Statement sql = (Statement) conn.createStatement();
			String query = "select * from bus.languages";
			PreparedStatement ps = c.prepareStatement(query);
			ResultSet key = ps.executeQuery();
			langs = new ArrayList<Language>();

			while (key.next()) {
				Language lang = new Language();
				lang.id = key.getString("id");
				lang.name = key.getString("name");
				langs.add(lang);
			}
		} catch (SQLException e) {
			langs = null;
			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if(isClosed)
			DBConnectionFactory.closeConnection(c);
		}
		return langs;
	}
}
