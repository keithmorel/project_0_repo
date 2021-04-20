package com.revature.exceptions;

public class UpdateClientException extends Exception {

	public UpdateClientException() {
	}

	public UpdateClientException(String message) {
		super(message);
	}

	public UpdateClientException(Throwable cause) {
		super(cause);
	}

	public UpdateClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public UpdateClientException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
