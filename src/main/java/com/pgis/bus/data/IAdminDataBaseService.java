package com.pgis.bus.data;

import java.sql.SQLException;

import javax.naming.NamingException;

import com.pgis.bus.data.orm.User;
import com.pgis.bus.data.repositories.RepositoryException;

public interface IAdminDataBaseService extends  IDataBaseService{
    
	User getUser(int id) throws WebDataBaseServiceException;
	User getUserByName(String name)throws WebDataBaseServiceException;
	Authenticate_enum authenticate(String userRole, String userName, String userPassword) throws  RepositoryException;
}
