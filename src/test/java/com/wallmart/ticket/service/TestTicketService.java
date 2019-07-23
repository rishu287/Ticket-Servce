package com.wallmart.ticket.service;

import static org.junit.Assert.*;
import org.junit.Test;
import com.wallmart.ticket.exception.SeatHoldException;
import com.wallmart.ticket.model.SeatHold;

public class TestTicketService {

	
	
	@Test
	public void testFindAndHoldSeats()  {
		TicketService ticketService = new TicketServiceImplementation();
		ticketService.findAndHoldSeats(10, "rishu287@gmail.com");
		assertEquals(90, ticketService.numSeatsAvailable());
		ticketService.findAndHoldSeats(25, "ggo@gmail.com");
		assertEquals(65, ticketService.numSeatsAvailable());
		ticketService.findAndHoldSeats(55, "ppjo@gmail.com");
		assertEquals(10, ticketService.numSeatsAvailable());
	}

	
	@Test(expected = SeatHoldException.class)
	public void testFindAndHoldSeatsDuplicateCustomer()  {
		TicketService ticketService = new TicketServiceImplementation();
		ticketService.findAndHoldSeats(5, "rishu287@gmail.com");
		ticketService.findAndHoldSeats(5, "rishu287@gmail.com");
	}
	
	
	@Test(expected = SeatHoldException.class)
	public void testFindAndHoldSeatsInvalidEmail()  {
		TicketService ticketService = new TicketServiceImplementation();
		ticketService.findAndHoldSeats(5, "ppp-.com");
	}
	
	@Test(expected = SeatHoldException.class)
	public void testFindAndHoldSeatsIncorrectSeatReservation()  {
		TicketService ticketService = new TicketServiceImplementation();
		ticketService.findAndHoldSeats(0, "rishu287@gmail.com");
	}
	
	@Test
	public void testAndReserveSeat()  {
		TicketService ticketService = new TicketServiceImplementation();
		SeatHold seathold = ticketService.findAndHoldSeats(10, "rishu287@gmail.com");
		assertEquals(90, ticketService.numSeatsAvailable());
		String confirmation=ticketService.reserveSeats(seathold.getSeatHoldId(), "rishu287@gmail.com");
		assertEquals(seathold.toString(), confirmation);
	}
	
}
