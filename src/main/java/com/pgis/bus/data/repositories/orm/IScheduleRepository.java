package com.pgis.bus.data.repositories.orm;

import java.sql.SQLException;
import java.util.Collection;

import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.data.orm.ScheduleGroupDay;
import com.pgis.bus.data.orm.Timetable;
import com.pgis.bus.data.repositories.IRepository;

public interface IScheduleRepository extends IRepository {

	Schedule get(int id) throws SQLException;

	Schedule getByRouteWay(int routeWayID) throws SQLException;

	void insert(Schedule s) throws SQLException;

	void update(Schedule s) throws SQLException;

	void remove(int id) throws SQLException;

	void removeByRouteWay(int routeWayID) throws SQLException;

	Collection<ScheduleGroup> getScheduleGroups(int scheduleID) throws SQLException;

	Collection<ScheduleGroupDay> getScheduleGroupDays(int schedule_group_id) throws SQLException;

	Collection<Timetable> getTimeTables(int scheduleGroupId) throws SQLException;
}
