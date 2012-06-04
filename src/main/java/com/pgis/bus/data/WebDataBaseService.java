package com.pgis.bus.data;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.orm.Language;
import com.pgis.bus.data.orm.Station;

public class WebDataBaseService implements IDataBaseService {
	private static final Logger log = LoggerFactory
			.getLogger(WebDataBaseService.class); // 1. Объявляем переменную
													// логгера

	protected Connection getConnection() throws WebDataBaseServiceException {

		Connection conn = DBConnectionFactory.getConnection();
		if (conn == null)
			throw new WebDataBaseServiceException(
					WebDataBaseServiceException.err_enum.c_connect_to_db_err);
		return conn;
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

}
