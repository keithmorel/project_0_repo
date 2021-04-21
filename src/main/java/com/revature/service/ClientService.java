package com.revature.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

public class ClientService {

	private ClientRepository clientRepository;
	
	public ClientService() {
		this.clientRepository = new ClientRepository();
	}
	
	public ClientService(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}
	
	public List<GetClientDTO> getAllClients() throws DatabaseException {

		return clientRepository.getAllClients();
		
	}
	
	public GetClientDTO getClientById(String stringId) throws ClientNotFoundException, DatabaseException, BadParameterException {
		
		try {
			int clientId = Integer.parseInt(stringId);
			return clientRepository.getClientById(clientId);
		} catch (NumberFormatException e) {
			throw new BadParameterException("Client id must be an int. User Provided: " + stringId);
		}
		
	}
	
	// Add a client
	public Client addClient(PostClientDTO clientDTO) throws DatabaseException, AddClientException {
		
		if (clientDTO.getFirstName().trim().equals("") || clientDTO.getLastName().trim().equals("")) {
			throw new AddClientException("User tried to add a client without a first or last name");
		}
		
		try {
			
			Connection connection = ConnectionUtil.getConnection(); // Have control over a connection object here
			this.clientRepository.setConnection(connection); // pass the connection into the DAO
			connection.setAutoCommit(false); // Turn off autocommit so we have control over commit v. not committing
			
			Client client = clientRepository.addClient(clientDTO);
			connection.commit(); // This is when changes will actually be persisted
			return client;
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
	}

	public GetClientDTO updateClient(String clientId, PostClientDTO clientDTO) throws UpdateClientException, DatabaseException, ClientNotFoundException, BadParameterException {

		if (clientDTO.getFirstName().trim().equals("") || clientDTO.getLastName().trim().equals("")) {
			throw new UpdateClientException("User tried to update a client without giving a first and last name to update");
		}
		
		try {
			
			Connection connection = ConnectionUtil.getConnection();
			this.clientRepository.setConnection(connection);
			connection.setAutoCommit(false);
			
			try {
				int id = Integer.parseInt(clientId);
				GetClientDTO updatedClient = clientRepository.updateClient(id, clientDTO);
				connection.commit();
				return updatedClient;
			} catch (NumberFormatException e) {
				throw new BadParameterException("Client id must be an int. User provided: " + clientId);
			}			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		} 
		
	}

	public boolean deleteClient(String clientId) throws ClientNotFoundException, DatabaseException, BadParameterException {

		try {
			
			Connection connection = ConnectionUtil.getConnection();
			this.clientRepository.setConnection(connection);
			connection.setAutoCommit(false);
			
			try {
				int id = Integer.parseInt(clientId);
				boolean didUpdate = clientRepository.deleteClient(id);
				connection.commit();
				return didUpdate;
			} catch (NumberFormatException e) {
				throw new BadParameterException("Client id must be an int. User provided: " + clientId);
			}
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
	}

}
