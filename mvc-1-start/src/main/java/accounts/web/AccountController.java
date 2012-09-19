package accounts.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
	// TODO 6: Enable autowiring of the AccountManager dependency
	public AccountController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
		
	// TODO 1: Implement the /accountDetails request handling method
	
	/**
	 * <p>Provide a model with a list of all accounts for the account List page.</p>
	 * 
	 * @param model the "implicit" model created by Spring MVC
	 */
	@RequestMapping("/accountList")
	public String accountList(Model model) {
		model.addAttribute("accounts", accountManager.getAllAccounts());
		return "/WEB-INF/views/accountList.jsp";
	}
}
