package rewards.testdb;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public class TestDataSourceFactory {
	
	static class MapBasedDatabasePopulator extends ResourceDatabasePopulator {
		public void setProperties(Map<String, String> properties) {
			String schemaLocation, dataLocation;
			if ((schemaLocation = properties.get("schemaLocation")) == null) {
				throw new RuntimeException("Cannot find value for schemaLocation key in properties");
			}
			if ((dataLocation = properties.get("testDataLocation")) == null) {
				throw new RuntimeException("Cannot find value for testDataLocation key in properties");
			}
			DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
			super.setScripts(new Resource[]{
					resourceLoader.getResource(schemaLocation),
					resourceLoader.getResource(dataLocation)
			});
		}
	}
	
	private static DataSource createDataSource(String databaseName, DatabasePopulator databasePopulator) {
		EmbeddedDatabaseFactory databaseFactory = new EmbeddedDatabaseFactory();
		databaseFactory.setDatabaseName(databaseName);
		databaseFactory.setDatabasePopulator(databasePopulator);
		return databaseFactory.getDatabase();
	}
	
	private static String getDatabaseName(Map<String, String> properties) {
		String databaseName = properties.get("testDatabaseName");
		if (databaseName == null) {
			throw new RuntimeException("Cannot find value for testDatabaseName key in properties");
		}
		return databaseName;
	}

	public static DataSource createDataSourceFromMap(Map<String, String> properties) {
		MapBasedDatabasePopulator databasePopulator = new MapBasedDatabasePopulator();
		databasePopulator.setProperties(properties);
		return createDataSource(getDatabaseName(properties), databasePopulator);
	}

	public static DataSource createDataSourceFromProperties(Properties properties) {
		MapBasedDatabasePopulator databasePopulator = new MapBasedDatabasePopulator();
		Map<String, String> propertiesMap = new HashMap<String, String>((Map)properties);
		databasePopulator.setProperties(propertiesMap);
		return createDataSource(getDatabaseName(propertiesMap), databasePopulator);
	}
}
