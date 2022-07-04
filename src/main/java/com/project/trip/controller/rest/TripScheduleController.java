package com.project.trip.controller.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip.model.Trip;
import com.project.trip.model.TripSchedule;
import com.project.trip.payload.request.TripScheduleRequest;
import com.project.trip.payload.response.TripScheduleResponse;
import com.project.trip.payload.response.Response;
import com.project.trip.service.impl.TripScheduleServiceImpl;
import com.project.trip.service.impl.TripServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

//Controller untuk trip schedule
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/trip-schedule")
public class TripScheduleController {

	//Inisiasi tripScheduleServiceImpl
	@Autowired
	TripScheduleServiceImpl tripScheduleServiceImpl;

	//Inisiasi tripServiceImpl
	@Autowired
	TripServiceImpl tripServiceImpl;

	//Menambah trip schedule baru
	@PostMapping("")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createTripSchedule(@Valid @RequestBody TripScheduleRequest tripScheduleRequest) {
		
		//Ubah field sesuai tipe data di Java
		Long tripDetailId = tripScheduleRequest.getTripDetailId();

		//Jika ada field yang kosong atau 0, kembalikan bad request
		if(tripScheduleRequest.getTripDate() == null || tripDetailId == null || tripDetailId == 0) {
			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
			
		//Ambil tripDate
		LocalDate tripDate = tripScheduleRequest.getTripDate();
		
		//Get trip detail
		Trip tripDetail = tripServiceImpl.getTripById(tripDetailId);

		//Jika data trip detail tidak ditemukan, kembalikan bad request
		if(tripDetail.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data trip detail tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		//Get LocalDate hari ini
		LocalDate today = LocalDate.now();
		
		//Jika trip schedule sudah lewat, kembalikan bad request
		if(tripDate.isBefore(today)) {
			Response errorResponse = new Response("400", "Bad Request", "Trip date sudah lewat,"
					+ " silahkan masukkan trip date yang lain");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Ambil kapasitas bus, lalu gunakan sebagai availableSeats
		int availableSeats = tripDetail.getBus().getCapacity();
		
		//Create new trip schedule object
		TripSchedule tripSchedule = new TripSchedule(tripDate.toString(), availableSeats, tripDetail);
		
		//Simpan trip schedule baru ke database
		tripScheduleServiceImpl.saveTripSchedule(tripSchedule);
		
		//Kembalikan response
		Response successResponse = new Response("200", "OK", "TripSchedule berhasil disimpan");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}
	
	//Mengambil semua trip schedule yang tersimpan
	@GetMapping("")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getAllTripSchedule() {
		
		//Get all trip schedule
		List<TripSchedule> tripSchedules = tripScheduleServiceImpl.getAllTripSchedule();

		//Memeriksa apakah ada trip schedule yang tersimpan
		if(tripSchedules.size() != 0) {
			//Jika ada trip schedule yang tersimpan, kembalikan 200 ok beserta daftar trip schedule
			List<TripScheduleResponse> tripScheduleResponses = new ArrayList<>();
			
			for (TripSchedule tripSchedule : tripSchedules) {
				tripScheduleResponses.add(new TripScheduleResponse(tripSchedule.getId(),
						tripSchedule.getTripDate(), tripSchedule.getAvailableSeats(),
						tripSchedule.getTripDetail().getId()));
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(tripScheduleResponses);
		} else {
			//Jika tidak ada TripSchedule tersimpan, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Tidak ada trip schedule yang tersimpan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	//Mengambil satu trip schedule berdasarkan id
	@GetMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getTripScheduleById(@PathVariable Long id) {
		
		//Get trip schedule
		TripSchedule tripSchedule = tripScheduleServiceImpl.getTripScheduleById(id);
		
		//Periksa trip schedule
		if(tripSchedule.getId() != null) {
			//Jika trip schedule bukan object berisi null value, kembali response 200 ok beserta trip schedule
			TripScheduleResponse tripScheduleResponse = new TripScheduleResponse(tripSchedule.getId(),
					tripSchedule.getTripDate(), tripSchedule.getAvailableSeats(),
					tripSchedule.getTripDetail().getId());
			
			List<Integer> seatNumbers = tripScheduleServiceImpl.getAllSeatNumberBooked(id);
			System.out.print("BOOKED SEAT NUMBER: ");
			for(int number: seatNumbers) {
				System.out.print(number + ", ");
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(tripScheduleResponse);
		} else {
			//Jika tidak ada trip schedule dengan id tersebut, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data trip schedule tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	//Mengambil semua trip schedule dari suatu trip
	@GetMapping("/trip/{tripId}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getTripScheduleByTripId(@PathVariable(value = "tripId") Long tripId) {
		
		//Jika tripId tidak valid, kembalikan not found
		if(tripServiceImpl.getTripById(tripId).getId() != tripId) {
			Response errorResponse = new Response("404", "Not Found", "Data trip tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
			
		//Get all trip schedule berdasarkan trip id
		List<TripSchedule> tripSchedules = tripScheduleServiceImpl.getAllTripScheduleByTripId(tripId);
		
		//Cek apakah ada trip schedule dari trip id tersebut
		if(tripSchedules.size() != 0) {
			//Jika trip tersebut memiliki trip schedule, kembalikan ok beserta daftar trip schedule
			List<TripScheduleResponse> tripScheduleResponses = new ArrayList<>();
			
			for(TripSchedule tripSchedule: tripSchedules) {
				tripScheduleResponses.add(new TripScheduleResponse(tripSchedule.getId(),
						tripSchedule.getTripDate(), tripSchedule.getAvailableSeats(),
						tripSchedule.getTripDetail().getId()));
			}

			return ResponseEntity.status(HttpStatus.OK).body(tripScheduleResponses);
		} else {
			//Jika tidak ada trip schedule, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Tidak ada trip schedule dari trip tersebut");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
	
	//Mengubah salah satu TripSchedule berdasarkan id
	@PutMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateTripSchedule(@Valid @RequestBody TripScheduleRequest tripScheduleRequest,
			@PathVariable Long id) {
		
		//Ubah field sesuai tipe data di Java
		Long tripDetailId = tripScheduleRequest.getTripDetailId();

		//Coba mengambil data trip schedule yang exist
		TripSchedule tripSchedule = tripScheduleServiceImpl.getTripScheduleById(id);
		
		//Periksa trip schedule, jika hasilnya object trip schedule dengan null, kembalikan not found
		if(tripSchedule.getId() == null) {
			Response errorResponse = new Response("404", "Not Found", "Data trip schedule tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}

		//Jika ada field yang kosong atau 0, kembalikan bad request
		if(tripScheduleRequest.getTripDate() == null || tripDetailId == null || tripDetailId == 0) {
			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Ambil tripDate
		LocalDate tripDate = tripScheduleRequest.getTripDate();
		
		//Get trip detail
		Trip tripDetail = tripServiceImpl.getTripById(tripDetailId);
		
		//Jika data trip detail tidak ditemukan, kembalikan bad request
		if(tripDetail.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data trip detail tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		//Get LocalDate hari ini
		LocalDate today = LocalDate.now();
		
		//Jika trip schedule sudah lewat, kembalikan bad request
		if(tripDate.isBefore(today)) {
			Response errorResponse = new Response("400", "Bad Request", "Trip date sudah lewat, silahkan masukkan trip date yang lain");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Ambil kapasitas bus, lalu gunakan sebagai availableSeats
		int availableSeats = tripDetail.getBus().getCapacity();
		
		//Set kolom trip schedule
		tripSchedule.setTripDate(tripDate.toString());
		tripSchedule.setAvailableSeats(availableSeats);
		tripSchedule.setTripDetail(tripDetail);
		
		//Simpan trip schedule baru ke database
		tripScheduleServiceImpl.saveTripSchedule(tripSchedule);
		
		//Kembalikan response
		Response successResponse = new Response("200", "OK", "Trip schedule berhasil diubah");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}

	//Menghapus satu trip schedule berdasarkan id
	@DeleteMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteTripSchedule(@PathVariable Long id) {
		//Memeriksa apakah trip schedule dengan id tersebut ada
		try {
			tripScheduleServiceImpl.deleteTripSchedule(id);
			
			//Jika ada trip schedule dengan id tersebut, kembalikan response ok
			Response successResponse = new Response("200", "OK", "Trip schedule berhasil dihapus");
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		} catch (EmptyResultDataAccessException e) {
			//Jika trip schedule dengan id tersebut tidak ada, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data trip schedule tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
}