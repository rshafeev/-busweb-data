package com.pgis.bus.data.repositories;

import com.pgis.bus.data.Authenticate_enum;
import com.pgis.bus.data.orm.User;
import com.pgis.bus.data.repositories.RepositoryException;

public interface IUsersRepository extends IRepository {
	User getUser(int id) throws RepositoryException;
	User getUserByName(String name);
	Authenticate_enum authenticate(String userRole, String userName, String userPassword) throws RepositoryException;
	
}
