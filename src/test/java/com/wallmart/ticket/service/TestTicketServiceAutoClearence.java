package com.wallmart.ticket.service;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.wallmart.ticket.model.SeatHold;

public class TestTicketServiceAutoClearence {

	
	@Test
	public void testTicketHoldAutomaticClearence() throws InterruptedException {
		TicketService ticketService = new TicketServiceImplementation();
		SeatHold seathold = ticketService.findAndHoldSeats(10, "rishu287@gmail.com");
		assertEquals(90, ticketService.numSeatsAvailable());
		ticketService.reserveSeats(seathold.getSeatHoldId(), "rishu287@gmail.com");
		seathold = ticketService.findAndHoldSeats(20, "rishu287@gmail.com");
		assertEquals(70, ticketService.numSeatsAvailable());
		ticketService.findAndHoldSeats(50, "popo@gmail.com");
		assertEquals(20, ticketService.numSeatsAvailable());
		ticketService.findAndHoldSeats(20, "helloo@gmail.com");
		assertEquals(0, ticketService.numSeatsAvailable());
		long currentTimeinMillisec = System.currentTimeMillis();
		System.out.println("Waiting to Clear of Holded seats ... Please have patience");
		while(currentTimeinMillisec + 20000L > System.currentTimeMillis())
		{
			// Just a loop to kill time
		}
		assertEquals(90, ticketService.numSeatsAvailable());
	}
	
	@Test
	public void testTicketHoldAutomaticClearenceWithNewThread() throws InterruptedException {
		TicketService ticketService = new TicketServiceImplementation();
		SeatHold seathold = ticketService.findAndHoldSeats(10, "rishu287@gmail.com");
		assertEquals(90, ticketService.numSeatsAvailable());
		ticketService.findAndHoldSeats(50, "popo@gmail.com");
		assertEquals(40, ticketService.numSeatsAvailable());
		ticketService.findAndHoldSeats(20, "helloo@gmail.com");
		assertEquals(20, ticketService.numSeatsAvailable());
		Thread t = new Thread();
		System.out.println("Waiting for Clearing of Holded seats ... Please have patience");
		t.sleep(20000L);
		assertEquals(100, ticketService.numSeatsAvailable());
	}
	
}
