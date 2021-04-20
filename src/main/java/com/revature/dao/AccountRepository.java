package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.dto.AccountDTO;
import com.revature.exceptions.AccountNotFoundException;
import com.revature.exceptions.DatabaseException;
import com.revature.model.Account;
import com.revature.util.ConnectionUtil;

public class AccountRepository {

	// Transaction management
	// A good practice might be to handle a transaction over in the service layer. Here is where we might perform
	// individual database operations
	
	// So, I will create a constructor that accepts an external connection object into this DAO instead
	// and then we'll make use of that
	private Connection connection;
	
	public AccountRepository() {
		super();
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public List<Account> getAllAccountsOfClient(int clientId) throws DatabaseException {

		List<Account> accountList = new ArrayList<>();

		try (Connection connection = ConnectionUtil.getConnection()) {
			
			String sql = "SELECT * FROM accounts WHERE client_id = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1,  clientId);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String accountType = rs.getString("accountType");
				int amount = rs.getInt("amount");
				
				accountList.add(new Account(id, accountType, amount));
				
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
		return accountList;
		
	}
	
	public List<Account> getAllAccountsOfClientLessThan(int clientId, int amtLessThan) throws DatabaseException {

		List<Account> accountList = new ArrayList<>();

		try (Connection connection = ConnectionUtil.getConnection()) {
			
			String sql = "SELECT * FROM accounts WHERE client_id = ? AND amount < ?";
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1,  clientId);
			pstmt.setInt(2,  amtLessThan);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String accountType = rs.getString("accountType");
				int amount = rs.getInt("amount");
				
				accountList.add(new Account(id, accountType, amount));
				
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
		return accountList;
		
	}
	
	public List<Account> getAllAccountsOfClientGreaterThan(int clientId, int amtGreaterThan) throws DatabaseException {

		List<Account> accountList = new ArrayList<>();

		try (Connection connection = ConnectionUtil.getConnection()) {
			
			String sql = "SELECT * FROM accounts WHERE client_id = ? AND amount > ?";
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1,  clientId);
			pstmt.setInt(2,  amtGreaterThan);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String accountType = rs.getString("accountType");
				int amount = rs.getInt("amount");
				
				accountList.add(new Account(id, accountType, amount));
				
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
		return accountList;
		
	}
	
	// Overloaded method to incorporate the amountGreaterThan and amountLessThan query params
	public List<Account> getAllAccountsOfClient(int clientId, int amtGreaterThan, int amtLessThan) throws DatabaseException {

		List<Account> accountList = new ArrayList<>();

		try (Connection connection = ConnectionUtil.getConnection()) {
			
			String sql = "SELECT * FROM accounts WHERE client_id = ? AND amount > ? AND amount < ?";
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1,  clientId);
			pstmt.setInt(2,  amtGreaterThan);
			pstmt.setInt(3,  amtLessThan);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String accountType = rs.getString("accountType");
				int amount = rs.getInt("amount");
				
				accountList.add(new Account(id, accountType, amount));
				
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
		return accountList;
		
	}

	public Account addAccount(int clientId, AccountDTO accDTO) throws DatabaseException {
		
		try {
			String sql = "INSERT INTO accounts (accountType, amount, client_id) VALUES (?, ?, ?)";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, accDTO.getAccountType());
			pstmt.setInt(2, accDTO.getAmount());
			pstmt.setInt(3, clientId);
			
			int recordsAdded = pstmt.executeUpdate();
			
			if (recordsAdded != 1) {
				throw new DatabaseException("Couldn't add an account to the database");
			}
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt("id");
				return new Account(id, accDTO.getAccountType(), accDTO.getAmount());
			} else {
				throw new DatabaseException("Account id was not generated, and therefore adding an account failed");
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong with the database. "
					+ "Exception message: " + e.getMessage());
		}
		
	}

	public Account getAccountById(int clientId, int accountId) throws DatabaseException, AccountNotFoundException {

		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM accounts WHERE client_id = ? AND id = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1,  clientId);
			pstmt.setInt(2, accountId);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				String accountType = rs.getString("accountType");
				int amount = rs.getInt("amount");
				
				return new Account(id, accountType, amount);
				
			} else {
				throw new AccountNotFoundException("Couldn't find an account with the id provided. User provided: " + accountId);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
	}

	public Account updateAccount(int clientId, int accountId, AccountDTO accDTO) throws AccountNotFoundException, DatabaseException {

		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "UPDATE accounts SET accountType = ?, amount = ? WHERE id = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, accDTO.getAccountType());
			pstmt.setInt(2, accDTO.getAmount());
			pstmt.setInt(3,  accountId);
			
			int rowsAffected = pstmt.executeUpdate();
			
			if (rowsAffected != 1) {
				throw new AccountNotFoundException("Couldn't update client because it doesnt exist.");
			} else {
				// No issue with update query, return a new object with the updated information provided
				return new Account(accountId, accDTO.getAccountType(), accDTO.getAmount());
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
	}

	public boolean deleteAccount(int clientId, int accountId) throws AccountNotFoundException, DatabaseException {

		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "DELETE FROM accounts WHERE id = ? AND client_id = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1,  accountId);
			pstmt.setInt(2,  clientId);
			
			int rowsAffected = pstmt.executeUpdate();
			
			if (rowsAffected != 1) {
				throw new AccountNotFoundException("Couldn't delete account because it doesnt exist.");
			} else {
				return true;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
	}
	
}
