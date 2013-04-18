package com.pgis.bus.data.repositories.orm;

import com.pgis.bus.data.orm.User;
import com.pgis.bus.data.orm.type.AuthenticateResult;
import com.pgis.bus.data.repositories.IRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public interface IUsersRepository extends IRepository {
	User get(int id) throws RepositoryException;

	AuthenticateResult authenticate(String userRole, String userName, String userPassword) throws RepositoryException;

}
