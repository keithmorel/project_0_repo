package com.revature.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;

import com.revature.dao.AccountRepository;
import com.revature.dto.AccountDTO;
import com.revature.exceptions.AccountNotFoundException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.model.Account;
import com.revature.util.ConnectionUtil;

public class AccountServiceTest {
	
	private static AccountRepository mockAccountRepository;
	private static Connection mockConnection;
	
	private AccountService accountService;
	
	@BeforeClass
	public static void setUp() throws DatabaseException, AccountNotFoundException {
		mockAccountRepository = mock(AccountRepository.class);
		mockConnection = mock(Connection.class);
		
		List<Account> accountList = new ArrayList<>();
		accountList.add(new Account(1, "Savings", 1000));
		accountList.add(new Account(2, "Checking", 15000));
		when(mockAccountRepository.getAllAccountsOfClient(eq(1)))
			.thenReturn(accountList);
		
		List<Account> accountList2 = new ArrayList<>();
		accountList2.add(new Account(1, "Savings", 1000));
		when(mockAccountRepository.getAllAccountsOfClientLessThan(eq(1), eq(10000)))
		.thenReturn(accountList2);
		
		List<Account> accountList3 = new ArrayList<>();
		accountList3.add(new Account(2, "Checking", 15000));
		when(mockAccountRepository.getAllAccountsOfClientGreaterThan(eq(1), eq(5000)))
		.thenReturn(accountList3);

		List<Account> accountList4 = new ArrayList<>();
		when(mockAccountRepository.getAllAccountsOfClient(eq(1), eq(5000), eq(10000)))
		.thenReturn(accountList4);
		
		when(mockAccountRepository.getAccountById(eq(1), eq(2)))
			.thenReturn(new Account(2, "Savings", 10000));
		
		when(mockAccountRepository.addAccount(eq(1), eq(new AccountDTO("Savings", 10000))))
			.thenReturn(new Account(2, "Savings", 10000));
		
		when(mockAccountRepository.updateAccount(eq(1), eq(2), eq(new AccountDTO("Checking", 99999))))
			.thenReturn(new Account(2, "Checking", 99999));
		
		when(mockAccountRepository.deleteAccount(eq(1), eq(2)))
			.thenReturn(true);
	}
	
	@Before
	public void beforeTest() {
		accountService = new AccountService(mockAccountRepository);
	}
	
