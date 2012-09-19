package rewards.testdb.config;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * A test that verifies each factory bean in alternate-datasource-config.xml creates a data source successfully. To
 * perform the verification, this class simply looks up the data source bean then tries to acquire a connection against
 * it.
 * 
 * This test is designed for the lab only, and is not very useful in the real application.
 */
public class AlternateDataSourceConfigTests {

	private ApplicationContext applicationContext;
	
	@Before
	public void setUp() throws Exception {
		// create the bean container
		applicationContext = new ClassPathXmlApplicationContext(
				"classpath:/rewards/testdb/config/alternate-datasource-config.xml");
		// note: all bean definitions in that file are marked lazy so objects won't be created until looked up
	}

	@Test public void dataSourceBuiltFromConstants() throws Exception {
		DataSource dataSource = applicationContext.getBean("dataSource-createdFromConstants", DataSource.class);
		getConnection(dataSource);
	}

	@Test public void dataSourceBuiltFromMapFactoryMethod() throws Exception {
		DataSource dataSource = applicationContext.getBean("dataSource-createdFromMap", DataSource.class);
		getConnection(dataSource);
	}

	@Test public void dataSourceBuiltFromListFactoryMethod() throws Exception {
		DataSource dataSource = applicationContext.getBean("dataSource-createdFromList", DataSource.class);
		getConnection(dataSource);
	}

	@Test public void dataSourceBuiltFromPropertiesFactoryMethod() throws Exception {
		DataSource dataSource = applicationContext.getBean("dataSource-createdFromProperties", DataSource.class);
		getConnection(dataSource);
	}

	private void getConnection(DataSource dataSource) throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		assertEquals(1, jdbcTemplate.queryForInt("SELECT COUNT(*) FROM T_RESTAURANT"));
	}
}