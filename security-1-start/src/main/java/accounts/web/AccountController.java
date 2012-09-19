package accounts.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import accounts.Account;
import accounts.AccountManager;

@Controller
public class AccountController {
	private AccountManager accountManager;
	
	@Autowired
	public AccountController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	@RequestMapping("/accountList")
	public void getAccountList(Model model) {
		model.addAttribute("accounts", accountManager.getAllAccounts());
	}

	@RequestMapping("/accountDetails")
	public void getAccountDetails(Integer entityId, Model model) {
		model.addAttribute("account", accountManager.getAccount(entityId));
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.setRequiredFields(new String[] {"number", "name"});
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/editAccount")
	public void getEditAccount(Integer entityId, Model model) {
		model.addAttribute("account", accountManager.getAccount(entityId));
	}
	
    public void validateAccount(Account account, Errors errors) {
    	if (!StringUtils.hasText(account.getNumber())) {
    		errors.rejectValue("number", "empty.value");
    	}
       	if (!StringUtils.hasText(account.getName())) {
    		errors.rejectValue("name", "empty.value");
    	}
    }  

	@RequestMapping(method=RequestMethod.POST, value="/editAccount")
	public String postEditAccount(Account account, BindingResult bindingResult) {
		validateAccount(account, bindingResult);
		if (bindingResult.hasErrors()) {
			return "editAccount";
		}
		accountManager.update(account);
		return "redirect:/accounts/accountDetails?entityId=" + account.getEntityId();
	}
}
