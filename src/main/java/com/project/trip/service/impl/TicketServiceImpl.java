package com.project.trip.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.model.Ticket;
import com.project.trip.repository.TicketRepository;
import com.project.trip.service.TicketService;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {
	
	@Autowired
	private TicketRepository ticketRepository;

	public Ticket saveTicket(Ticket ticket) {
		ticketRepository.save(ticket);
		return ticket;
	}

	public List<Ticket> getAllTicket() {
		return ticketRepository.findAll();
	}

	public Ticket getTicketById(Long id) {
		return ticketRepository.findById(id).orElse(new Ticket());
	}

	public List<Ticket> getAllTicketByPassengerId(Long passengerId) {
		return ticketRepository.findByPassengerId(passengerId);
	}

	public List<Ticket> getAllTicketByTripScheduleId(Long tripScheduleId) {
		return ticketRepository.findByTripScheduleId(tripScheduleId);
	}

	public int getNumberOfBookedSeatsByPassengerId(Long passengerId, Long tripScheduleId) {
		return ticketRepository.numberOfBookedSeatsByPassengerId(passengerId, tripScheduleId);
	}
	
	public void deleteTicket(Long id) {
		ticketRepository.deleteById(id);
	}
}
