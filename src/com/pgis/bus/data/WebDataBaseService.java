package com.pgis.bus.data;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.orm.*;


public class WebDataBaseService implements IDataBaseService {
	private static final Logger log = LoggerFactory
			.getLogger(WebDataBaseService.class); // 1. Объявляем переменную
													// логгера
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
