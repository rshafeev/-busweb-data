package test.com.pgis.data.orm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.net.models.route.ScheduleModel;

public class ScheduleTest {

	@Test
	public void initTest() {
		// test
		Schedule schedule = new Schedule();
		schedule.setId(10);
		schedule.setRouteWayId(100);

		ScheduleGroup grp1 = new ScheduleGroup();
		grp1.setId(1);
		grp1.setScheduleID(100);
		schedule.addScheduleGroup(grp1);

		ScheduleGroup grp2 = new ScheduleGroup();
		grp2.setId(1);
		grp2.setScheduleID(100);
		schedule.addScheduleGroup(grp2);

		assertEquals(10, schedule.getId());
		assertEquals(100, schedule.getRouteWayId());
		assertEquals(2, schedule.getScheduleGroups().size());
	}

	@Test
	public void initFromModelTest() {
		// prepare data
		Schedule schedule = new Schedule();
		schedule.setId(10);
		schedule.setRouteWayId(100);

		ScheduleGroup grp1 = new ScheduleGroup();
		grp1.setId(1);
		grp1.setScheduleID(100);
		schedule.addScheduleGroup(grp1);

		ScheduleGroup grp2 = new ScheduleGroup();
		grp2.setId(1);
		grp2.setScheduleID(100);
		schedule.addScheduleGroup(grp2);

		ScheduleModel model = schedule.toModel();

		// test
		Schedule schedule2 = new Schedule(model);
		assertEquals(10, schedule2.getId());
		assertEquals(100, schedule2.getRouteWayId());
		assertEquals(2, schedule2.getScheduleGroups().size());
	}

	@Test
	public void toModelTest() {
		// prepare data
		Schedule schedule = new Schedule();
		schedule.setId(10);
		schedule.setRouteWayId(100);

		ScheduleGroup grp1 = new ScheduleGroup();
		grp1.setId(1);
		grp1.setScheduleID(100);
		schedule.addScheduleGroup(grp1);

		ScheduleGroup grp2 = new ScheduleGroup();
		grp2.setId(1);
		grp2.setScheduleID(100);
		schedule.addScheduleGroup(grp2);

		// test
		ScheduleModel model = schedule.toModel();
		assertEquals(schedule.getId(), model.getId());
		assertEquals(schedule.getRouteWayId(), model.getRouteWayID());
		assertEquals(schedule.getScheduleGroups().size(), model.getGroups().size());

	}

}
