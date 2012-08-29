package test.com.pgis.data.repositories;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.com.pgis.data.TestDBConnectionManager;
import test.com.pgis.data.TestDataSource;

import com.pgis.bus.data.Authenticate_enum;
import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.repositories.impl.UsersRepository;

//import com.pgis.bus.data.repositories.UsersRepository;
public class UsersRepositoryTest_local {
	@Before
	public void init() {
		TestDataSource source = new TestDataSource();
		TestDBConnectionManager dbConnectionManager = new TestDBConnectionManager(source.getDataSource());
		DBConnectionFactory.init(dbConnectionManager);
		System.out.print("init test\n");
	}

	@After
	public void destroy() {
		DBConnectionFactory.free();
	}

	@Test
	public void test() throws Exception {
		assertFalse(false);
	}

	@Test
	public void authenticateTest() throws Exception {
		UsersRepository users = new UsersRepository();
		Authenticate_enum result =  users.authenticate("admin","roma", "14R199009");
		
	}
}
