package com.pgis.bus.data.repositories.orm.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.orm.User;
import com.pgis.bus.data.orm.type.AuthenticateResult;
import com.pgis.bus.data.repositories.Repository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.IUsersRepository;

public class UsersRepository extends Repository implements IUsersRepository {
	private static final Logger log = LoggerFactory.getLogger(UsersRepository.class);

	public UsersRepository(IDBConnectionManager connManager) {
		super(connManager);
	}

	public UsersRepository(IDBConnectionManager connManager, boolean isCommited) {
		super(connManager, isCommited);
	}

	protected UsersRepository(IDBConnectionManager connManager, Connection c, boolean isClosed, boolean isCommited) {
		super(connManager, isCommited);
		super.isClosed = isClosed;
		super.connection = c;
	}

	@Override
	public User get(int id) throws RepositoryException {
		Connection c = super.getConnection();
		User user = null;
		try {
			// Statement sql = (Statement) conn.createStatement();
			String query = "select * from bus.users where id = ? ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				user = new User();
				user.setName(key.getString("login"));
				user.setRoleID(key.getInt("role_id"));
			}
		} catch (SQLException e) {
			user = null;
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return user;
	}

	@Override
	public AuthenticateResult authenticate(String userRole, String userName, String userPassword)
			throws RepositoryException {
		Connection conn = super.getConnection();
		try {
			String query = "SELECT bus.authenticate(?,?,?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, userRole);
			ps.setString(2, userName);
			ps.setString(3, userPassword);
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				return AuthenticateResult.getType(key.getInt(1));
			}
		} catch (SQLException e) {
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(conn);
		}
		return AuthenticateResult.c_unknown;
	}

}
