package test.com.pgis.data.repositories;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.Authenticate_enum;
import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.repositories.impl.UsersRepository;

//import com.pgis.bus.data.repositories.UsersRepository;
public class UsersRepositoryTest_local {

	IDBConnectionManager dbConnectionManager = null;

	@Before
	public void init() {
		dbConnectionManager = TestDBConnectionManager.create();

	}

	@Test
	public void test() throws Exception {
		assertFalse(false);
	}

	@Test
	public void authenticateTest() throws Exception {
		UsersRepository users = new UsersRepository(dbConnectionManager);
		Authenticate_enum result = users.authenticate("admin", "roma",
				"14R199009");

	}
}
