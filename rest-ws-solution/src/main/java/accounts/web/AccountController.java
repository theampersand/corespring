package accounts.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

import accounts.Account;
import accounts.AccountManager;
import accounts.Beneficiary;

/**
 * A controller handling requests for CRUD operations on Accounts and their Beneficiaries.
 */
@Controller
public class AccountController {
	
	private AccountManager accountManager;
	
	/**
	 * Creates a new AccountController with a given account manager.
	 */
	@Autowired 
	public AccountController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
	
	/**
	 * Provide a list of all accounts.
	 */
	@RequestMapping(value="/accounts", method=RequestMethod.GET)
	public @ResponseBody List<Account> accountSummary() {
		return accountManager.getAllAccounts();
	}
	
	/**
	 * Provide the details of an account with the given id.
	 */
	@RequestMapping(value="/accounts/{accountId}", method=RequestMethod.GET)
	public @ResponseBody Account accountDetails(@PathVariable("accountId") int id) {
		return retrieveAccount(id);
	}
	
	/**
	 * Creates a new Account, setting its URL as the Location header on the response.
	 */
	@RequestMapping(value="/accounts", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void createAccount(@RequestBody Account newAccount, 
							  HttpServletRequest request, 
							  HttpServletResponse response) {
		Account account = accountManager.save(newAccount);
		response.setHeader("Location", getLocationForChildResource(request, account.getEntityId()));
	}

	/**
	 * Returns the Beneficiary with the given name for the Account with the given id.   
	 */
	@RequestMapping(value="/accounts/{accountId}/beneficiaries/{beneficiaryName}", method=RequestMethod.GET)
	public @ResponseBody Beneficiary getBeneficiary(@PathVariable("accountId") int accountId, 
			@PathVariable("beneficiaryName") String beneficiaryName) {
		return retrieveAccount(accountId).getBeneficiary(beneficiaryName);
	}
	
	/**
	 * Adds a Beneficiary with the given name to the Account with the given id,
	 * setting its URL as the Location header on the response. 
	 */
	@RequestMapping(value="/accounts/{accountId}/beneficiaries", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void addBeneficiary(@PathVariable("accountId") int accountId, 
							   @RequestBody String beneficiaryName,
							   HttpServletRequest request, 
							   HttpServletResponse response) {
		accountManager.addBeneficiary(accountId, beneficiaryName);
		response.setHeader("Location", getLocationForChildResource(request, beneficiaryName));
	}
	
	/**
	 * Removes the Beneficiary with the given name from the Account with the given id. 
	 */
	@RequestMapping(value="/accounts/{accountId}/beneficiaries/{beneficiaryName}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeBeneficiary(@PathVariable("accountId") int accountId, 
			                      @PathVariable("beneficiaryName") String beneficiaryName) {
		accountManager.removeBeneficiary(accountId, beneficiaryName);
	}
	
	/**
	 * Maps IllegalArgumentExceptions to a 404 Not Found HTTP status code.
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({IllegalArgumentException.class})
	public void handleNotFound() {
		// just return empty 404
	}

	/**
	 * Maps DataIntegrityViolationException to a 409 Conflict HTTP status code.
	 */
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler({DataIntegrityViolationException.class})
	public void handleAlreadyExists() {
		// just return empty 409
	}

	/**
	 * Finds the Account with the given id, throwing an IllegalArgumentException
	 * if there is no such Account. 
	 */
	private Account retrieveAccount(int accountId) throws IllegalArgumentException {
		Account account = accountManager.getAccount(accountId);
		if (account == null) {
			throw new IllegalArgumentException("No such account with id " + accountId);
		}
		return account;
	}

	/**
	 * determines URL of child resource based on the full URL of the given request,
	 * appending the path info with the given childIdentifier using a UriTemplate.
	 */ 
	private String getLocationForChildResource(HttpServletRequest request, Object childIdentifier) {
		StringBuffer url = request.getRequestURL();
		UriTemplate template = new UriTemplate(url.append("/{childId}").toString());
		return template.expand(childIdentifier).toASCIIString();
	}

}