package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pgis.bus.data.helpers.LoadImportObjectOptions;
import com.pgis.bus.data.models.ImportRouteModel;
import com.pgis.bus.data.orm.ImportObject;
import com.pgis.bus.data.repositories.IimportRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public class ImportRepository extends Repository implements IimportRepository {

	private static final Logger log = LoggerFactory
			.getLogger(ImportRepository.class);

	public ImportRepository() {
		super();
	}

	public ImportRepository(Connection c, boolean isClosed, boolean isCommited) {
		super();
		this.connection = c;
		this.isClosed = isClosed;
		this.isCommited = isCommited;
	}

	@Override
	public void insertObject(ImportObject obj) throws RepositoryException {
		Connection c = super.getConnection();
		try {
			ImportRepository rep = new ImportRepository(c, false, false);
			ImportObject newObj = rep.getObject(obj.city_id, obj.route_type,
					obj.route_number);
			if (newObj != null) {
				newObj.obj = obj.obj;
				rep.updateObjectByID(newObj);
			} else {
				String query = "INSERT INTO bus.import_objects (city_id,route_type,route_number,obj) "
						+ "VALUES(?,bus.route_type_enum(?),?,?) RETURNING id; ";
				PreparedStatement ps = c.prepareStatement(query);
				ps.setInt(1, obj.city_id);
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
		} finally {
			super.closeConnection(c);
		}
	}

	@Override
	public ImportRouteModel getRouteModelForObj(int objID)
			throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeObject(int ID) throws RepositoryException {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<ImportObject> getObjects(int cityID, String routeType,
			LoadImportObjectOptions opts) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImportObject getObject(int cityID, String routeType, String number)
			throws RepositoryException {
		Connection c = super.getConnection();
		ImportObject obj = null;
		try {
			String query = "SELECT * FROM bus.import_objects WHERE "
					+ "city_id = ? AND route_type = bus.route_type_enum(?) AND "
					+ "route_number = ?;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, cityID);
			ps.setString(2, routeType);
			ps.setString(3, number);
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				obj = new ImportObject();
				obj.id = key.getInt("id");
				obj.obj = key.getString("obj");
				obj.city_id = cityID;
				obj.route_number = number;
				obj.route_type = routeType;
			} 
			super.commit(c);
		} catch (Exception e) {
			log.error("getObject() exception: ", e);
			super.rollback(c);
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
		} finally {
			super.closeConnection(c);
		}
	}

}
