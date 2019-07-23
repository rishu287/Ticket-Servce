package com.wallmart.ticket.exception;

public class SeatHoldException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SeatHoldException()
	{
		super();
	}
	
	public SeatHoldException(String message)
	{
		super(message);
	}
}
