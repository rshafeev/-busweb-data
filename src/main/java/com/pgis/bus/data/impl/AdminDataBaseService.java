package com.pgis.bus.data.impl;

import com.pgis.bus.data.Authenticate_enum;
import com.pgis.bus.data.IAdminDataBaseService;
import com.pgis.bus.data.orm.*;
import com.pgis.bus.data.repositories.RepositoryException;

public class AdminDataBaseService extends DataBaseService implements
		IAdminDataBaseService {

	public AdminDataBaseService() {
		super();
	}

	@Override
	public User getUser(int id) throws RepositoryException {
		return usersRepotitory.getUser(id);
	}

	@Override
	public User getUserByName(String name) {
		return null;
	}

	@Override
	public Authenticate_enum authenticate(String userRole, String userName,
			String userPassword) throws RepositoryException {
		return usersRepotitory.authenticate(userRole, userName, userPassword);
	}

	@Override
	public City updateCity(City updateCity) throws RepositoryException {
		return citiesRepotitory.updateCity(updateCity);
	}

	@Override
	public City insertCity(City newCity) throws RepositoryException {
		return citiesRepotitory.insertCity(newCity);
	}

	@Override
	public void deleteCity(int city_id) throws RepositoryException {
		this.citiesRepotitory.deleteCity(city_id);

	}

	@Override
	public Station insertStation(Station station) throws RepositoryException {
		return this.stationsRepository.insertStation(station);
	}

	@Override
	public Station updateStation(Station station) throws RepositoryException {
		return this.stationsRepository.updateStation(station);
	}

	@Override
	public void deleteStation(int station_id) throws RepositoryException {
		this.stationsRepository.deleteStation(station_id);

	}

}
