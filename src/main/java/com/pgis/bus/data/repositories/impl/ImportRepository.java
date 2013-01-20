package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.helpers.LoadImportObjectOptions;
import com.pgis.bus.data.models.ImportRouteModel;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.ImportObject;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.repositories.ICitiesRepository;
import com.pgis.bus.data.repositories.IStationsRepository;
import com.pgis.bus.data.repositories.IimportRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public class ImportRepository extends Repository implements IimportRepository {

	private static final Logger log = LoggerFactory
			.getLogger(ImportRepository.class);

	public ImportRepository(IDBConnectionManager connManager) {
		super(connManager);
	}

	public ImportRepository(Connection c,
			boolean isClosed, boolean isCommited) {
		super();
		this.connection = c;
		this.isClosed = isClosed;
		this.isCommited = isCommited;
	}

	@Override
	public void insertObject(ImportObject obj) throws RepositoryException {
		// validation
		if (obj == null || obj.obj == null)
			throw new RepositoryException(
					RepositoryException.err_enum.c_input_data);
		Connection c = super.getConnection();
		try {

			ImportRepository rep = new ImportRepository(c, false, false);
			ImportObject newObj = rep.getObject(obj.city_key, obj.route_type,
					obj.route_number);
			if (newObj != null) {
				newObj.obj = obj.obj;
				rep.updateObjectByID(newObj);
			} else {
				String query = "INSERT INTO bus.import_objects (city_key,route_type,route_number,obj) "
						+ "VALUES(?,bus.route_type_enum(?),?,?) RETURNING id; ";
				PreparedStatement ps = c.prepareStatement(query);
				ps.setString(1, obj.city_key);
				ps.setString(2, obj.route_type);
				ps.setString(3, obj.route_number);
				ps.setString(4, obj.obj);
				ResultSet key = ps.executeQuery();

				if (key.next()) {
					int id = key.getInt(1);
					obj.id = id;
				} else {
					throw new Exception("not found new id of ImportObject");
				}
			}
			super.commit(c);
		} catch (Exception e) {
			log.error("insertObject() exception: ", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
	}

	@Override
	public ImportRouteModel getRouteModelForObj(int objID)
			throws RepositoryException {
		try {
			ImportObject obj = getObject(objID);
			if (obj == null)
				return null;
			Connection c = super.getConnection();
			ICitiesRepository cities = new CitiesRepository(c,false,false);
			City city = cities.getCityByKey(obj.city_key);
			if (city == null)
				throw new RepositoryException("Can not find city");
			ImportRouteModel routeModel = (new Gson()).fromJson(obj.obj,
					ImportRouteModel.class);

			routeModel.setCityID(city.id);
			routeModel.setNumber(obj.route_number);
			routeModel.setRouteType(obj.route_type);
			routeModel.init();

			IStationsRepository stations = new StationsRepository(c,false,false);

			int ind = -1;
			for (int i = 0; i < routeModel.getDirectStations().length; i++) {
				Station rowStation = routeModel.getDirectStations()[i];
				Station findStation = stations.getStation(
						rowStation.getNameByLanguage("c_ru"),
						rowStation.getLocation());
				if (findStation != null) {
					routeModel.getDirectStations()[i].copyFrom(findStation);
				} else {
					routeModel.getDirectStations()[i].setId(ind);
					ind--;
				}
			}

			for (int i = 0; i < routeModel.getReverseStations().length; i++) {
				Station rowStation = routeModel.getReverseStations()[i];
				Station findStation = stations.getStation(
						rowStation.getNameByLanguage("c_ru"),
						rowStation.getLocation());
				if (findStation != null) {
					routeModel.getReverseStations()[i].copyFrom(findStation);
				} else {
					rowStation.setId(ind);
					ind--;
				}
			}
			return routeModel;
		} catch (Exception e) {
			log.error("insertObject() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {

		}
		return null;
	}

	@Override
	public void removeObject(int ID) throws RepositoryException {
		Connection c = super.getConnection();
		try {
			String query = "DELETE FROM bus.import_objects  WHERE id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, ID);
			ps.execute();
			super.commit(c);
		} catch (Exception e) {
			log.error("updateObject() exception: ", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
	}

	@Override
	public Collection<ImportObject> getObjects(String cityKey,
			String routeType, LoadImportObjectOptions opts)
			throws RepositoryException {
		Connection c = super.getConnection();
		Collection<ImportObject> objects = new ArrayList<ImportObject>();
		try {
			String query = null;
			if (opts.isLoadData() == true) {
				query = "SELECT id,route_number,obj FROM bus.import_objects WHERE "
						+ "city_key = ? AND route_type = bus.route_type_enum(?);";
			} else {
				query = "SELECT id,route_number FROM bus.import_objects WHERE "
						+ "city_key = ? AND route_type = bus.route_type_enum(?)";
			}

			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, cityKey);
			ps.setString(2, routeType);
			ResultSet key = ps.executeQuery();

			while (key.next()) {
				ImportObject obj = new ImportObject();
				obj.id = key.getInt("id");
				obj.route_number = key.getString("route_number");
				if (opts.isLoadData() == true) {
					obj.obj = key.getString("obj");
				}
				obj.city_key = cityKey;
				obj.route_type = routeType;

				objects.add(obj);
			}
		} catch (Exception e) {
			log.error("getObject() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return objects;
	}

	@Override
	public ImportObject getObject(int objID) throws RepositoryException {
		Connection c = super.getConnection();
		ImportObject obj = null;
		try {
			String query = "SELECT * FROM bus.import_objects WHERE id = ?";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, objID);
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				obj = new ImportObject();
				obj.id = objID;
				obj.obj = key.getString("obj");
				obj.city_key = key.getString("city_key");
				obj.route_number = key.getString("route_number");
				obj.route_type = key.getString("route_type");
			}
			super.commit(c);
		} catch (Exception e) {
			log.error("getObject() exception: ", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return obj;
	}

	@Override
	public ImportObject getObject(String cityKey, String routeType,
			String number) throws RepositoryException {
		Connection c = super.getConnection();
		ImportObject obj = null;
		try {
			String query = "SELECT * FROM bus.import_objects WHERE "
					+ "city_key = ? AND route_type = bus.route_type_enum(?) AND "
					+ "route_number = ?;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, cityKey);
			ps.setString(2, routeType);
			ps.setString(3, number);
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				obj = new ImportObject();
				obj.id = key.getInt("id");
				obj.obj = key.getString("obj");
				obj.city_key = cityKey;
				obj.route_number = number;
				obj.route_type = routeType;
			}
			super.commit(c);
		} catch (Exception e) {
			log.error("getObject() exception: ", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return obj;
	}

	@Override
	public void updateObjectByID(ImportObject importObject)
			throws RepositoryException {
		Connection c = super.getConnection();
		try {

			String query = "UPDATE bus.import_objects SET obj = ? WHERE id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, importObject.obj);
			ps.setInt(2, importObject.id);
			ps.execute();

			super.commit(c);
		} catch (Exception e) {
			log.error("updateObject() exception: ", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
	}

}
