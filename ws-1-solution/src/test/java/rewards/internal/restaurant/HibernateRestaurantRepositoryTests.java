package rewards.internal.restaurant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import common.money.Percentage;

public class HibernateRestaurantRepositoryTests {

	private HibernateRestaurantRepository repository;

	private PlatformTransactionManager transactionManager;

	private TransactionStatus status;

	@Before
	public void setUp() throws Exception {
		repository = new HibernateRestaurantRepository();
		SessionFactory sessionFactory = createTestSessionFactory();
		repository.setSessionFactory(sessionFactory);
		transactionManager = new HibernateTransactionManager(sessionFactory);
		status = transactionManager.getTransaction(new DefaultTransactionDefinition());
	}

	@After
	public void tearDown() throws Exception {
		transactionManager.rollback(status);
	}

	@Test
	public void testFindRestaurantByMerchantNumber() {
		Restaurant restaurant = repository.findByMerchantNumber("1234567890");
		assertNotNull("the restaurant should never be null", restaurant);
		assertEquals("the merchant number is wrong", "1234567890", restaurant.getNumber());
		assertEquals("the name is wrong", "AppleBees", restaurant.getName());
		assertEquals("the benefitPercentage is wrong", Percentage.valueOf("8%"), restaurant.getBenefitPercentage());
		assertEquals("the benefit availability policy is wrong", AlwaysAvailable.INSTANCE, restaurant
				.getBenefitAvailabilityPolicy());
	}

	private SessionFactory createTestSessionFactory() throws Exception {
		// simulate the Spring bean initialization lifecycle
		AnnotationSessionFactoryBean factoryBean = new AnnotationSessionFactoryBean();
		factoryBean.setDataSource(createTestDataSource());
		Class[] annotatedClasses = new Class[] { Restaurant.class };
		factoryBean.setAnnotatedClasses(annotatedClasses);
		factoryBean.afterPropertiesSet();
		return (SessionFactory) factoryBean.getObject();
	}

	private DataSource createTestDataSource() {
		return new EmbeddedDatabaseBuilder()
			.setName("rewards")
			.addScript("/rewards/testdb/schema.sql")
			.addScript("/rewards/testdb/test-data.sql")
			.build();
	}

}
