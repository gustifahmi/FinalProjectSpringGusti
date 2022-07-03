package com.project.trip.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip.model.Ticket;
import com.project.trip.payload.response.Response;
import com.project.trip.payload.response.TicketResponse;
import com.project.trip.service.impl.TicketServiceImpl;
import com.project.trip.service.impl.TripScheduleServiceImpl;
import com.project.trip.service.impl.UserServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

//Controller untuk ticket
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/ticket")
public class TicketController {

	//Inisiasi ticketServiceImpl
	@Autowired
	TicketServiceImpl ticketServiceImpl;

	//Inisiasi userServiceImpl
	@Autowired
	UserServiceImpl userServiceImpl;

	//Inisiasi tripScheduleServiceImpl
	@Autowired
	TripScheduleServiceImpl tripScheduleServiceImpl;
	
	//Mengambil semua ticket yang tersimpan
	@GetMapping("")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllTicket() {
		
		//Get all ticket
		List<Ticket> tickets = ticketServiceImpl.getAllTicket();

		//Memeriksa apakah ada ticket yang tersimpan
		if(tickets.size() != 0) {
			//Jika ada ticket yang tersimpan, kembalikan 200 ok beserta daftar ticket
			List<TicketResponse> ticketResponses = new ArrayList<>();
			
			for (Ticket ticket : tickets) {
				ticketResponses.add(new TicketResponse(ticket.getId(), ticket.getSeatNumber(),
						ticket.getCancellable(), ticket.getJourneyDate(), ticket.getPassenger().getId(),
						ticket.getTripSchedule().getId()));
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(ticketResponses);
		} else {
			//Jika tidak ada ticket tersimpan, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Tidak ada ticket yang tersimpan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	//Mengambil satu ticket berdasarkan id
	@GetMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getTicketById(@PathVariable Long id) {
		
		//Get ticket
		Ticket ticket = ticketServiceImpl.getTicketById(id);
		
		//Periksa ticket
		if(ticket.getId() != null) {
			//Jika ticket bukan object berisi null value, kembali response 200 ok beserta ticket
			TicketResponse ticketResponse = new TicketResponse(ticket.getId(), ticket.getSeatNumber(),
					ticket.getCancellable(), ticket.getJourneyDate(),
					ticket.getPassenger().getId(), ticket.getTripSchedule().getId());
		
			return ResponseEntity.status(HttpStatus.OK).body(ticketResponse);
		} else {
			//Jika tidak ada ticket dengan id tersebut, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data ticket tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	//Mengambil semua ticket yang sudah dipesan dari suatu trip schedule
	@GetMapping("/trip-schedule/{tripScheduleId}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllTicketByTripScheduleId(@PathVariable Long tripScheduleId) {
		
		//Get all ticket
		List<Ticket> tickets = ticketServiceImpl.getAllTicketByTripScheduleId(tripScheduleId);

		//Memeriksa apakah ada ticket yang tersimpan
		if(tickets.size() != 0) {
			//Jika ada ticket yang tersimpan, kembalikan 200 ok beserta daftar ticket
			List<TicketResponse> ticketResponses = new ArrayList<>();
			
			for (Ticket ticket : tickets) {
				ticketResponses.add(new TicketResponse(ticket.getId(), ticket.getSeatNumber(), ticket.getCancellable(),
						ticket.getJourneyDate(), ticket.getPassenger().getId(),
						ticket.getTripSchedule().getId()));
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(ticketResponses);
		} else {
			//Jika tidak ada ticket tersimpan, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Tidak ada ticket yang dipesan dari trip schedule tersebut");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
}
