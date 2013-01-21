package test.com.pgis.data;

import com.pgis.bus.data.IDBConnectionManager;

public class DBTestConnectionFactory {

	private static TestDBConnectionManager dbConnectionManager = null;

	public static synchronized IDBConnectionManager getTestDBConnectionManager() {
		if (dbConnectionManager == null) {
			TestDataSource source = new TestDataSource();
			dbConnectionManager = new TestDBConnectionManager(
					source.getDataSource());
		}
		return dbConnectionManager;
	}
}
