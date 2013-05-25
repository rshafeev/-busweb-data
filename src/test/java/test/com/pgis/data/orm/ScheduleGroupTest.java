package test.com.pgis.data.orm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.pgis.bus.data.helpers.PGIntervalHelper;
import com.pgis.bus.data.helpers.TimeHelper;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.data.orm.ScheduleGroupDay;
import com.pgis.bus.data.orm.Timetable;
import com.pgis.bus.data.orm.type.DayEnum;
import com.pgis.bus.net.models.route.schedule.ScheduleGroupModel;

public class ScheduleGroupTest {

	ScheduleGroup grp = null;

	@Before
	public void init() {
		grp = new ScheduleGroup();
		grp.setId(1);
		grp.setScheduleID(100);

		Timetable t1 = new Timetable();
		t1.setId(1);
		t1.setTimeA(TimeHelper.fromSeconds(3660));
		t1.setTimeB(TimeHelper.fromSeconds(7200));
		t1.setFrequency(PGIntervalHelper.fromSeconds(400));
		t1.setScheduleGroupID(100);
		grp.addTimetable(t1);

		Timetable t2 = new Timetable();
		t2.setId(1);
		t2.setTimeA(TimeHelper.fromSeconds(3660));
		t2.setTimeB(TimeHelper.fromSeconds(7200));
		t2.setFrequency(PGIntervalHelper.fromSeconds(400));
		t2.setScheduleGroupID(100);
		grp.addTimetable(t2);

		ScheduleGroupDay d1 = new ScheduleGroupDay();
		d1.setId(1);
		d1.setScheduleGroupID(1);
		d1.setDayID(DayEnum.c_Saturday);
		grp.addDay(d1);

		ScheduleGroupDay d2 = new ScheduleGroupDay();
		d2.setId(2);
		d2.setScheduleGroupID(1);
		d2.setDayID(DayEnum.c_Sunday);
		grp.addDay(d2);

	}

	@Test
	public void initTest() {
		ScheduleGroup grp1 = new ScheduleGroup();
		grp1.setId(1);
		grp1.setScheduleID(100);

		assertEquals(1, grp1.getId());
		assertEquals(100, grp1.getScheduleID());

	}

	@Test
	public void initFromModelTest() {
		ScheduleGroupModel model = grp.toModel();

		ScheduleGroup grp1 = new ScheduleGroup(model, grp.getScheduleID());

		assertEquals(grp.getId(), grp1.getId());
		assertEquals(grp.getScheduleID(), grp1.getScheduleID());
		assertEquals(grp.getDays().size(), grp1.getDays().size());
		assertEquals(grp.getTimetables().size(), grp1.getTimetables().size());
	}

	@Test
	public void toModelTest() {
		ScheduleGroupModel model = grp.toModel();

		assertEquals(grp.getId(), model.getId());
		assertEquals(grp.getDays().size(), model.getDays().size());
		assertEquals(grp.getTimetables().size(), model.getTimetable().size());
	}
	
	@Test
	public void cloneTest() throws CloneNotSupportedException
	{
		grp = new ScheduleGroup();
		grp.setId(1);
		grp.setScheduleID(100);

		Timetable t1 = new Timetable();
		t1.setId(1);
		t1.setTimeA(TimeHelper.fromSeconds(3660));
		t1.setTimeB(TimeHelper.fromSeconds(7200));
		t1.setFrequency(PGIntervalHelper.fromSeconds(400));
		t1.setScheduleGroupID(100);
		grp.addTimetable(t1);

		ScheduleGroupDay d1 = new ScheduleGroupDay();
		d1.setId(1);
		d1.setScheduleGroupID(1);
		d1.setDayID(DayEnum.c_Saturday);
		grp.addDay(d1);
		
		ScheduleGroup grp_clone = grp.clone();
		
		grp.setId(2);
		grp.setScheduleID(200);

		Timetable t2 = new Timetable();
		t2.setId(2);
		t2.setTimeA(TimeHelper.fromSeconds(3620));
		t2.setTimeB(TimeHelper.fromSeconds(7400));
		t2.setFrequency(PGIntervalHelper.fromSeconds(500));
		t2.setScheduleGroupID(140);
		grp.addTimetable(t2);

		ScheduleGroupDay d2 = new ScheduleGroupDay();
		d2.setId(7);
		d2.setScheduleGroupID(2);
		d2.setDayID(DayEnum.c_Sunday);
		grp.addDay(d2);
		
		assertEquals(1,grp_clone.getId());
		assertEquals(100,grp_clone.getScheduleID());
		assertEquals(1,grp_clone.getTimetables().size());
		assertEquals(1,grp_clone.getDays().size());

	}
	
	@Test
	public void cloneTest2() throws CloneNotSupportedException
	{
		ScheduleGroup scheduleGroup = new ScheduleGroup();
		ScheduleGroup scheduleGroup_clone = scheduleGroup.clone();
		assertNotNull (scheduleGroup_clone);
	}

}
