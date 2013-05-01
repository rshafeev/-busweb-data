package test.com.pgis.data.orm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.pgis.bus.data.helpers.PGIntervalHelper;
import com.pgis.bus.data.helpers.TimeHelper;
import com.pgis.bus.data.orm.Timetable;
import com.pgis.bus.net.models.route.schedule.TimetableModel;

public class TimetableTest {

	@Test
	public void initTest() {
		Timetable t = new Timetable();
		t.setId(1);
		t.setTimeA(TimeHelper.fromSeconds(3660));
		t.setTimeB(TimeHelper.fromSeconds(7200));
		t.setFrequency(PGIntervalHelper.fromSeconds(400));
		t.setScheduleGroupID(100);

		assertEquals(1, t.getId());
		assertEquals(100, t.getScheduleGroupID());
		assertEquals(3660, TimeHelper.toSeconds(t.getTimeA()));
		assertEquals(7200, TimeHelper.toSeconds(t.getTimeB()));
		assertEquals(400, PGIntervalHelper.toSeconds(t.getFrequency()));

	}

	@Test
	public void toModelTest() {
		Timetable t = new Timetable();
		t.setId(1);
		t.setTimeA(TimeHelper.fromSeconds(3660));
		t.setTimeB(TimeHelper.fromSeconds(7200));
		t.setFrequency(PGIntervalHelper.fromSeconds(400));
		t.setScheduleGroupID(100);

		TimetableModel model = t.toModel();

		assertEquals(3660, model.getTimeA());
		assertEquals(7200, model.getTimeB());
		assertEquals(400, model.getFreq());

	}

}
