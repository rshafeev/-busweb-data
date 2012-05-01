package com.pgis.bus.data;

import com.pgis.bus.data.orm.*;

public interface IDataBaseService {

	Station getStation(int id);
	Station getStationByName(String name,Language lang);
	
}
