package com.pgis.bus.data.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.pgis.bus.data.Authenticate_enum;
import com.pgis.bus.data.IAdminDataBaseService;
import com.pgis.bus.data.orm.*;
import com.pgis.bus.data.repositories.IUsersRepository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.impl.UsersRepository;




public class AdminDataBaseService extends DataBaseService
                                      implements IAdminDataBaseService {
	
	public AdminDataBaseService()
	{
		super();	
	}

	@Override
	public User getUser(int id)throws RepositoryException {
		
		return usersRepotitory.getUser(id);
	}

	@Override
	public User getUserByName(String name)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Authenticate_enum authenticate(String userRole, String userName,
			String userPassword) throws RepositoryException {
			
		return usersRepotitory.authenticate(userRole, userName, userPassword);
	}
   

  	
	
}
