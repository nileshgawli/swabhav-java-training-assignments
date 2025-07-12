package com.aurionpro.exception;

public class InsufficientFundsException extends Exception {
	private static final long serialVersionUID = 7106654624850128138L;

	public InsufficientFundsException(String message) {
		super(message);
	}
}