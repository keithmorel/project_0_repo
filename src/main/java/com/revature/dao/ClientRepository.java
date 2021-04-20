package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.dto.GetClientDTO;
import com.revature.dto.PostClientDTO;
import com.revature.exceptions.AddClientException;
import com.revature.exceptions.ClientNotFoundException;
import com.revature.exceptions.DatabaseException;
import com.revature.model.Client;
import com.revature.util.ConnectionUtil;

public class ClientRepository {
	
	private Connection connection;
	private ClientRepository clientRepository;
	
	public ClientRepository() {
		super();
	}
	
	public ClientRepository(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public List<GetClientDTO> getAllClients() throws DatabaseException {
		
		List<GetClientDTO> clientList = new ArrayList<>();

		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM clients";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				
				int clientId = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				
				clientList.add(new GetClientDTO(clientId, firstName, lastName));
				
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
		return clientList;
		
	}
	
	public GetClientDTO getClientById(int clientId) throws ClientNotFoundException, DatabaseException {
		
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM clients WHERE id = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1,  clientId);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				GetClientDTO client = new GetClientDTO(clientId, firstName, lastName);
				return client;
			} else {
				throw new ClientNotFoundException("Couldn't find a client with that id. Id provided: " + clientId);
			}
			
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
	}

	public Client addClient(PostClientDTO clientDTO) throws DatabaseException, AddClientException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO clients (first_name, last_name) VALUES (?, ?)";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, clientDTO.getFirstName());
			pstmt.setString(2, clientDTO.getLastName());
			
			int recordsAdded = pstmt.executeUpdate();
			
			if (recordsAdded != 1) {
				throw new DatabaseException("Couldn't add a client to the database");
			}
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				Client newClient = new Client(id, clientDTO.getFirstName(), clientDTO.getLastName());
				newClient.setAccounts(new ArrayList<>());
				return newClient;
			} else {
				throw new DatabaseException("Client id was not generated, and therefore adding a client failed");
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
	}

	public GetClientDTO updateClient(int clientId, PostClientDTO clientDTO) throws DatabaseException, ClientNotFoundException {

		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "UPDATE clients SET first_name = ?, last_name = ? WHERE id = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, clientDTO.getFirstName());
			pstmt.setString(2, clientDTO.getLastName());
			pstmt.setInt(3,  clientId);
			
			int rowsAffected = pstmt.executeUpdate();
			
			if (rowsAffected != 1) {
				throw new ClientNotFoundException("Couldn't update client because it doesnt exist.");
			} else {
				// No issue with update query, return a new object with the updated information provided
				return new GetClientDTO(clientId, clientDTO.getFirstName(), clientDTO.getLastName());
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}		
		
	}

	public boolean deleteClient(int clientId) throws ClientNotFoundException, DatabaseException {

		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "DELETE FROM clients WHERE id = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1,  clientId);
			
			int rowsAffected = pstmt.executeUpdate();
			
			if (rowsAffected != 1) {
				throw new ClientNotFoundException("Couldn't delete client because it doesnt exist.");
			} else {
				// No issue with delete query, return a new object with the updated information provided
				return true;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
		
	}
	
}
