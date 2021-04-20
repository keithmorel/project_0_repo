package com.revature.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.revature.dao.AccountRepository;
import com.revature.dto.AccountDTO;
import com.revature.exceptions.AccountNotFoundException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.model.Account;
import com.revature.util.ConnectionUtil;

public class AccountService {

	private AccountRepository accountRepository;
	
	public AccountService() {
		this.accountRepository = new AccountRepository();
	}
	
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	public List<Account> getAllAccountsOfClient(String stringId) throws DatabaseException, BadParameterException {

		try {
			Connection connection = ConnectionUtil.getConnection(); // Have control over a connection object here
			this.accountRepository.setConnection(connection); // pass the connection into the DAO
			connection.setAutoCommit(false); // Turn off autocommit so we have control over commit v. not committing
			
			try {
				int clientId = Integer.parseInt(stringId);
				
				List<Account> accountList = accountRepository.getAllAccountsOfClient(clientId);
				
				connection.commit(); // This is when changes will actually be persisted
				return accountList;
			} catch (NumberFormatException e) {
				throw new BadParameterException("Account id must be an int. User provided id: " + stringId);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
	}
	
	public List<Account> getAllAccountsOfClientLessThan(String stringId, String amtLessThanStr) throws DatabaseException, BadParameterException {

		try {
			Connection connection = ConnectionUtil.getConnection(); // Have control over a connection object here
			this.accountRepository.setConnection(connection); // pass the connection into the DAO
			connection.setAutoCommit(false); // Turn off autocommit so we have control over commit v. not committing
			
			try {
				int clientId = Integer.parseInt(stringId);
				int amtLessThan = Integer.parseInt(amtLessThanStr);
				
				List<Account> accountList = accountRepository.getAllAccountsOfClientLessThan(clientId, amtLessThan);
				
				connection.commit(); // This is when changes will actually be persisted
				return accountList;
			} catch (NumberFormatException e) {
				throw new BadParameterException("Account id, amount greater than and amount less than values, must all be ints. User provided \nid: "
						+ stringId + " amount less than: " + amtLessThanStr);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
	}
	
	public List<Account> getAllAccountsOfClientGreaterThan(String stringId, String amtGreaterThanStr) throws DatabaseException, BadParameterException {

		try {
			Connection connection = ConnectionUtil.getConnection(); // Have control over a connection object here
			this.accountRepository.setConnection(connection); // pass the connection into the DAO
			connection.setAutoCommit(false); // Turn off autocommit so we have control over commit v. not committing
			
			try {
				int clientId = Integer.parseInt(stringId);
				int amtGreaterThan = Integer.parseInt(amtGreaterThanStr);
				
				List<Account> accountList = accountRepository.getAllAccountsOfClientGreaterThan(clientId, amtGreaterThan);
				
				connection.commit(); // This is when changes will actually be persisted
				return accountList;
			} catch (NumberFormatException e) {
				throw new BadParameterException("Account id, amount greater than and amount less than values, must all be ints. User provided \nid: "
						+ stringId + " amount greater than: " + amtGreaterThanStr);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
	}
	
	// Overloaded Version of the generic getAllAccountsOfClient with query params accounted for
	public List<Account> getAllAccountsOfClient(String stringId, String amtGreaterThanStr, String amtLessThanStr) throws DatabaseException, BadParameterException {

		try {
			Connection connection = ConnectionUtil.getConnection(); // Have control over a connection object here
			this.accountRepository.setConnection(connection); // pass the connection into the DAO
			connection.setAutoCommit(false); // Turn off autocommit so we have control over commit v. not committing
			
			try {
				int clientId = Integer.parseInt(stringId);
				int amtGreaterThan = Integer.parseInt(amtGreaterThanStr);
				int amtLessThan = Integer.parseInt(amtLessThanStr);
				
				List<Account> accountList = accountRepository.getAllAccountsOfClient(clientId, amtGreaterThan, amtLessThan);
				
				connection.commit(); // This is when changes will actually be persisted
				return accountList;
			} catch (NumberFormatException e) {
				throw new BadParameterException("Account id, amount greater than and amount less than values, must all be ints. User provided \nid: "
						+ stringId + " amount greater than: " + amtGreaterThanStr + " amount less than: " + amtLessThanStr);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
	}

	public Account getAccountById(String clientStringId, String accountStringId) throws DatabaseException, AccountNotFoundException, BadParameterException {

		try {
			Connection connection = ConnectionUtil.getConnection(); // Have control over a connection object here
			this.accountRepository.setConnection(connection); // pass the connection into the DAO
			connection.setAutoCommit(false); // Turn off autocommit so we have control over commit v. not committing
			
			try {
				int clientId = Integer.parseInt(clientStringId);
				int accountId = Integer.parseInt(accountStringId);
				
				Account account = accountRepository.getAccountById(clientId, accountId);
				
				connection.commit(); // This is when changes will actually be persisted
				return account;
			} catch (NumberFormatException e) {
				throw new BadParameterException("Client id and Account id must be ints. User provided " + clientStringId + " and " + accountStringId);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
	}
	
	public Account addAccount(String stringId, AccountDTO accDTO) throws BadParameterException, DatabaseException {
		try {
			Connection connection = ConnectionUtil.getConnection();
			this.accountRepository.setConnection(connection);
			connection.setAutoCommit(false);
			
			try {
				int clientId = Integer.parseInt(stringId);
				
				Account account = accountRepository.addAccount(clientId, accDTO);
				
				connection.commit(); // This is when changes will actually be persisted
				return account;
			} catch (NumberFormatException e) {
				throw new BadParameterException("Account id must be an int. User provided " + stringId);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}

	}

	public Account updateAccount(String clientStringId, String accountStringId, AccountDTO accDTO) throws AccountNotFoundException, DatabaseException, BadParameterException {

		try {
			Connection connection = ConnectionUtil.getConnection(); // Have control over a connection object here
			this.accountRepository.setConnection(connection); // pass the connection into the DAO
			connection.setAutoCommit(false); // Turn off autocommit so we have control over commit v. not committing
			
			try {
				int clientId = Integer.parseInt(clientStringId);
				int accountId = Integer.parseInt(accountStringId);
				
				Account account = accountRepository.updateAccount(clientId, accountId, accDTO);
				
				connection.commit(); // This is when changes will actually be persisted
				return account;
			} catch (NumberFormatException e) {
				throw new BadParameterException("Client id and Account id must be ints. User provided " + clientStringId + " and " + accountStringId);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
		
	}

	public boolean deleteAccount(String stringClientId, String stringAccountId) throws AccountNotFoundException, DatabaseException {

		try {
			
			Connection connection = ConnectionUtil.getConnection();
			accountRepository.setConnection(connection);
			connection.setAutoCommit(false);
			
			int clientId = Integer.parseInt(stringClientId);
			int accountId = Integer.parseInt(stringAccountId);
			boolean didUpdate = accountRepository.deleteAccount(clientId, accountId);
			connection.commit();
			return didUpdate;
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
	}
	
}
