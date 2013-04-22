package com.pgis.bus.data.repositories.orm;

import java.sql.SQLException;
import java.util.Collection;

import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.repositories.IRepository;

public interface IRoutesRepository extends IRepository {

	Route get(int routeID) throws SQLException;

	RouteWay getRouteWay(int routeID, boolean directType) throws SQLException;

	void insert(Route route) throws SQLException;

	void remove(int routeID) throws SQLException;

	void update(Route route) throws SQLException;

	void update(RouteWay routeWay) throws SQLException;

	void updateNumber(int numberKey, Collection<StringValue> number) throws SQLException;

	Collection<RouteRelation> getRouteWayRelations(int routeWayID) throws SQLException;

}
