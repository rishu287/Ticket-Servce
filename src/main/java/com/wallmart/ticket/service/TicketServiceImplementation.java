package com.wallmart.ticket.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.validator.routines.EmailValidator;
import com.wallmart.ticket.exception.SeatHoldException;
import com.wallmart.ticket.model.SeatHold;

public class TicketServiceImplementation implements TicketService {

	Map<String, SeatHold> seatHolded = new HashMap<>();
	Map<String, SeatHold> seatReserved = new HashMap<>();
	EmailValidator emailValidator = EmailValidator.getInstance();

	public int numSeatsAvailable() {
		int numberOfSeatHolded = 0;
		int numberOfSeatReserved = 0;
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
		return SEAT_SIZE - totalNumberOfEngagedSeats;
	}

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
		if(seatHolded.containsKey(customerEmail))
		{
			throw new SeatHoldException("Sorry, you are already holding a seat. Please give others a chance");
		}
		SeatHold seathold = new SeatHold(SEAT_PRICE, numSeats, customerEmail);
		seatHolded.put(customerEmail, seathold);
		return seathold;
	}

	public String reserveSeats(int seatHoldId, String customerEmail) {

		if (!emailValidator.isValid(customerEmail)) {
			throw new SeatHoldException("EmailId is not valid");
		}
		if (seatHoldId != seatHolded.get(customerEmail).getSeatHoldId()) {
			throw new SeatHoldException("The Following seats are no longer holded by the Customer " + customerEmail);
		}
		seatReserved.put(customerEmail, seatHolded.get(customerEmail));
		seatHolded.remove(customerEmail);
		return seatReserved.get(customerEmail).toString();
	}

	class ClearHoldedSeats extends Thread {
		@Override
		public void run() {
			while (true) {
				seatHolded.clear();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
