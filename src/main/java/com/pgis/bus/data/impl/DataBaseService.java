package com.pgis.bus.data.impl;

import java.sql.Connection;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.IDataBaseService;
import com.pgis.bus.data.impl.DataBaseServiceException.err_enum;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Language;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.repositories.ICitiesRepository;
import com.pgis.bus.data.repositories.IUsersRepository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.impl.CitiesRepository;
import com.pgis.bus.data.repositories.impl.UsersRepository;

public class DataBaseService implements IDataBaseService {
	protected IUsersRepository usersRepotitory;
	protected ICitiesRepository citiesRepotitory;
	
	private static final Logger log = LoggerFactory
			.getLogger(DataBaseService.class); // 1. Объявляем переменную
													// логгера
	
	private void init() 
	{
		usersRepotitory = new UsersRepository();
		citiesRepotitory = new CitiesRepository();
	}

	protected Connection getConnection() throws DataBaseServiceException {

		Connection conn = DBConnectionFactory.getConnection();
		if (conn == null)
			throw new DataBaseServiceException(
					DataBaseServiceException.err_enum.c_connect_to_db_err);
		return conn;
	}

	public DataBaseService(){
		init();
	}
	@Override
	public Station getStation(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Station getStationByName(String name, Language lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<City> getAllCities() throws RepositoryException{
		return citiesRepotitory.getAllCities();
	}

}
