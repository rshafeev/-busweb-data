package com.pgis.bus.data.repositories.orm;

import java.sql.SQLException;
import java.util.Collection;

import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.IRepository;

public interface IRoutesRepository extends IRepository {

	Route get(int routeID) throws RepositoryException;

	/**
	 * Возвращает список маршрутов с загруженным номером для заданного языка
	 * 
	 * @param langID Язык
	 * @return Список маршрутов
	 * @throws SQLException
	 */
	Collection<Route> getAll(LangEnum langID, int cityID, String routeTypeID) throws RepositoryException;

	RouteWay getRouteWay(int routeID, boolean directType) throws RepositoryException;

	boolean isExist(int cityID, String routeTypeID, StringValue number) throws RepositoryException;

	void insert(Route route) throws RepositoryException;

	void remove(int routeID) throws RepositoryException;

	void update(Route route) throws RepositoryException;

	void update(RouteWay routeWay) throws RepositoryException;

	void updateNumber(Integer numberKey, Collection<StringValue> number) throws RepositoryException;

	Collection<RouteRelation> getRouteWayRelations(int routeWayID) throws RepositoryException;

}
