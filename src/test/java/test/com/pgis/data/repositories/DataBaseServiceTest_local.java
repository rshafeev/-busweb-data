package test.com.pgis.data.repositories;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import org.junit.Test;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.orm.ICitiesRepository;
import com.pgis.bus.data.service.IDataBaseService;
import com.pgis.bus.data.service.impl.DataBaseService;

public class DataBaseServiceTest_local {

	@Test(expected = RepositoryException.class)
	public void disposeTest() throws Exception {
		System.out.println("disposeTest()");
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		try {
			IDataBaseService db = null;
			ICitiesRepository rep = null;
			City city = null;
			try {
				db = new DataBaseService(dbConnMngr);
				// Получим город.
				rep = db.Cities();
				city = rep.getByKey("kharkiv");
				System.out.println("city:" + city);
				assertNotNull(city);
			} finally {
				db.rollback();
				db.dispose();
			}
			// Работать с ORM объектом можно даже после освобождения ресурсов сервиса db
			HashMap<LangEnum, StringValue> name = city.getName();
			assertNotNull(name);
			System.out.println("city:" + city);
			// Закончили работу с серсисом db, однако продолжим работу с его репозиторием.
			// В таком случае при любом действиии с репозиторием, который был порожден
			// данным сервисом, быдет выброшено исключение TransactConnectionException.
			rep.getAll();
		} finally {
			dbConnMngr.dispose();
		}

	}
}
