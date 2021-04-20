package com.revature.dto;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class AccountDTO {
	
	private String accountType;
	private int amount;
	
	public AccountDTO() {
		super();
	}
	
	public AccountDTO(String accountType, int amount) {
			this.accountType = accountType;
			this.amount = amount;
	}
	
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + amount;
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
		AccountDTO other = (AccountDTO) obj;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (amount != other.amount)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AccountDTO [accountType=" + accountType + ", amount=" + amount + "]";
	}
	
}
