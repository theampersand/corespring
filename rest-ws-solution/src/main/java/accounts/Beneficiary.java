package accounts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import common.money.MonetaryAmount;
import common.money.Percentage;

/**
 * A single beneficiary allocated to an account. Each beneficiary has a name (e.g. Annabelle) and a savings balance
 * tracking how much money has been saved for he or she to date (e.g. $1000).
 */
@Entity
@Table(name = "T_ACCOUNT_BENEFICIARY")
public class Beneficiary {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer entityId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "ALLOCATION_PERCENTAGE")
	@Type(type = "common.money.PercentageUserType")
	private Percentage allocationPercentage;

	@Column(name = "SAVINGS")
	@Type(type = "common.money.MonetaryAmountUserType")
	private MonetaryAmount savings = MonetaryAmount.valueOf("0.00");

	protected Beneficiary() {
	}

	/**
	 * Creates a new account beneficiary.
	 * @param name the name of the beneficiary
	 * @param allocationPercentage the beneficiary's allocation percentage within its account
	 */
	public Beneficiary(String name, Percentage allocationPercentage) {
		this.name = name;
		this.allocationPercentage = allocationPercentage;
	}

	/**
	 * Creates a new account beneficiary. This constructor should be called by privileged objects responsible for
	 * reconstituting an existing Account object from some external form such as a collection of database records.
	 * Marked package-private to indicate this constructor should never be called by general application code.
	 * @param name the name of the beneficiary
	 * @param allocationPercentage the beneficiary's allocation percentage within its account
	 * @param savings the total amount saved to-date for this beneficiary
	 */
	Beneficiary(String name, Percentage allocationPercentage, MonetaryAmount savings) {
		this.name = name;
		this.allocationPercentage = allocationPercentage;
		this.savings = savings;
	}

	/**
	 * Returns the entity identifier used to internally distinguish this entity among other entities of the same type in
	 * the system. Should typically only be called by privileged data access infrastructure code such as an Object
	 * Relational Mapper (ORM) and not by application code.
	 * @return the internal entity identifier
	 */
	public Integer getEntityId() {
		return entityId;
	}

	/**
	 * Sets the internal entity identifier - should only be called by privileged data access code (repositories that
	 * work with an Object Relational Mapper (ORM)). Should never be set by application code explicitly.
	 * @param entityId the internal entity identifier
	 */
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	/**
	 * Returns the beneficiary name.
	 */
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the beneficiary's allocation percentage in this account.
	 */
	public Percentage getAllocationPercentage() {
		return allocationPercentage;
	}

	/**
	 * Sets the beneficiary's allocation percentage in this account.
	 * @param allocationPercentage The new allocation percentage
	 */
	public void setAllocationPercentage(Percentage allocationPercentage) {
		this.allocationPercentage = allocationPercentage;
	}

	/**
	 * Returns the amount of savings this beneficiary has accrued.
	 */
	public MonetaryAmount getSavings() {
		return savings;
	}
	
	public void setSavings(MonetaryAmount savings) {
		this.savings = savings;
	}

	public String toString() {
		return "name = '" + name + "', allocationPercentage = " + allocationPercentage + ", savings = " + savings + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((allocationPercentage == null) ? 0 : allocationPercentage.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((savings == null) ? 0 : savings.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Beneficiary other = (Beneficiary) obj;
		if (allocationPercentage == null) {
			if (other.allocationPercentage != null)
				return false;
		} else if (!allocationPercentage.equals(other.allocationPercentage))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (savings == null) {
			if (other.savings != null)
				return false;
		} else if (!savings.equals(other.savings))
			return false;
		return true;
	}
}