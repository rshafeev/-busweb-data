package com.pgis.bus.data.repositories.orm.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.orm.SerializedRouteObject;
import com.pgis.bus.data.repositories.orm.ISerializedRoutesRepository;

public class SerializedRoutesRepository extends Repository implements ISerializedRoutesRepository {
	private static final Logger log = LoggerFactory.getLogger(SerializedRoutesRepository.class);

	public SerializedRoutesRepository(IConnectionManager connManager) {
		super(connManager);
	}

	@Override
	public SerializedRouteObject get(String cityKey, String routeType, String number) throws RepositoryException {

		SerializedRouteObject obj = null;
		try {
			Connection c = super.getConnection();
			String query = "SELECT * FROM bus.import_objects WHERE "
					+ "city_key = ? AND route_type = bus.route_type_enum(?) AND " + "route_number = ?;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, cityKey);
			ps.setString(2, routeType);
			ps.setString(3, number);
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				obj = new SerializedRouteObject();
				obj.id = key.getInt("id");
				obj.obj = key.getString("obj");
				obj.city_key = cityKey;
				obj.route_number = number;
				obj.route_type = routeType;
			}
		} catch (Exception e) {
			super.handeThrowble(e);
		}
		return obj;
	}

	@Override
	public SerializedRouteObject get(int objID) throws RepositoryException {

		SerializedRouteObject obj = null;
		try {
			Connection c = super.getConnection();
			String query = "SELECT * FROM bus.import_objects WHERE id = ?";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, objID);
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				obj = new SerializedRouteObject();
				obj.id = objID;
				obj.obj = key.getString("obj");
				obj.city_key = key.getString("city_key");
				obj.route_number = key.getString("route_number");
				obj.route_type = key.getString("route_type");
			}
		} catch (Exception e) {
			super.handeThrowble(e);
		}
		return obj;
	}

	@Override
	public void updateByID(SerializedRouteObject importObject) throws RepositoryException {

		try {
			Connection c = super.getConnection();
			String query = "UPDATE bus.import_objects SET obj = ? WHERE id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, importObject.obj);
			ps.setInt(2, importObject.id);
			ps.execute();

		} catch (Exception e) {
			super.handeThrowble(e);
		}
	}

	@Override
	public void insert(SerializedRouteObject importObject) throws RepositoryException {
		// validation
		if (importObject == null || importObject.obj == null)
			throw new RepositoryException(RepositoryException.err_enum.orm_obj_invalid);

		try {
			Connection c = super.getConnection();
			SerializedRouteObject newObj = this.get(importObject.city_key, importObject.route_type,
					importObject.route_number);
			if (newObj != null) {
				newObj.obj = importObject.obj;
				this.updateByID(newObj);
			} else {
				String query = "INSERT INTO bus.import_objects (city_key,route_type,route_number,obj) "
						+ "VALUES(?,bus.route_type_enum(?),?,?) RETURNING id; ";
				PreparedStatement ps = c.prepareStatement(query);
				ps.setString(1, importObject.city_key);
				ps.setString(2, importObject.route_type);
				ps.setString(3, importObject.route_number);
				ps.setString(4, importObject.obj);
				ResultSet key = ps.executeQuery();

				if (key.next()) {
					int id = key.getInt(1);
					importObject.id = id;
				} else {
					throw new SQLException("not found new id of ImportObject");
				}
			}
		} catch (Exception e) {
			super.handeThrowble(e);
		}
	}

	@Override
	public void remove(int ID) throws RepositoryException {

		try {
			Connection c = super.getConnection();
			String query = "DELETE FROM bus.import_objects  WHERE id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, ID);
			ps.execute();
		} catch (Exception e) {
			super.handeThrowble(e);
		}
	}

}
