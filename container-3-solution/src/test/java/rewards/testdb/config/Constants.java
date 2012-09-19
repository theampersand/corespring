package rewards.testdb.config;

/**
 * Exposes test data source property values as static constants.
 */
public final class Constants {

	/**
	 * The name of the test database.
	 */
	public static final String TEST_DATABASE_NAME = "rewards";

	/**
	 * The path to the file containing the database schema.
	 */
	public static final String SCHEMA_LOCATION = "classpath:rewards/testdb/schema.sql";

	/**
	 * The path to the file containing the database test data.
	 */
	public static final String TEST_DATA_LOCATION = "classpath:rewards/testdb/test-data.sql";

	private Constants() {

	}
}
