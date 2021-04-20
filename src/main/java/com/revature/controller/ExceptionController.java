package com.revature.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dto.MessageDTO;
import com.revature.exceptions.AccountNotFoundException;
import com.revature.exceptions.AddClientException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.ClientNotFoundException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.UpdateClientException;

import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;

public class ExceptionController implements Controller {

	private Logger logger = LoggerFactory.getLogger(ExceptionController.class);
	
	/*
	 * Exception handler
	 */
	private ExceptionHandler<BadParameterException> badParameterExceptionHandler = (e, ctx) -> {
		logger.warn("A user provided a bad parameter. Exception message is: \n" + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<DatabaseException> databaseExceptionHandler = (e, ctx) -> {
		logger.error("Could not connect to database. Exception message is \n" + e.getMessage());
		ctx.status(500);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<AddClientException> addClientExceptionHandler = (e, ctx) -> {
		logger.warn("Could not add client. Exception message is \n" + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<ClientNotFoundException> clientNotFoundExceptionHandler = (e, ctx) -> {
		logger.warn("A user tried to get or update information of a client that doesn't exist. Exception message is: \n" + e.getMessage());
		ctx.status(404); //
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<AccountNotFoundException> accountNotFoundExceptionHandler = (e, ctx) -> {
		logger.warn("A user tried to get or update information of an account that doesn't exist. Exception message is: \n" + e.getMessage());
		ctx.status(404); //
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<UpdateClientException> UpdateClientExceptionHandler = (e, ctx) -> {
		logger.warn("Could not update client. Exception message is \n" + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.exception(BadParameterException.class, badParameterExceptionHandler);
		app.exception(DatabaseException.class, databaseExceptionHandler);
		app.exception(AddClientException.class, addClientExceptionHandler);
		app.exception(ClientNotFoundException.class, clientNotFoundExceptionHandler);
		app.exception(AccountNotFoundException.class, accountNotFoundExceptionHandler);
		app.exception(UpdateClientException.class, UpdateClientExceptionHandler);
	}

}
