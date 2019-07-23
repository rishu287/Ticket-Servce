package com.wallmart.ticket.model;

public class SeatHold {

	private static int count=0;
	private int seatHoldId;
	private double seatHoldPrice;
	private int numberOfSeatHolded;
	private String customerEmail;

	public SeatHold(double seatHoldPrice, int numberOfSeatHolded, String customerEmail) {
        this.seatHoldId=++count;
		this.seatHoldPrice = seatHoldPrice;
		this.numberOfSeatHolded = numberOfSeatHolded;
		this.customerEmail = customerEmail;
	}

	public int getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(int seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	public double getSeatHoldPrice() {
		return seatHoldPrice;
	}

	public void setSeatHoldPrice(double seatHoldPrice) {
		this.seatHoldPrice = seatHoldPrice;
	}

	public int getNumberOfSeatHolded() {
		return numberOfSeatHolded;
	}

	public void setNumberOfSeatHolded(int numberOfSeatHolded) {
		this.numberOfSeatHolded = numberOfSeatHolded;

	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

}
