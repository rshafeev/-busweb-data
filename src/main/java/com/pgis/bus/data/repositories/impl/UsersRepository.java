package com.pgis.bus.data.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.Authenticate_enum;
import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.orm.User;
import com.pgis.bus.data.repositories.IUsersRepository;
import com.pgis.bus.data.repositories.RepositoryException;

import java.sql.*;

public class UsersRepository extends Repository implements IUsersRepository {
	private static final Logger log = LoggerFactory
			.getLogger(UsersRepository.class);

	public UsersRepository(IDBConnectionManager connManager) {
		super(connManager);
	}

	public UsersRepository(IDBConnectionManager connManager, Connection c,
			boolean isClosed, boolean isCommited) {
		super(connManager);
		this.connection = c;
		this.isClosed = isClosed;
		this.isCommited = isCommited;
	}

	@Override
	public User getUser(int id) throws RepositoryException {
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
				user.name = key.getString("login");
				user.role_id = key.getInt("role_id");
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
	public User getUserByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Authenticate_enum authenticate(String userRole, String userName,
			String userPassword) throws RepositoryException {
		Connection conn = super.getConnection();
		try {
			String query = "SELECT bus.authenticate(?,?,?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, userRole);
			ps.setString(2, userName);
			ps.setString(3, userPassword);
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				return Authenticate_enum.getType(key.getInt(1));
			}
		} catch (SQLException e) {
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(conn);
		}
		return Authenticate_enum.c_unknown;
	}

}
