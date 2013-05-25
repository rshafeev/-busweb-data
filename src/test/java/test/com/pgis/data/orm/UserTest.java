package test.com.pgis.data.orm;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pgis.bus.data.orm.Language;
import com.pgis.bus.data.orm.User;

public class UserTest {

	@Test
	public void cloneTest() throws CloneNotSupportedException {
		User user = new User();
		user.setId(5);
		user.setName("Marianna");
		user.setRoleID(560);
		
		User user_clone = user.clone();
		
		user.setId(7);
		user.setName("Roma");
		user.setRoleID(452);
		
		assertEquals(5, (int)(user_clone.getId()));
		assertEquals("Marianna", user_clone.getName());
		assertEquals(560, user_clone.getRoleID());	
	}
	
	@Test
	public void cloneTest2() throws CloneNotSupportedException
	{
		User user = new User();
		User user_clone = user.clone();
		assertNotNull (user_clone);
	}

}
