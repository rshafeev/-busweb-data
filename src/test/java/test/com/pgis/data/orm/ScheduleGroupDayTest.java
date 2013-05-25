package test.com.pgis.data.orm;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroupDay;
import com.pgis.bus.data.orm.type.DayEnum;

public class ScheduleGroupDayTest {

	@Test
	public void cloneTest() throws CloneNotSupportedException {
		ScheduleGroupDay sgd = new ScheduleGroupDay();
		sgd.setId(56);
		sgd.setScheduleGroupID(87);
		sgd.setDayID(DayEnum.c_Monday);
		
		ScheduleGroupDay sgd_clone = sgd.clone();
		
		sgd.setId(89);
		sgd.setScheduleGroupID(74);
		sgd.setDayID(DayEnum.c_Sunday);
		
		assertEquals(56, sgd_clone.getId());
		assertEquals(87,sgd_clone.getScheduleGroupID() );
		assertEquals(DayEnum.c_Monday,sgd_clone.getDayID() );
		
	}
	
	@Test
	public void cloneTest2() throws CloneNotSupportedException
	{
		ScheduleGroupDay sgd = new ScheduleGroupDay();
		ScheduleGroupDay sgd_clone = sgd.clone();
		assertNotNull (sgd_clone);
	}

}
