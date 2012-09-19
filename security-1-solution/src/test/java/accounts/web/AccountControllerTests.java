package accounts.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;

import accounts.Account;
import accounts.AccountManager;

/**
 * A JUnit test case testing the AccountController. 
 */
public class AccountControllerTests {

	private AccountController controller;
	private AccountManager manager;

	@Before
	public void setUp() throws Exception {
		manager = new StubAccountManager();
		controller = new AccountController(manager);
	}

	@Test
	public void testGetAccountDetails() {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.getAccountDetails(0, model);
		Account account = (Account) model.get("account");
		assertNotNull(account);
		assertEquals(Integer.valueOf(0), account.getEntityId());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAccountList() {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.getAccountList(model);
		List<Account> accounts = (List<Account>) model.get("accounts");
		assertNotNull(accounts);
		assertEquals(1, accounts.size());
		assertEquals(Integer.valueOf(0), accounts.get(0).getEntityId());
	}

	@Test(expected = ObjectRetrievalFailureException.class)
	public void testInvalidId() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.getAccountDetails(1, model);
	}

	@Test
	public void testValidateAllValid() {
		Account account = new Account("1", "Ben");
		Errors errors = new BindException(account, "account");
		controller.validateAccount(account, errors);
		assertEquals("No errors should be registered", 0, errors.getErrorCount());
	}

	@Test
	public void testValidateInvalidName() {
		Account account = new Account("1", "");
		Errors errors = new BindException(account, "account");
		controller.validateAccount(account, errors);
		assertEquals("One error should be registered", 1, errors.getErrorCount());
		FieldError error = errors.getFieldError("name");
		assertNotNull("Should have an error registred for the name field", error);
		assertEquals("Should have registered an empty value error", "empty.value", error.getCode());
	}

	@Test
	public void testValidateInvalidNumber() {
		Account account = new Account("", "Ben");
		Errors errors = new BindException(account, "account");
		controller.validateAccount(account, errors);
		assertEquals("One error should be registered", 1, errors.getErrorCount());
		FieldError error = errors.getFieldError("number");
		assertNotNull("Should have an error registred for the number field", error);
		assertEquals("Should have registered an empty value error", "empty.value", error.getCode());
	}

	@Test
	public void testValidateAllInvalid() {
		Account account = new Account(null, null);
		Errors errors = new BindException(account, "account");
		controller.validateAccount(account, errors);
		assertEquals("Two errors should be registered", 2, errors.getErrorCount());
	}
	
	@Test
	public void testGetEditAccount() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.getEditAccount(0, model);
		Account account = (Account) model.get("account");
		assertNotNull(account);
		assertEquals(Integer.valueOf(0), account.getEntityId());
	}

	@Test
	public void testSuccessfulPost() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.getEditAccount(0, model);
		Account account = (Account) model.get("account");
		account.setName("Ben");
		account.setNumber("987654321");
		BindingResult br = new MapBindingResult(model, "account");
		String viewName = controller.postEditAccount(account, br);
		Account modifiedAccount = manager.getAccount(0);
		assertEquals("Object name has not been updated by post", "Ben", modifiedAccount.getName());
		assertEquals("Object number has not been updated by post", "987654321", modifiedAccount.getNumber());
		assertEquals("Post has not redirected to the correct URL", "redirect:/accounts/accountDetails?entityId=0", viewName);
	}

	@Test
	public void testUnsuccessfulPost() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.getEditAccount(0, model);
		Account account = (Account) model.get("account");
		account.setName("");
		account.setNumber("");
		BindingResult br = new MapBindingResult(model, "account");
		String viewName = controller.postEditAccount(account, br);
		assertEquals("Invalid Post has not returned to correct view", "editAccount", viewName);
	}
}
