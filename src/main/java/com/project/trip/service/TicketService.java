package com.project.trip.service;

import java.util.List;

import com.project.trip.model.Ticket;

//Interface TicketService
public interface TicketService {
	Ticket saveTicket(Ticket ticket);
	List<Ticket> getAllTicket();
	Ticket getTicketById(Long id);
	List<Ticket> getAllTicketByPassengerId(Long passengerId);
	List<Ticket> getAllTicketByTripScheduleId(Long tripScheduleId);
	int getNumberOfBookedSeatsByPassengerId(Long passengerId, Long tripScheduleId);
	void deleteTicket(Long id);
}
