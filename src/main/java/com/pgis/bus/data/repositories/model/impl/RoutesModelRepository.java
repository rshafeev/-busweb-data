package com.pgis.bus.data.repositories.model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.model.IRoutesModelRepository;
import com.pgis.bus.net.models.route.RouteModel;
import com.pgis.bus.net.models.route.RouteRelationModel;
import com.pgis.bus.net.models.route.RouteWayModel;
import com.pgis.bus.net.models.route.RoutesListModel;
import com.pgis.bus.net.models.route.ScheduleModel;

public class RoutesModelRepository extends ModelRepository implements IRoutesModelRepository {

	private static final Logger log = LoggerFactory.getLogger(RoutesModelRepository.class);

	public RoutesModelRepository(Locale locale, IDBConnectionManager connManager) {
		super(locale, connManager);
	}

	public RoutesModelRepository(Locale locale, IDBConnectionManager connManager, boolean isCommited) {
		super(locale, connManager, isCommited);
	}

	public RoutesModelRepository(String langID, IDBConnectionManager connManager) {
		super(langID, connManager);
	}

	public RoutesModelRepository(String langID, IDBConnectionManager connManager, boolean isCommited) {
		super(langID, connManager, isCommited);
	}

	public RoutesModelRepository(IDBConnectionManager connManager) {
		super(connManager);
	}

	public RoutesModelRepository(IDBConnectionManager connManager, boolean isCommited) {
		super(connManager, isCommited);
	}

	protected RoutesModelRepository(String langID, IDBConnectionManager connManager, Connection c, boolean isClosed,
			boolean isCommited) {
		super(langID, connManager, isCommited);
		super.isClosed = isClosed;
		super.connection = c;
	}

	@Override
	public RouteModel get(int routeID) throws RepositoryException {
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
		} finally {
			super.closeConnection(c);
		}
		return route;
	}

	@Override
	public RoutesListModel getRoutesList(int cityID, String routeTypeID, String langID) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<RouteRelationModel> getRouteRelations(int routeID, boolean directType, String langID)
			throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<RouteRelationModel> getRouteRelations(int routeID, boolean directType, String langID,
			int startInd, int finishInd) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduleModel getSchedule(int routeWayID) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RouteWayModel getRouteWay(int routeID, boolean directType) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

}
