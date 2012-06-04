package test.com.pgis.data.repositories;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.com.pgis.data.repositories.TestDataSource;

import com.pgis.bus.data.Authenticate_enum;
import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.repositories.UsersRepository;

//import com.pgis.bus.data.repositories.UsersRepository;
public class UsersRepositoryTest_local {

	TestDataSource source = null;

	@Before
	public void init() {
		source = new TestDataSource();
		DBConnectionFactory.init(source.getDataSource());
		System.out.print("init test\n");
	}

	@After
	public void destroy() {
		if (source != null) {
			source.close();
			source = null;
		}
	}

	@Test
	public void test() throws Exception {
		assertFalse(false);
	}

	@Test
	public void authenticateTest() throws Exception {
		UsersRepository users = new UsersRepository();
		Authenticate_enum result =  users.authenticate("admin","roma", "rar");
		
	}
}
