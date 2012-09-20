package rewards.ws.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@ContextConfiguration(locations = { "classpath:rewards/ws/client/client-config.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class SoapRewardNetworkTests {

	private static final String NAMESPACE_URI = "http://www.springsource.com/reward-network";

	@Autowired
	private WebServiceTemplate webServiceTemplate;

	@Test
	public void testWebServiceWithJAXB() throws Exception {
		// TODO 5: Implement this method by using your generated JAXB2 classes
	}

	@Test
	public void testWebServiceWithXml() throws Exception {
		Document document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();
		Element requestElement = document.createElementNS(NAMESPACE_URI,
				"rewardAccountForDiningRequest");
		requestElement.setAttribute("amount", "100.00");
		requestElement.setAttribute("creditCardNumber", "1234123412341234");
		requestElement.setAttribute("merchantNumber", "1234567890");

		System.out.println(xmlToString(requestElement));
		
		Source source = new DOMSource(requestElement);
		Result result = new DOMResult();
		webServiceTemplate.sendSourceAndReceiveToResult(source, result);
		Element responseElement = (Element) ((DOMResult) result).getNode().getFirstChild();
		
		// assert the expected reward confirmation results
		assertNotNull(responseElement);
		
		// the account number should be '123456789'
		assertEquals("123456789", responseElement.getAttribute("accountNumber"));
		
		// the total contribution amount should be 8.00 (8% of 100.00)
		assertEquals("8.00", responseElement.getAttribute("amount"));
		
		
	}

	private String xmlToString(Element requestElement)
			throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
		Source source = new DOMSource(requestElement);
		StringWriter stringWriter = new StringWriter();
		Result result = new StreamResult(stringWriter);
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.transform(source, result);
		return (stringWriter.getBuffer().toString());
	}

}
