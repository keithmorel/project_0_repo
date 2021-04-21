package com.revature.exceptions;

public class NotClientsAccountException extends Exception {

	public NotClientsAccountException() {
	}

	public NotClientsAccountException(String message) {
		super(message);
	}

	public NotClientsAccountException(Throwable cause) {
		super(cause);
	}

	public NotClientsAccountException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotClientsAccountException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
