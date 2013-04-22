package com.pgis.bus.data.repositories.model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.models.JsonRouteObjectModel;
import com.pgis.bus.data.models.JsonRouteObjectsListModel;
import com.pgis.bus.data.repositories.IRepository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.model.IJsonRouteObjectsModelRepository;
import com.pgis.bus.data.service.impl.ServiceConnectionManager;

public class JsonRouteObjectsModelRepository extends ModelRepository implements IJsonRouteObjectsModelRepository,
		IRepository {
	private static final Logger log = LoggerFactory.getLogger(JsonRouteObjectsModelRepository.class);

	public JsonRouteObjectsModelRepository(IConnectionManager connManager) {
		super(connManager);
	}

	public JsonRouteObjectsModelRepository(Locale locale, ServiceConnectionManager connManager) {
		super(locale, connManager);
	}

	public JsonRouteObjectsModelRepository(String langID, ServiceConnectionManager connManager) {
		super(langID, connManager);
	}

	@Override
	public JsonRouteObjectModel get(int id) throws SQLException {

		return null;
	}

	@Override
	public JsonRouteObjectsListModel getmportObjectsList(String cityKey, String routeType) throws SQLException {
		Connection c = super.getConnection();
		JsonRouteObjectsListModel model = new JsonRouteObjectsListModel();
		model.setCityKey(cityKey);
		model.setRouteType(routeType);
		try {
			String query = "SELECT id,route_number FROM bus.import_objects WHERE city_key = ? "
					+ "AND route_type = bus.route_type_enum(?)";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, cityKey);
			ps.setString(2, routeType);
			ResultSet key = ps.executeQuery();

			while (key.next()) {
				int id = key.getInt("id");
				String routeNumber = key.getString("route_number");
				model.addImportRouteObject(id, routeNumber);
			}
		} catch (Exception e) {
			log.error("getObject() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
		return model;
	}
}
