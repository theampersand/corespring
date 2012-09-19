package accounts.internal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import accounts.Account;
import accounts.AccountManager;
import accounts.Beneficiary;

import common.money.Percentage;

/**
 * An account manager that uses Hibernate to find accounts.
 */
@Repository
@Transactional
public class HibernateAccountManager implements AccountManager {

	private SessionFactory sessionFactory;

	/**
	 * Creates a new Hibernate account manager.
	 * @param sessionFactory the Hibernate session factory
	 */
	public HibernateAccountManager(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<Account> getAllAccounts() {
		return sessionFactory.getCurrentSession().createQuery("from Account").list();
	}

	@Transactional(readOnly = true)
	public Account getAccount(Integer id) {
		return (Account) sessionFactory.getCurrentSession().get(Account.class, id);
	}

	public void update(Account account) {
		sessionFactory.getCurrentSession().update(account);
	}

	public Account save(Account newAccount) {
		sessionFactory.getCurrentSession().save(newAccount);
		return newAccount;
	}
	
	public void updateBeneficiaryAllocationPercentages(Integer accountId, Map<String, Percentage> allocationPercentages) {
		Account account = getAccount(accountId);
		for (Entry<String, Percentage> entry : allocationPercentages.entrySet()) {
			account.getBeneficiary(entry.getKey()).setAllocationPercentage(entry.getValue());
		}
	}

	public void addBeneficiary(Integer accountId, String beneficiaryName) {
		getAccount(accountId).addBeneficiary(beneficiaryName, Percentage.zero());
	}

	public void removeBeneficiary(Integer accountId, String beneficiaryName, Map<String, Percentage> allocationPercentages) {
		getAccount(accountId).removeBeneficiary(beneficiaryName);
		updateBeneficiaryAllocationPercentages(accountId, allocationPercentages);
	}

	public void removeBeneficiary(Integer accountId, String beneficiaryName) {
		Account account = getAccount(accountId);
		Beneficiary beneficiaryToRemove = account.getBeneficiary(beneficiaryName);
		account.removeBeneficiary(beneficiaryName);
		Set<Beneficiary> remainingBeneficiaries = account.getBeneficiaries();
		if (remainingBeneficiaries.isEmpty()) return;
		Percentage extra = new Percentage(
				beneficiaryToRemove.getAllocationPercentage().asBigDecimal().divide(BigDecimal.valueOf(remainingBeneficiaries.size())));
		for (Beneficiary beneficiary : remainingBeneficiaries) {
			beneficiary.setAllocationPercentage(beneficiary.getAllocationPercentage().add(extra));
		}
	}
}