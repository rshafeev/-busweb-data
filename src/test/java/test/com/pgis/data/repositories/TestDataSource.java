package test.com.pgis.data.repositories;

import javax.sql.DataSource;
import org.postgresql.ds.PGPoolingDataSource;

public class TestDataSource {
	private PGPoolingDataSource source = null;
	
	private PGPoolingDataSource createSource(String dataSourceName,String serverName,String dbName,String user,String password)
	{
		if(PGPoolingDataSource.getDataSource(dataSourceName)!=null)
			return PGPoolingDataSource.getDataSource(dataSourceName);
		PGPoolingDataSource source  = new PGPoolingDataSource();
		source.setDataSourceName(dataSourceName);
		source.setDatabaseName(dbName);
		source.setUser(user);
		source.setPassword(password);
		source.setMaxConnections(100);
		source.setServerName(serverName);
		return source;
	}
	private void init()
	{
		source = createSource("jdbc:postgresql","localhost","transportDB","postgres","14R199009postgres");
		
	}
	public TestDataSource()
	{
		init();
	}
	public DataSource getDataSource()
	{
		return (DataSource)source;
	}
	
	public void close()
	{
		if(source!=null)
			source.close();
	}
}
