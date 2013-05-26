package com.pgis.bus.data.repositories.orm;

import java.sql.SQLException;
import java.util.Collection;

import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.data.orm.ScheduleGroupDay;
import com.pgis.bus.data.orm.Timetable;
import com.pgis.bus.data.repositories.IRepository;

public interface IScheduleRepository extends IRepository {

	Schedule get(int id) throws RepositoryException;

	Schedule getByRouteWay(int routeWayID) throws RepositoryException;

	void insert(Schedule s) throws RepositoryException;

	void update(Schedule s) throws RepositoryException;

	void remove(int id) throws RepositoryException;

	void removeByRouteWay(int routeWayID) throws RepositoryException;

	Collection<ScheduleGroup> getScheduleGroups(int scheduleID) throws RepositoryException;

	Collection<ScheduleGroupDay> getScheduleGroupDays(int schedule_group_id) throws RepositoryException;

	Collection<Timetable> getTimeTables(int scheduleGroupId) throws RepositoryException;
}