	@Test 
	public void test_happyPath_getAllAccountsOfClient() throws DatabaseException, BadParameterException {
	
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			List<Account> actual = accountService.getAllAccountsOfClient("1");
			List<Account> expected = new ArrayList<>();
			expected.add(new Account(1, "Savings", 1000));
			expected.add(new Account(2, "Checking", 15000));
			assertEquals(expected, actual);
		}
		
	}
	
	@Test 
	public void test_happyPath_getAllAccountsOfClientLessThan() throws DatabaseException, BadParameterException {
	
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			List<Account> actual = accountService.getAllAccountsOfClientLessThan("1", "10000");
			List<Account> expected = new ArrayList<>();
			expected.add(new Account(1, "Savings", 1000));
			assertEquals(expected, actual);
		}
		
	}
	
	@Test 
	public void test_happyPath_getAllAccountsOfClientGreaterThan() throws DatabaseException, BadParameterException {
	
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			List<Account> actual = accountService.getAllAccountsOfClientGreaterThan("1", "5000");
			List<Account> expected = new ArrayList<>();
			expected.add(new Account(2, "Checking", 15000));
			assertEquals(expected, actual);
		}
		
	}
	
	@Test 
	public void test_happyPath_getAllAccountsOfClient_greaterThan_lessThan() throws DatabaseException, BadParameterException {
	
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			List<Account> actual = accountService.getAllAccountsOfClient("1", "5000", "10000");
			List<Account> expected = new ArrayList<>();
			assertEquals(expected, actual);
		}
		
	}
	
	@Test 
	public void test_happyPath_getAccountById() throws DatabaseException, AccountNotFoundException, BadParameterException {
	
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			Account actual = accountService.getAccountById("1", "2");
			Account expected = new Account(2, "Savings", 10000);
			assertEquals(expected, actual);
		}
		
	}
	
	@Test 
	public void test_happyPath_addAccount() throws DatabaseException, BadParameterException {
	
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			Account actual = accountService.addAccount("1", new AccountDTO("Savings", 10000));
			Account expected = new Account(2, "Savings", 10000);
			assertEquals(expected, actual);
		}
		
	}
	
	@Test 
	public void test_happyPath_updateAccount() throws DatabaseException, AccountNotFoundException, BadParameterException {
	
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			Account actual = accountService.updateAccount("1", "2", new AccountDTO("Checking", 99999));
			Account expected = new Account(2, "Checking", 99999);
			assertEquals(expected, actual);
		}
		
	}
	
	@Test 
	public void test_happyPath_deleteAccount() throws AccountNotFoundException, DatabaseException {
	
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			boolean actual = accountService.deleteAccount("1", "2");
			boolean expected = true;
			assertEquals(expected, actual);
		}
		
	}
	
	@Test
	public void test_nonIntClientId_getAllAccountsOfClient() throws DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				accountService.getAllAccountsOfClient("abc");
				fail("BadParameterException was not thrown");
			} catch (BadParameterException e) {
				assertEquals(e.getMessage(), "Account id must be an int. User provided id: abc");
			}
			
		}
	}
	
	@Test
	public void test_nonIntClientId_nonIntLessThanStr_getAllAccountsOfClientLessThan() throws DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				accountService.getAllAccountsOfClientLessThan("abc", "abc");
				fail("BadParameterException was not thrown");
			} catch (BadParameterException e) {
				assertEquals(e.getMessage(), "Account id, amount greater than and amount less than values, must all be ints. User provided \nid: abc amount less than: abc");
			}
			
		}
	}
	
	@Test
	public void test_nonIntClientId_intLessThanStr_getAllAccountsOfClientLessThan() throws DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				accountService.getAllAccountsOfClientLessThan("abc", "100");
				fail("BadParameterException was not thrown");
			} catch (BadParameterException e) {
				assertEquals(e.getMessage(), "Account id, amount greater than and amount less than values, must all be ints. User provided \nid: abc amount less than: 100");
			}
			
		}
	}
	
	@Test
	public void test_intClientId_nonIntLessThanStr_getAllAccountsOfClientLessThan() throws DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				accountService.getAllAccountsOfClientLessThan("100", "abc");
				fail("BadParameterException was not thrown");
			} catch (BadParameterException e) {
				assertEquals(e.getMessage(), "Account id, amount greater than and amount less than values, must all be ints. User provided \nid: 100 amount less than: abc");
			}
			
		}
	}
	
	@Test
	public void test_nonIntClientId_nonIntLessThanStr_getAllAccountsOfClientGreaterThan() throws DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				accountService.getAllAccountsOfClientGreaterThan("abc", "abc");
				fail("BadParameterException was not thrown");
			} catch (BadParameterException e) {
				assertEquals(e.getMessage(), "Account id, amount greater than and amount less than values, must all be ints. User provided \nid: abc amount greater than: abc");
			}
			
		}
	}
	
	@Test
	public void test_nonIntClientId_intLessThanStr_getAllAccountsOfClientGreaterThan() throws DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				accountService.getAllAccountsOfClientGreaterThan("abc", "100");
				fail("BadParameterException was not thrown");
			} catch (BadParameterException e) {
				assertEquals(e.getMessage(), "Account id, amount greater than and amount less than values, must all be ints. User provided \nid: abc amount greater than: 100");
			}
			
		}
	}
	
	@Test
	public void test_intClientId_nonIntLessThanStr_getAllAccountsOfClientGreaterThan() throws DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				accountService.getAllAccountsOfClientGreaterThan("100", "abc");
				fail("BadParameterException was not thrown");
			} catch (BadParameterException e) {
				assertEquals(e.getMessage(), "Account id, amount greater than and amount less than values, must all be ints. User provided \nid: 100 amount greater than: abc");
			}
			
		}
	}

}
