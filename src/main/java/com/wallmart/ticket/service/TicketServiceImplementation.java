package com.wallmart.ticket.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.validator.routines.EmailValidator;
import com.wallmart.ticket.exception.SeatHoldException;
import com.wallmart.ticket.model.SeatHold;

public class TicketServiceImplementation implements TicketService {

	private static final long DEFAULT_TIMEOUT = 10000;
	Map<String, SeatHold> seatHolded = new HashMap<>();
	Map<String, SeatHold> seatReserved = new HashMap<>();
	EmailValidator emailValidator = EmailValidator.getInstance();
	Properties properties = new Properties();
	Long currentTimeinMillisec;

	public TicketServiceImplementation() {
		loadProperties();
		currentTimeinMillisec = System.currentTimeMillis();
	}

	/**
	 * Loads properties from a config file
	 */
	private void loadProperties() {
		try {
			properties.load(TicketServiceImplementation.class.getClassLoader()
					.getResourceAsStream("ticket-service.properties"));
		} catch (IOException e) {
			throw new SeatHoldException("No ticket-service.properties configured. The System will fail. ");
		}
	}

	/**
	 * Clears the holded seat after a specicfic interval of time. The time interval
	 * can be controlled from a config file. If nothing is configured default time
	 * will be taken.
	 * 
	 */
	private void clearHoldedSeats() {

		long timeOut = DEFAULT_TIMEOUT;
		if (properties.get(TIMEOUT) != null) {
			timeOut = Long.valueOf((String) properties.get(TIMEOUT));
		}

		Long expiredTimestamp = currentTimeinMillisec + timeOut;
		if (expiredTimestamp < System.currentTimeMillis()) {
			System.out.println("Clearing out holded objects");
			seatHolded.clear();
		}
	}

	/**
	 * Implementation - The number of seats in the venue that are neither held nor
	 * reserved
	 * 
	 * @return the number of tickets available in the venue
	 */

	@Override
	public int numSeatsAvailable() {
		clearHoldedSeats();
		int numberOfSeatHolded = 0;
		int numberOfSeatReserved = 0;
		int numberofSeatsAvailable = 0;
		Iterator<Map.Entry<String, SeatHold>> itrSeatHold = seatHolded.entrySet().iterator();
		while (itrSeatHold.hasNext()) {
			Map.Entry<String, SeatHold> entry = itrSeatHold.next();
			numberOfSeatHolded += entry.getValue().getNumberOfSeatHolded();
		}
		Iterator<Map.Entry<String, SeatHold>> itrSeatReserved = seatReserved.entrySet().iterator();
		while (itrSeatReserved.hasNext()) {
			Map.Entry<String, SeatHold> entry = itrSeatReserved.next();
			numberOfSeatReserved += entry.getValue().getNumberOfSeatHolded();
		}
		int totalNumberOfEngagedSeats = numberOfSeatHolded + numberOfSeatReserved;
		numberofSeatsAvailable = Integer.valueOf((String) properties.get(SEAT_SIZE)) - totalNumberOfEngagedSeats;
		return numberofSeatsAvailable;
	}

	/**
	 * Implementation - Find and hold the best available seats for a customer
	 * 
	 * @param numSeats      the number of seats to find and hold
	 * @param customerEmail unique identifier for the customer
	 * @return a SeatHold object identifying the specific seats and related
	 *         information
	 */

	@Override
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		if (numSeats <= 0) {
			throw new SeatHoldException("Number of seats has to be atleast 1");
		}

		if (!emailValidator.isValid(customerEmail)) {
			throw new SeatHoldException("EmailId is not valid");
		}
		if (numSeatsAvailable() <= 0) {
			throw new SeatHoldException("Sorry, no seats are available");
		}
		if (seatHolded.containsKey(customerEmail)) {
			throw new SeatHoldException("Sorry, you are already holding a seat. Please give others a chance");
		}
		SeatHold seathold = new SeatHold(Double.valueOf((String) properties.get(SEAT_PRICE)), numSeats, customerEmail);
		seatHolded.put(customerEmail, seathold);
		return seathold;
	}

	/**
	 * 
	 * Implementation -Commit seats held for a specific customer
	 * 
	 * @param seatHoldId    the seat hold identifier
	 * @param customerEmail the email address of the customer to which the seat hold
	 *                      is assigned
	 * @return a reservation confirmation code
	 */

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {

		if (!emailValidator.isValid(customerEmail)) {
			throw new SeatHoldException("EmailId is not valid");
		}
		if (seatHolded.get(customerEmail) == null || seatHoldId != seatHolded.get(customerEmail).getSeatHoldId()) {
			throw new SeatHoldException("The Following seats are no longer holded by the Customer " + customerEmail);
		}
		seatReserved.put(customerEmail, seatHolded.get(customerEmail));
		seatHolded.remove(customerEmail);
		return seatReserved.get(customerEmail).toString();
	}

}
