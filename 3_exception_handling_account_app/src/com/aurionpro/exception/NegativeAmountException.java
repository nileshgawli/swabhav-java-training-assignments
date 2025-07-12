package com.aurionpro.exception;

public class NegativeAmountException extends Exception {
	private static final long serialVersionUID = -7735810248956859263L;

	public NegativeAmountException(String message) {
		super(message);
	}
}