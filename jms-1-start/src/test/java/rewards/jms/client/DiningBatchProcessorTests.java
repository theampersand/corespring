package rewards.jms.client;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.Dining;

/**
 * Tests the Dining batch processor
 */
@ContextConfiguration(locations = {"/rewards/jms/client/client-config.xml",
		"/rewards/jms/jms-rewards-config.xml",
		"/rewards/jms/jms-infrastructure-config.xml",
		"/rewards/system-test-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class DiningBatchProcessorTests {

	@Autowired
	private DiningBatchProcessor diningBatchProcessor;

	@Autowired
	private RewardConfirmationLogger confirmationLogger;

	@Test
	public void testBatch() throws Exception {
		Dining dining1 = Dining.createDining("80.93", "1234123412341234", "1234567890");
		Dining dining2 = Dining.createDining("56.12", "1234123412341234", "1234567890");
		Dining dining3 = Dining.createDining("32.64", "1234123412341234", "1234567890");
		Dining dining4 = Dining.createDining("77.05", "1234123412341234", "1234567890");
		Dining dining5 = Dining.createDining("94.50", "1234123412341234", "1234567890");

		List<Dining> batch = new ArrayList<Dining>();
		batch.add(dining1);
		batch.add(dining2);
		batch.add(dining3);
		batch.add(dining4);
		batch.add(dining5);

		// TODO 08: invoke the DiningBatchProcessor to send dining list via JMS

		waitForBatch(batch.size(), 1000);

		// TODO 09: assert that the confirmation logger has received the entire batch
	}

	private void waitForBatch(int batchSize, int timeout) throws InterruptedException {
		int sleepTime = 50;
		while (confirmationLogger.getConfirmations().size() < batchSize && timeout > 0) {
			Thread.sleep(sleepTime);
			timeout -= sleepTime;
		}
	}
}
