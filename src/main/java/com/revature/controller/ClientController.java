package com.revature.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dto.GetClientDTO;
import com.revature.dto.MessageDTO;
import com.revature.dto.PostClientDTO;
import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.service.AccountService;
import com.revature.service.ClientService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class ClientController implements Controller {
	
	private Logger logger = LoggerFactory.getLogger(AccountController.class);

	private ClientService clientService;
	private AccountService accountService;
	
	public ClientController() {
		this.clientService = new ClientService();
		this.accountService = new AccountService();
	}
	
	private Handler getAllClients = ctx -> {
		
		List<GetClientDTO> clientList = clientService.getAllClients();
		
		ctx.json(clientList);
		ctx.status(200);
		
	};
	
	private Handler getClientById = ctx -> {
		
		String clientId = ctx.pathParam("id");
		GetClientDTO clientDTO = clientService.getClientById(clientId);
		List<Account> accountList = accountService.getAllAccountsOfClient(clientId);
		
		Client clientWithAccounts = new Client(clientDTO.getId(), clientDTO.getFirstName(), clientDTO.getLastName());
		clientWithAccounts.setAccounts(accountList);
		
		ctx.json(clientWithAccounts);
		ctx.status(200);
	};
	
	private Handler addClient = ctx -> {
		
		PostClientDTO clientDTO = ctx.bodyAsClass(PostClientDTO.class);
		Client client = clientService.addClient(clientDTO);
		logger.info("Successfully created account with id: " + client.getId());
		ctx.json(client);
		ctx.status(201);
	};
	
	private Handler updateClient = ctx -> {
		
		String clientId = ctx.pathParam("id");
		PostClientDTO clientDTO = ctx.bodyAsClass(PostClientDTO.class);
		GetClientDTO updatedClientDTO = clientService.updateClient(clientId, clientDTO);
		List<Account> accountList = accountService.getAllAccountsOfClient(clientId);
		
		Client updatedClientWithAccounts = new Client(updatedClientDTO.getId(), updatedClientDTO.getFirstName(), updatedClientDTO.getLastName());
		updatedClientWithAccounts.setAccounts(accountList);
		logger.info("Successfully updated account with id: " + clientId);
		ctx.json(updatedClientWithAccounts);
		ctx.status(200);
	};
	
	private Handler deleteClient = ctx -> {
		
		String clientId = ctx.pathParam("id");
		boolean didDelete = clientService.deleteClient(clientId);
		
		MessageDTO message = new MessageDTO();
		if (didDelete) {
			logger.info("Successfully deleted account with id: " + clientId);
			message.setMessage("Client was successfully deleted");
			ctx.json(message.getMessage()).status(200);
		} else { // Should most likely never get here, but just in case give 400 status instead of arbitrary 500 status
			message.setMessage("Failed to delete client");
			ctx.json(message.getMessage()).status(400);
		}
		
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/clients", getAllClients);
		app.get("/clients/:id", getClientById);
		app.post("/clients", addClient);
		app.put("/clients/:id", updateClient);
		app.delete("/clients/:id", deleteClient);
	}

}
