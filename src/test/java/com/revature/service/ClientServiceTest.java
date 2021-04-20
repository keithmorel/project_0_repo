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

import com.revature.dao.ClientRepository;
import com.revature.dto.GetClientDTO;
import com.revature.dto.PostClientDTO;
import com.revature.exceptions.AddClientException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.ClientNotFoundException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.UpdateClientException;
import com.revature.model.Client;
import com.revature.util.ConnectionUtil;

public class ClientServiceTest {
	
	private static ClientRepository mockClientRepository;
	private static Connection mockConnection;
	
	private ClientService clientService;
	
	@BeforeClass
	public static void setUp() throws DatabaseException, ClientNotFoundException, AddClientException {
		mockClientRepository = mock(ClientRepository.class);
		mockConnection = mock(Connection.class);
		
		List<GetClientDTO> clientList = new ArrayList<>();
		clientList.add(new GetClientDTO(1, "Jane", "Doe"));
		clientList.add(new GetClientDTO(2, "John", "Doe"));
		when(mockClientRepository.getAllClients())
			.thenReturn(clientList);
		
		when(mockClientRepository.getClientById(eq(1)))
			.thenReturn(new GetClientDTO(1, "Robert", "Bobby"));
		
		when(mockClientRepository.addClient(eq(new PostClientDTO("Robert", "Bobby"))))
			.thenReturn(new Client(1, "Robert", "Bobby"));
		
		when(mockClientRepository.updateClient(eq(1), eq(new PostClientDTO("New", "Client"))))
			.thenReturn(new GetClientDTO(1, "New", "Client"));
		
		when(mockClientRepository.deleteClient(eq(1)))
			.thenReturn(true);
	}
	
	@Before
	public void beforeTest() {
		clientService = new ClientService(mockClientRepository);
	}
	
