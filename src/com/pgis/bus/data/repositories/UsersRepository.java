package com.pgis.bus.data.repositories;

import java.sql.Connection;

import com.pgis.bus.data.orm.*;
import com.pgis.bus.data.*;

public class UsersRepository extends Repository implements IUsersRepository {

	@Override
	public User getUser(int id) throws RepositoryException {
		Connection conn = Repository.getConnection();
		
		return null;
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
		
		
		DBConnectionFactory.closeConnection(conn);
		
		return null;
	}

}
