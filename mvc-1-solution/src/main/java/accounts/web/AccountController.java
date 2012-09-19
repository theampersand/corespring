package accounts.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import accounts.AccountManager;

/**
 * A Spring MVC @Controller controller handling requests for both the
 * account summary and the account details pages.   
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
	 * <p>Provide a model with an account for the account detail page.</p>
	 * 
	 * @param id the id of the account
	 * @param model the "implicit" model created by Spring MVC
	 */
	@RequestMapping("/accountDetails")
	public String accountDetails(@RequestParam("entityId") int id, Model model) {
		model.addAttribute("account", accountManager.getAccount(id));
		return "accountDetails";
	}
	
	/**
	 * <p>Provide a model with a list of all accounts for the account List page.</p>
	 * 
	 * @param model the "implicit" model created by Spring MVC
	 */
	@RequestMapping("/accountList")
	public String accountList(Model model) {
		model.addAttribute("accounts", accountManager.getAllAccounts());
		return "accountList";
	}
}