	/*
	 * Happy Path Tests
	 */
	@Test 
	public void test_happyPath_getAllClients() throws DatabaseException {
	
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			List<GetClientDTO> actual = clientService.getAllClients();
			List<GetClientDTO> expected = new ArrayList<>();
			expected.add(new GetClientDTO(1, "Jane", "Doe"));
			expected.add(new GetClientDTO(2, "John", "Doe"));
			assertEquals(expected, actual);
		}
		
	}
	
	@Test 
	public void test_happyPath_getClientById() throws ClientNotFoundException, DatabaseException, BadParameterException {
	
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			GetClientDTO actual = clientService.getClientById("1");
			GetClientDTO expected = new GetClientDTO(1, "Robert", "Bobby");
			assertEquals(expected, actual);
		}
		
	}
	
	@Test
	public void test_happyPath_addClient() throws DatabaseException, AddClientException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			Client actual = clientService.addClient(new PostClientDTO("Robert", "Bobby"));
			Client expected = new Client(1, "Robert", "Bobby");
			assertEquals(expected, actual);
		}
		
	}
	
	@Test
	public void test_happyPath_updateClient() throws UpdateClientException, DatabaseException, ClientNotFoundException, BadParameterException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			GetClientDTO actual = clientService.updateClient("1", new PostClientDTO("New", "Client"));
			GetClientDTO expected = new GetClientDTO(1, "New", "Client");
			assertEquals(expected, actual);
		}
		
	}
	
	@Test
	public void test_happyPath_deleteClient() throws ClientNotFoundException, DatabaseException, BadParameterException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			boolean actual = clientService.deleteClient("1");
			boolean expected = true;
			assertEquals(expected, actual);
		}
		
	}
	
	@Test
	public void test_nonIntClientId_getClientById() throws ClientNotFoundException, DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.getClientById("abc");
				fail("BadParameterException was not thrown");
			} catch (BadParameterException e) {
				assertEquals(e.getMessage(), "Client id must be an int. User Provided: abc");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameBlank_lastNameBlank_addClient() throws DatabaseException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.addClient(new PostClientDTO("", ""));
				fail("AddClientException was not thrown");
			} catch (AddClientException e) {
				assertEquals(e.getMessage(), "User tried to add a client without a first or last name");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameBlank_lastNameNotBlank_addClient() throws DatabaseException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.addClient(new PostClientDTO("", "Test"));
				fail("AddClientException was not thrown");
			} catch (AddClientException e) {
				assertEquals(e.getMessage(), "User tried to add a client without a first or last name");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameBlank_withSpaces_lastNameNotBlank_addClient() throws DatabaseException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.addClient(new PostClientDTO("    ", "Test"));
				fail("AddClientException was not thrown");
			} catch (AddClientException e) {
				assertEquals(e.getMessage(), "User tried to add a client without a first or last name");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameNotBlank_lastNameBlank_addClient() throws DatabaseException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.addClient(new PostClientDTO("Test", ""));
				fail("AddClientException was not thrown");
			} catch (AddClientException e) {
				assertEquals(e.getMessage(), "User tried to add a client without a first or last name");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameNotBlank_lastNameBlank_withSpaces_addClient() throws DatabaseException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.addClient(new PostClientDTO("Test", "    "));
				fail("AddClientException was not thrown");
			} catch (AddClientException e) {
				assertEquals(e.getMessage(), "User tried to add a client without a first or last name");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameBlank_withSpaces_lastNameBlank_withSpaces_addClient() throws DatabaseException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.addClient(new PostClientDTO("   ", "         "));
				fail("AddClientException was not thrown");
			} catch (AddClientException e) {
				assertEquals(e.getMessage(), "User tried to add a client without a first or last name");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameBlank_noSpaces_lastNameBlank_withSpaces_addClient() throws DatabaseException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.addClient(new PostClientDTO("", "         "));
				fail("AddClientException was not thrown");
			} catch (AddClientException e) {
				assertEquals(e.getMessage(), "User tried to add a client without a first or last name");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameBlank_withSpaces_lastNameBlank_noSpaces_addClient() throws DatabaseException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.addClient(new PostClientDTO("   ", ""));
				fail("AddClientException was not thrown");
			} catch (AddClientException e) {
				assertEquals(e.getMessage(), "User tried to add a client without a first or last name");
			}
			
		}
		
	}
	
	@Test
	public void test_nonIntClientId_updateClient() throws UpdateClientException, DatabaseException, ClientNotFoundException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.updateClient("abc", new PostClientDTO("Test", "Test"));
				fail("BadParameterException was not thrown");
			} catch (BadParameterException e) {
				assertEquals(e.getMessage(), "Client id must be an int. User provided: abc");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameBlank_lastNameBlank_updateClient() throws DatabaseException, ClientNotFoundException, BadParameterException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.updateClient("1", new PostClientDTO("", ""));
				fail("AddClientException was not thrown");
			} catch (UpdateClientException e) {
				assertEquals(e.getMessage(), "User tried to update a client without giving a first and last name to update");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameBlank_lastNameNotBlank_updateClient() throws DatabaseException, ClientNotFoundException, BadParameterException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.updateClient("1", new PostClientDTO("", "Test"));
				fail("AddClientException was not thrown");
			} catch (UpdateClientException e) {
				assertEquals(e.getMessage(), "User tried to update a client without giving a first and last name to update");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameBlank_withSpaces_lastNameNotBlank_updateClient() throws DatabaseException, ClientNotFoundException, BadParameterException   {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.updateClient("1", new PostClientDTO("    ", "Test"));
				fail("AddClientException was not thrown");
			} catch (UpdateClientException e) {
				assertEquals(e.getMessage(), "User tried to update a client without giving a first and last name to update");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameNotBlank_lastNameBlank_updateClient() throws DatabaseException, ClientNotFoundException, BadParameterException   {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.updateClient("1", new PostClientDTO("Test", ""));
				fail("AddClientException was not thrown");
			} catch (UpdateClientException e) {
				assertEquals(e.getMessage(), "User tried to update a client without giving a first and last name to update");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameNotBlank_lastNameBlank_withSpaces_updateClient() throws DatabaseException, ClientNotFoundException, BadParameterException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.updateClient("1", new PostClientDTO("Test", "      "));
				fail("AddClientException was not thrown");
			} catch (UpdateClientException e) {
				assertEquals(e.getMessage(), "User tried to update a client without giving a first and last name to update");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameBlank_withSpaces_lastNameBlank_withSpaces_updateClient() throws DatabaseException, ClientNotFoundException, BadParameterException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.updateClient("1", new PostClientDTO("        ", "           "));
				fail("AddClientException was not thrown");
			} catch (UpdateClientException e) {
				assertEquals(e.getMessage(), "User tried to update a client without giving a first and last name to update");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameBlank_noSpaces_lastNameBlank_withSpaces_updateClient() throws DatabaseException, ClientNotFoundException, BadParameterException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.updateClient("1", new PostClientDTO("", "           "));
				fail("AddClientException was not thrown");
			} catch (UpdateClientException e) {
				assertEquals(e.getMessage(), "User tried to update a client without giving a first and last name to update");
			}
			
		}
		
	}
	
	@Test
	public void test_firstNameBlank_withSpaces_lastNameBlank_noSpaces_updateClient() throws DatabaseException, ClientNotFoundException, BadParameterException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.updateClient("1", new PostClientDTO("        ", ""));
				fail("AddClientException was not thrown");
			} catch (UpdateClientException e) {
				assertEquals(e.getMessage(), "User tried to update a client without giving a first and last name to update");
			}
			
		}
		
	}
	
	@Test
	public void test_nonIntClientId_deleteClient() throws ClientNotFoundException, DatabaseException  {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				clientService.deleteClient("abc");
				fail("BadParameterException was not thrown");
			} catch (BadParameterException e) {
				assertEquals(e.getMessage(), "Client id must be an int. User provided: abc");
			}
			
		}
		
	}
	
}





