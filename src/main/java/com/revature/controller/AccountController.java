package com.revature.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dto.AccountDTO;
import com.revature.dto.MessageDTO;
import com.revature.model.Account;
import com.revature.service.AccountService;
import com.revature.service.ClientService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class AccountController implements Controller {
	
	private Logger logger = LoggerFactory.getLogger(AccountController.class);

	private AccountService accountService;
	private ClientService clientService;
	
	public AccountController() {
		this.accountService = new AccountService();
		this.clientService = new ClientService();
	}
	
	private Handler getAllAccountsOfClient = ctx -> {
		
		String amtGreaterThan = ctx.queryParam("amountGreaterThan");
		String amtLessThan = ctx.queryParam("amountLessThan");

		String clientId = ctx.pathParam("clientid");
		
		clientService.getClientById(clientId); // Check if client exists
		
		List<Account> accountList;
		if (amtGreaterThan != null && amtLessThan != null) { // If both parameters are provided
			accountList = accountService.getAllAccountsOfClient(clientId, amtGreaterThan, amtLessThan);
		} else if (amtGreaterThan == null && amtLessThan != null) { // If only less than param provided
			accountList = accountService.getAllAccountsOfClientLessThan(clientId, amtLessThan);
		} else if (amtGreaterThan != null && amtLessThan == null) { // If only greater than param provided
			accountList = accountService.getAllAccountsOfClientGreaterThan(clientId, amtGreaterThan);
		} else { // (amtGreaterThan == null && amtLessThan == null) // If both params are missing
			accountList = accountService.getAllAccountsOfClient(clientId);
		}
		
		ctx.json(accountList);
		ctx.status(200);
	};
	
	private Handler getAccountById = ctx -> {
		
		String clientId = ctx.pathParam("clientid");
		String accountId = ctx.pathParam("accountid");
		
		clientService.getClientById(clientId); // Check if client exists
		Account account = accountService.getAccountById(clientId, accountId);
		
		ctx.json(account);
		ctx.status(200);
	};
		
	private Handler addAccount = ctx -> {
		String clientId = ctx.pathParam("clientid");
		AccountDTO accInfo = ctx.bodyAsClass(AccountDTO.class);
		clientService.getClientById(clientId); // Check if client exists
			
		Account insertedAccount = accountService.addAccount(clientId, accInfo);
		logger.info("Successfully created account with id: " + insertedAccount.getId());
		ctx.status(201); // 201 CREATED
		ctx.json(insertedAccount);		

	};
	
	private Handler updateAccount = ctx -> {
		String clientId = ctx.pathParam("clientid");
		String accountId = ctx.pathParam("accountid");
		AccountDTO accInfo = ctx.bodyAsClass(AccountDTO.class);
		
		accountService.getAccountById(clientId, accountId); // Check if client exists
		
		Account updatedAccount = accountService.updateAccount(clientId, accountId, accInfo);
		logger.info("Successfully updated account with id: " + accountId);
		ctx.status(201); // 201 CREATED
		ctx.json(updatedAccount);
	};
	
	private Handler deleteAccount = ctx -> {

		String clientId = ctx.pathParam("clientid");
		String accountId = ctx.pathParam("accountid");
		boolean didDelete = accountService.deleteAccount(clientId, accountId);
		
		MessageDTO message = new MessageDTO();
		if (didDelete) {
			logger.info("Successfully deleted account with id: " + accountId);
			message.setMessage("Account was successfully deleted");
			ctx.json(message.getMessage()).status(200);
		} else { // Should most likely never get here, but just in case give 400 status instead of arbitrary 500 status
			message.setMessage("Failed to delete account");
			ctx.json(message.getMessage()).status(400);
		}
		
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/clients/:clientid/accounts", getAllAccountsOfClient);
		app.get("/clients/:clientid/accounts/:accountid", getAccountById);
		app.post("/clients/:clientid/accounts", addAccount);
		app.put("/clients/:clientid/accounts/:accountid", updateAccount);
		app.delete("/clients/:clientid/accounts/:accountid", deleteAccount);
	}

}
