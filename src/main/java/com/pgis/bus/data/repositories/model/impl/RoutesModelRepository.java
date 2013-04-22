package com.pgis.bus.data.repositories.model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.model.IRoutesModelRepository;
import com.pgis.bus.net.models.route.RouteModel;
import com.pgis.bus.net.models.route.RouteRelationModel;
import com.pgis.bus.net.models.route.RouteWayModel;
import com.pgis.bus.net.models.route.RoutesListModel;
import com.pgis.bus.net.models.route.ScheduleModel;

public class RoutesModelRepository extends ModelRepository implements IRoutesModelRepository {

	private static final Logger log = LoggerFactory.getLogger(RoutesModelRepository.class);

	public RoutesModelRepository(Locale locale, IConnectionManager connManager) {
		super(locale, connManager);
	}

	public RoutesModelRepository(String langID, IConnectionManager connManager) {
		super(langID, connManager);
	}

	public RoutesModelRepository(IConnectionManager connManager) {
		super(connManager);
	}

	@Override
	public RouteModel get(int routeID) throws SQLException {
		Connection c = super.getConnection();
		RouteModel route = null;
		try {
			String query = "SELECT * from bus.routes WHERE id = ?; ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routeID);
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				int id = key.getInt("id");
				int city_id = key.getInt("city_id");
				String routeTypeID = key.getString("route_type_id");
				int number_key = key.getInt("number_key");
				double cost = key.getDouble("cost");
				route = new RouteModel();
				route.setId(id);
				route.setCost(cost);
				route.setCityID(city_id);
				route.setRouteTypeID(routeTypeID);
			}

		} catch (Exception e) {
			route = null;
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
		return route;
	}

	@Override
	public Collection<RouteRelationModel> getRouteRelations(int routeID, boolean directType, String langID)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<RouteRelationModel> getRouteRelations(int routeID, boolean directType, String langID,
			int startInd, int finishInd) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduleModel getSchedule(int routeWayID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RouteWayModel getRouteWay(int routeID, boolean directType) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoutesListModel getRoutesList(int cityID, String routeTypeID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
