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

	Map<String, SeatHold> seatHolded = new HashMap<>();
	Map<String, SeatHold> seatReserved = new HashMap<>();
	EmailValidator emailValidator = EmailValidator.getInstance();
	Properties properties = new Properties();
	Long currentTimeinMillisec;

	public TicketServiceImplementation() {
		loadProperties();
		currentTimeinMillisec = System.currentTimeMillis();
	}

	private void loadProperties() {
		try {
			properties.load(TicketServiceImplementation.class.getClassLoader()
					.getResourceAsStream("ticket-service.properties"));
		} catch (IOException e) {
			throw new SeatHoldException("ticket-service.properties. The System will fail. ");
		}
	}

	private void clearHoldedSeats() {
		Long expiredTimestamp = currentTimeinMillisec + 10000L;
		if (expiredTimestamp < System.currentTimeMillis())
		{
			System.out.println("Clearing out holded objects");
		    seatHolded.clear();
		}
	}

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
