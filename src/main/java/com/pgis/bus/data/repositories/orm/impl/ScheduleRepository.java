package com.pgis.bus.data.repositories.orm.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

import org.postgresql.util.PGInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.data.orm.ScheduleGroupDay;
import com.pgis.bus.data.orm.Timetable;
import com.pgis.bus.data.orm.type.DayEnum;
import com.pgis.bus.data.repositories.Repository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.IScheduleRepository;

public class ScheduleRepository extends Repository implements IScheduleRepository {
	private static final Logger log = LoggerFactory.getLogger(ScheduleRepository.class);

	public ScheduleRepository(IConnectionManager connManager) {
		super(connManager);
	}

	@Override
	public Schedule get(int id) throws SQLException {
		Connection c = super.getConnection();
		Schedule schedule = null;
		try {

			String query = "SELECT * from bus.schedule WHERE id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				int routeWayID = key.getInt("rway_id");
				schedule = new Schedule(this.connManager);
				schedule.setRouteWayId(routeWayID);
				schedule.setId(id);
				schedule.setScheduleGroups(this.getScheduleGroups(schedule.getId()));
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
		return schedule;
	}

	@Override
	public Schedule getByRouteWay(int routeWayID) throws SQLException {
		Connection c = super.getConnection();
		Schedule schedule = null;

		try {

			String query = "SELECT * from bus.schedule WHERE rway_id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routeWayID);
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				int id = key.getInt("id");
				schedule = new Schedule(this.connManager);
				schedule.setRouteWayId(routeWayID);
				schedule.setId(id);
				schedule.setScheduleGroups(this.getScheduleGroups(schedule.getId()));
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
		return schedule;
	}

	@Override
	public Collection<Timetable> getTimeTables(int scheduleGroupId) throws SQLException {
		Connection c = super.getConnection();
		Collection<Timetable> timeTables = null;

		try {
			String query = "SELECT * from bus.timetable WHERE schedule_group_id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, scheduleGroupId);
			ResultSet key = ps.executeQuery();
			timeTables = new ArrayList<Timetable>();
			while (key.next()) {
				int id = key.getInt("id");

				Time time_A = (Time) key.getObject("time_A");
				Time time_B = (Time) key.getObject("time_B");
				PGInterval frequency = (PGInterval) key.getObject("frequency");
				Timetable timetable = new Timetable();
				timetable.setId(id);
				timetable.setScheduleGroupID(scheduleGroupId);
				timetable.setTimeA(time_A);
				timetable.setTimeB(time_B);
				timetable.setFrequency(frequency);
				timeTables.add(timetable);
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
		return timeTables;
	}

	@Override
	public Collection<ScheduleGroupDay> getScheduleGroupDays(int schedule_group_id) throws SQLException {
		Connection c = super.getConnection();
		Collection<ScheduleGroupDay> days = null;

		try {

			String query = "SELECT * FROM bus.schedule_group_days WHERE schedule_group_id = ?;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, schedule_group_id);
			ResultSet key = ps.executeQuery();
			days = new ArrayList<ScheduleGroupDay>();
			while (key.next()) {
				int id = key.getInt("id");
				DayEnum day_id = DayEnum.valueOf(key.getString("day_id"));
				ScheduleGroupDay day = new ScheduleGroupDay();
				day.setId(id);
				day.setScheduleGroupID(schedule_group_id);
				day.setDayID(day_id);
				days.add(day);
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
		return days;
	}

	@Override
	public Collection<ScheduleGroup> getScheduleGroups(int scheduleID) throws SQLException {
		Connection c = super.getConnection();
		Collection<ScheduleGroup> groups = null;

		try {

			String query = "SELECT * from bus.schedule_groups WHERE schedule_id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, scheduleID);
			ResultSet key = ps.executeQuery();
			groups = new ArrayList<ScheduleGroup>();
			while (key.next()) {
				int id = key.getInt("id");
				ScheduleGroup group = new ScheduleGroup();
				group.setId(id);
				group.setScheduleID(scheduleID);
				group.setDays(getScheduleGroupDays(id));
				group.setTimetables(getTimeTables(id));
				groups.add(group);
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
		return groups;
	}

	public void insert(Schedule schedule) throws SQLException {
		Connection c = super.getConnection();
		String query = "INSERT INTO bus.schedule (rway_id) " + "VALUES(?) RETURNING id;";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, schedule.getRouteWayId());
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt("id");
			schedule.setId(id);

		} else {

			throw new RepositoryException(RepositoryException.err_enum.c_id_notFind);
		}
		for (ScheduleGroup g : schedule.getScheduleGroups()) {
			this.insertScheduleGroup(g);

		}
	}

	private void insertScheduleGroup(ScheduleGroup scheduleGroup) throws SQLException {
		Connection c = super.getConnection();
		String query = "INSERT INTO bus.schedule_groups (schedule_id) " + "VALUES(?) RETURNING id;";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, scheduleGroup.getScheduleID());
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt("id");
			scheduleGroup.setId(id);

		} else {
			throw new SQLException("not found new id and name_key");
		}
		for (Timetable t : scheduleGroup.getTimetables()) {
			insertTimetable(t);
		}
		for (ScheduleGroupDay d : scheduleGroup.getDays()) {
			insertScheduleGroupDay(d);
		}
	}

	private void insertScheduleGroupDay(ScheduleGroupDay d) throws SQLException {
		Connection c = super.getConnection();
		String query = "INSERT INTO bus.schedule_group_days (schedule_group_id,day_id) "
				+ "VALUES(?,bus.day_enum(?)) RETURNING id;";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, d.getScheduleGroupID());
		ps.setString(2, d.getDayID().name());
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt("id");
			d.setId(id);

		} else {
			throw new RepositoryException("not found new id and name_key");
		}
	}

	private void insertTimetable(Timetable t) throws SQLException {
		Connection c = super.getConnection();
		String query = "INSERT INTO bus.timetable (schedule_group_id,time_A,time_B,frequency) "
				+ "VALUES(?,?,?,?) RETURNING id;";
		PreparedStatement ps = c.prepareStatement(query);

		ps.setInt(1, t.getScheduleGroupID());
		ps.setTime(2, t.getTimeA());
		ps.setTime(3, t.getTimeB());
		ps.setObject(4, t.getFrequency());
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt("id");
			t.setId(id);
		} else {
			throw new RepositoryException(RepositoryException.err_enum.c_id_notFind);
		}
	}

	@Override
	public void update(Schedule s) throws SQLException {

		if (s == null)
			return;
		try {
			s.setConnManager(null);
			this.remove(s.getId());
			this.insert(s);
		} finally {
			s.setConnManager(this.connManager);
		}
	}

	@Override
	public void remove(int id) throws SQLException {
		Connection c = super.getConnection();
		String query = "DELETE FROM bus.schedule WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, id);
		ps.execute();
	}

	@Override
	public void removeByRouteWay(int routeWayID) throws SQLException {
		Connection c = super.getConnection();
		String query = "DELETE FROM bus.schedule WHERE rway_id = ?";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, routeWayID);
		ps.execute();
	}

}
