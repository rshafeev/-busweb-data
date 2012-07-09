package com.pgis.bus.data.repositories.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.Authenticate_enum;
import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.impl.DataBaseService;
import com.pgis.bus.data.orm.User;
import com.pgis.bus.data.repositories.IUsersRepository;
import com.pgis.bus.data.repositories.Repository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.RepositoryException.err_enum;

import java.sql.*;



public class UsersRepository extends Repository implements IUsersRepository {
	private static final Logger log = LoggerFactory
			.getLogger(DataBaseService.class); 
	@Override
	public User getUser(int id) throws RepositoryException {
		Connection conn = Repository.getConnection();
		User user = null;
		try {
			//Statement sql = (Statement) conn.createStatement();
			String query = "select * from bus.users where id = ? ";
			PreparedStatement ps = conn.prepareStatement(query);
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
			throw new  RepositoryException(RepositoryException.err_enum.c_sql_err);
		}		
		finally
		{
			DBConnectionFactory.closeConnection(conn);
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
		Connection conn = Repository.getConnection();
		try {
			Statement sql = conn.createStatement();
			String query = "SELECT bus.authenticate(?,?,?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1,  userRole);
			ps.setString(2,  userName);
			ps.setString(3,  userPassword);
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				return Authenticate_enum.getType(key.getInt(1));
			}
		} catch (SQLException e) {
			log.error("can not read database", e);
			throw new  RepositoryException(RepositoryException.err_enum.c_sql_err);
		}
		finally
		{
			DBConnectionFactory.closeConnection(conn);
		}
		return Authenticate_enum.c_unknown;
	}

}
