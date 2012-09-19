package rewards.internal.account;

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

import common.money.MonetaryAmount;
import common.money.Percentage;

public class HibernateAccountRepositoryTests {

	private HibernateAccountRepository repository;

	private PlatformTransactionManager transactionManager;

	private TransactionStatus status;

	@Before
	public void setUp() throws Exception {
		repository = new HibernateAccountRepository();
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
	public void testFindByCreditCard() {
		Account account = repository.findByCreditCard("1234123412341234");
		// assert the returned account contains what you expect given the state of the database
		assertNotNull("account should never be null", account);
		assertEquals("wrong entity id", Long.valueOf(0), account.getEntityId());
		assertEquals("wrong account number", "123456789", account.getNumber());
		assertEquals("wrong name", "Keith and Keri Donald", account.getName());
		assertEquals("wrong beneficiary collection size", 2, account.getBeneficiaries().size());

		Beneficiary b1 = account.getBeneficiary("Annabelle");
		assertNotNull("Annabelle should be a beneficiary", b1);
		assertEquals("wrong savings", MonetaryAmount.valueOf("0.00"), b1.getSavings());
		assertEquals("wrong allocation percentage", Percentage.valueOf("50%"), b1.getAllocationPercentage());

		Beneficiary b2 = account.getBeneficiary("Corgan");
		assertNotNull("Corgan should be a beneficiary", b2);
		assertEquals("wrong savings", MonetaryAmount.valueOf("0.00"), b2.getSavings());
		assertEquals("wrong allocation percentage", Percentage.valueOf("50%"), b2.getAllocationPercentage());
	}

	private SessionFactory createTestSessionFactory() throws Exception {
		// simulate the Spring bean initialization lifecycle
		AnnotationSessionFactoryBean factoryBean = new AnnotationSessionFactoryBean();
		factoryBean.setDataSource(createTestDataSource());
		Class[] annotatedClasses = new Class[] { Account.class, Beneficiary.class };
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
