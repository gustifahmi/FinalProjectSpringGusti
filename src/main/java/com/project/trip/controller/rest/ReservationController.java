package com.project.trip.controller.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip.model.Ticket;
import com.project.trip.model.TripSchedule;
import com.project.trip.model.User;
import com.project.trip.payload.request.ReservationRequest;
import com.project.trip.payload.response.AvailableSeatNumbersResponse;
import com.project.trip.payload.response.Response;
import com.project.trip.payload.response.TicketResponse;
import com.project.trip.service.impl.TicketServiceImpl;
import com.project.trip.service.impl.TripScheduleServiceImpl;
import com.project.trip.service.impl.UserServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

//Controller untuk reservasi ticket yang dapat dilakukan oleh user
//Catatan: Untuk beli dan batalkan ticket, harus menggunakan akun user
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

	// Inisiasi ticketServiceImpl
	@Autowired
	TicketServiceImpl ticketServiceImpl;

	// Inisiasi userServiceImpl
	@Autowired
	UserServiceImpl userServiceImpl;

	// Inisiasi tripScheduleServiceImpl
	@Autowired
	TripScheduleServiceImpl tripScheduleServiceImpl;

	// Membeli tiket
	@PostMapping("")
	@ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> bookTicket(@Valid @RequestBody ReservationRequest reservationRequest) {
	
		// Ubah field sesuai tipe data di Java
		int seatNumber = reservationRequest.getSeatNumber();
		Long tripScheduleId = reservationRequest.getTripScheduleId();

		// Mengambil passengerId dari user yang memesan
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		User user = userServiceImpl.getUserByUsername(username);
		Long passengerId = user.getId();

		//Jika ada field yang kosong atau 0, kembalikan bad request
		if (seatNumber == 0 || passengerId == null || tripScheduleId == null
				|| passengerId == 0 || tripScheduleId == 0 || reservationRequest.getCancellable() == null) {

			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		//Get cancellable
		boolean cancellable = reservationRequest.getCancellable();
		
		// Get trip schedule
		TripSchedule schedule = tripScheduleServiceImpl.getTripScheduleById(tripScheduleId);

		// Jika tripSchedule tidak ditemukan, kembalikan bad request
		if (schedule.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data trip schedule tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		//Get journey date berdasarkan column trip date milik trip schedule
		String journeyDate = schedule.getTripDate();
		
		// Get passenger
		User passenger = userServiceImpl.getUserById(passengerId);

		//Get LocalDate hari ini
		LocalDate today = LocalDate.now();
		
		//Parse kolom trip_date pada objek TripSchedule ke tipe LocalDate
		String tripDateString = tripScheduleServiceImpl.getTripScheduleById(tripScheduleId).getTripDate();
		LocalDate tripDate = LocalDate.parse(tripDateString);
		
		//Jika trip schedule sudah lewat, kembalikan bad request
		if(tripDate.isBefore(today)) {
			Response errorResponse = new Response("400", "Bad Request", "Trip schedule sudah lewat, silahkan pilih trip schedule yang lain");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		// Jika passenger tidak ditemukan, kembalikan bad request
		if (passenger.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data passenger tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		// Cek available seats
		int availableSeats = schedule.getAvailableSeats();
		if (availableSeats == 0) {
			Response errorResponse = new Response("400", "Bad Request", "Mohon maaf, tiket sudah habis");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		// Mengecek apakah user sudah memesan ticket di trip schedule ini
		int bookedSeatsByPassenger = ticketServiceImpl.getNumberOfBookedSeatsByPassengerId(passengerId, tripScheduleId);

		// Kalau sudah memesan, tidak bisa memesan lagi di trip schedule yang sama
		if (bookedSeatsByPassenger > 0) {
			String errorDetails = "Mohon maaf, anda tidak dapat memesan ticket lagi untuk trip schedule ini";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		// Cek kapasitas bus
		int capacity = schedule.getTripDetail().getBus().getCapacity();

		// Jika seat number tidak valid
		if (seatNumber > capacity) {
			String errorDetails = "Seat number tidak valid. Kapasitas bus hanya " + capacity + " kursi";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		// Get list seat number yang sudah dipesan
		List<Integer> numberBooked = tripScheduleServiceImpl.getAllSeatNumberBooked(tripScheduleId);

		// Jika seat number sudah dipesan, kembalikan bad request
		if (numberBooked.contains(seatNumber)) {
			String errorDetails = "Seat number sudah dipesan, silahkan pilih seat number lain";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		// Create new ticket object
		Ticket ticket = new Ticket(seatNumber, cancellable, journeyDate, schedule, passenger);

		// Simpan ticket baru ke database
		ticketServiceImpl.saveTicket(ticket);

		// Kurangi availableSeats
		schedule.setAvailableSeats(availableSeats - 1);
		tripScheduleServiceImpl.saveTripSchedule(schedule);

		// Kembalikan response
		Response successResponse = new Response("200", "OK", "Ticket berhasil dipesan");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}

	// Mengambil semua ticket yang dimiliki oleh user yang login
	@GetMapping("/passenger")
	@ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAllOwnedTicketByLoggedInUser() {

		// Mengambil passengerId dari user yang memesan
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		User user = userServiceImpl.getUserByUsername(username);
		Long passengerId = user.getId();

		// Get all ticket
		List<Ticket> tickets = ticketServiceImpl.getAllTicketByPassengerId(passengerId);

		// Memeriksa apakah ada ticket yang tersimpan
		if (tickets.size() != 0) {
			// Jika ada ticket yang tersimpan, kembalikan 200 ok beserta daftar ticket
			List<TicketResponse> ticketResponses = new ArrayList<>();

			for (Ticket ticket : tickets) {
				ticketResponses.add(new TicketResponse(ticket.getId(), ticket.getSeatNumber(), ticket.getCancellable(),
						ticket.getJourneyDate(), ticket.getPassenger().getId(), ticket.getTripSchedule().getId()));
			}

			return ResponseEntity.status(HttpStatus.OK).body(ticketResponses);
		} else {
			// Jika tidak ada ticket tersimpan, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Anda belum memiliki ticket yang tersimpan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	// Mengambil satu ticket berdasarkan id, tapi hanya bisa diakses oleh pemilik ticket
	@GetMapping("/{id}")
	@ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getTicketById(@PathVariable Long id) {

		// Get ticket
		Ticket ticket = ticketServiceImpl.getTicketById(id);

		// Jika tidak ada ticket dengan id tersebut, kembalikan not found
		if (ticket.getId() == null) {
			// Jika tidak ada ticket dengan id tersebut, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data ticket tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}

		// Mengambil passengerId dari user yang memesan
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		User user = userServiceImpl.getUserByUsername(username);
		Long passengerId = user.getId();

		//Jika ticket bukan milik user yang login, kembalikan bad request
		if(ticket.getPassenger().getId() != passengerId) {
			Response errorResponse = new Response("400", "Bad Request", "Anda tidak dapat melihat ticket milik penumpang lain");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Buat objek response
		TicketResponse ticketResponse = new TicketResponse(ticket.getId(), ticket.getSeatNumber(),
				ticket.getCancellable(), ticket.getJourneyDate(), ticket.getPassenger().getId(),
				ticket.getTripSchedule().getId());

		//Kembalikan response
		return ResponseEntity.status(HttpStatus.OK).body(ticketResponse);
	}

	// Mengambil semua ticket yang dimiliki oleh user yang login
	@GetMapping("/available-seat-number/{tripScheduleId}")
	@ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAvailableSeatNumberByTripScheduleId(@PathVariable Long tripScheduleId) {
		
		//Get trip schedule
		TripSchedule schedule = tripScheduleServiceImpl.getTripScheduleById(tripScheduleId);

		// Jika tidak ada ticket dengan id tersebut, kembalikan not found
		if (schedule.getId() == null) {
			// Jika tidak ada tripSchedule dengan id tersebut, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data trip schedule tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		
		//Get daftar seat number yang sudah dibook di suatu trip schedule
		List<Integer> bookedSeatNumbers = tripScheduleServiceImpl.getAllSeatNumberBooked(tripScheduleId);
		
		//Get kapasitas bus
		int capacity = schedule.getTripDetail().getBus().getCapacity();
		
		//Inisiasi availableSeatNumbers
		List<Integer> availableSeatNumbers = new ArrayList<>();
		
		//Cari seat number yang available masukkan ke list availableSeatNumbers
		for(int i = 1; i <= capacity; i++) {
			if(!bookedSeatNumbers.contains(i)) {
				availableSeatNumbers.add(i);
			}
		}
		
		//Kembalikan response
		AvailableSeatNumbersResponse availableSeatNumbersResponse = 
				new AvailableSeatNumbersResponse(tripScheduleId, availableSeatNumbers);
		return ResponseEntity.status(HttpStatus.OK).body(availableSeatNumbersResponse);
	}

	// Menghapus satu ticket berdasarkan id
	@DeleteMapping("/{id}")
	@ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> cancelTicket(@PathVariable Long id) {

		// Coba get ticket
		Ticket ticket = ticketServiceImpl.getTicketById(id);

		// Jika tiket merupakan objek kosong, kembalikan bad request
		if (ticket.getId() == null) {
			// Jika ticket dengan id tersebut tidak ada, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data ticket tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}

		// Mengambil passengerId dari user yang memesan
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		User user = userServiceImpl.getUserByUsername(username);
		Long passengerId = user.getId();
		
		// Jika ticket bukan milik user yang login
		if (passengerId != ticket.getPassenger().getId()) {
			// Jika ticket bukan milik user yang login, gagal membatalkan ticket
			Response errorResponse = new Response("400", "Bad Request",
					"Ticket hanya dapat dibatalkan oleh pemilik ticket");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		if (ticket.getCancellable() == false) {
			// Jika cancellable false, kembalikan bad request
			Response errorResponse = new Response("400", "Bad Request", "Mohon maaf, ticket tidak dapat dibatalkan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		// Get trip schedule
		TripSchedule schedule = ticket.getTripSchedule();
		
		// Delete ticket
		ticketServiceImpl.deleteTicket(id);

		//Tambahkan available seats
		schedule.setAvailableSeats(schedule.getAvailableSeats() + 1);
		tripScheduleServiceImpl.saveTripSchedule(schedule);
		
		// Kembalikan response
		Response successResponse = new Response("200", "OK", "Ticket berhasil dibatalkan");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}
}
