package com.pgis.bus.data;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.pgis.bus.data.orm.*;
import com.pgis.bus.data.repositories.IUsersRepository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.UsersRepository;




public class AdminDataBaseService extends WebDataBaseService
                                      implements IAdminDataBaseService {
	private IUsersRepository usersRepotitory;
	private void init() 
	{
		usersRepotitory = new UsersRepository();
	}
	
	public AdminDataBaseService()
	{
		super();	
		init();
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
