package com.project.trip.controller.rest;

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

import com.project.trip.model.Agency;
import com.project.trip.model.Bus;
import com.project.trip.model.Stop;
import com.project.trip.model.Trip;
import com.project.trip.payload.request.TripRequest;
import com.project.trip.payload.response.TripResponse;
import com.project.trip.payload.response.Response;
import com.project.trip.service.impl.AgencyServiceImpl;
import com.project.trip.service.impl.BusServiceImpl;
import com.project.trip.service.impl.StopServiceImpl;
import com.project.trip.service.impl.TripServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

//Controller untuk trip
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/trip")
public class TripController {

	//Inisiasi tripServiceImpl
	@Autowired
	TripServiceImpl tripServiceImpl;

	//Inisiasi stopServiceImpl
	@Autowired
	StopServiceImpl stopServiceImpl;

	//Inisiasi busServiceImpl
	@Autowired
	BusServiceImpl busServiceImpl;

	//Inisiasi agencyServiceImpl
	@Autowired
	AgencyServiceImpl agencyServiceImpl;

	//Menambah trip baru
	@PostMapping("")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createTrip(@Valid @RequestBody TripRequest tripRequest) {
		
		//Ubah field sesuai tipe data di Java
		int fare = tripRequest.getFare();
		int journeyTime = tripRequest.getJourneyTime();
		Long sourceStopId = tripRequest.getSourceStopId();
		Long destStopId = tripRequest.getDestStopId();
		Long busId = tripRequest.getBusId();

		//Jika ada field yang kosong atau 0, kembalikan bad request
		if(fare == 0 || journeyTime == 0 || sourceStopId == null || destStopId == null
				|| busId == null || sourceStopId == 0 || destStopId == 0 || busId == 0) {

			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		//Get bus
		Bus bus = busServiceImpl.getBusById(busId);
		
		//Jika data bus tidak ditemukan, kembalikan bad request
		if(bus.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data bus tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Get agency
		Agency agency = bus.getAgency();
		
		//Jika data agency tidak ditemukan, kembalikan bad request
		if(agency.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data agency tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		//Get destStop
		Stop destStop = stopServiceImpl.getStopById(destStopId);
		
		//Jika data destStop tidak ditemukan, kembalikan bad request
		if(destStop.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data destination stop tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Get sourceStop
		Stop sourceStop = stopServiceImpl.getStopById(sourceStopId);
		
		//Jika data sourceStop tidak ditemukan, kembalikan bad request
		if(sourceStop.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data source stop tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		//Jika source stop dan dest stop sama, kembalikan bad request
		if(sourceStop.getId() == destStop.getId()) {
			Response errorResponse = new Response("400", "Bad Request", "Field sourceStopId dan destStopId tidak boleh sama");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Create new trip object
		Trip trip = new Trip(fare, journeyTime, sourceStop, destStop, bus, agency);
		
		//Simpan trip baru ke database
		tripServiceImpl.saveTrip(trip);
		
		//Kembalikan response
		Response successResponse = new Response("200", "OK", "Trip berhasil disimpan");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}
	
	//Mengambil semua trip yang tersimpan
	@GetMapping("")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getAllTrip() {
		
		//Get all trip
		List<Trip> trips = tripServiceImpl.getAllTrip();

		//Memeriksa apakah ada trip yang tersimpan
		if(trips.size() != 0) {
			//Jika ada trip yang tersimpan, kembalikan 200 ok beserta daftar trip
			List<TripResponse> tripResponses = new ArrayList<>();
			
			for (Trip trip : trips) {
				tripResponses.add(new TripResponse(trip.getId(), trip.getFare(), trip.getJourneyTime(),
						trip.getSourceStop().getId(), trip.getDestStop().getId(),
						trip.getBus().getId(), trip.getAgency().getId()));
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(tripResponses);
		} else {
			//Jika tidak ada trip tersimpan, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Tidak ada trip yang tersimpan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	//Mengambil satu trip berdasarkan id
	@GetMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getTripById(@PathVariable Long id) {
		
		//Get trip
		Trip trip = tripServiceImpl.getTripById(id);
		
		//Periksa trip
		if(trip.getId() != null) {
			//Jika trip bukan object berisi null value, kembali response 200 ok beserta trip
			TripResponse tripResponse = new TripResponse(trip.getId(), trip.getFare(), trip.getJourneyTime(),
					trip.getSourceStop().getId(), trip.getDestStop().getId(),
					trip.getBus().getId(), trip.getAgency().getId());
		
			return ResponseEntity.status(HttpStatus.OK).body(tripResponse);
		} else {
			//Jika tidak ada Trip dengan id tersebut, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data trip tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	//Mengambil semua trip yang dimiliki suatu agency
	@GetMapping("/agency/{agencyId}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getTripByAgencyId(@PathVariable(value = "agencyId") Long agencyId) {
		
		//Jika agencyId tidak valid, kembalikan not found
		if(agencyServiceImpl.getAgencyById(agencyId).getId() != agencyId) {
			Response errorResponse = new Response("404", "Not Found", "Data agency tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
			
		//Get all trip berdasarkan agency id
		List<Trip> trips = tripServiceImpl.getAllTripByAgencyId(agencyId);
		
		//Cek apakah ada trip dari agency id tersebut
		if(trips.size() != 0) {
			//Jika agency tersebut memiliki trip, kembalikan ok beserta daftar trip
			List<TripResponse> tripResponses = new ArrayList<>();
			
			for(Trip trip: trips) {
				tripResponses.add(new TripResponse(trip.getId(), trip.getFare(), trip.getJourneyTime(),
						trip.getSourceStop().getId(), trip.getDestStop().getId(),
						trip.getBus().getId(), trip.getAgency().getId()));
			}

			return ResponseEntity.status(HttpStatus.OK).body(tripResponses);
		} else {
			//Jika tidak ada trip, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Agency tidak memiliki trip");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	//Mengambil semua trip yang dimiliki suatu bus
	@GetMapping("/bus/{busId}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getTripByBusId(@PathVariable(value = "busId") Long busId) {
		
		//Jika busId tidak valid, kembalikan not found
		if(busServiceImpl.getBusById(busId).getId() != busId) {
			Response errorResponse = new Response("404", "Not Found", "Data bus tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
			
		//Get all trip berdasarkan bus id
		List<Trip> trips = tripServiceImpl.getAllTripByBusId(busId);
		
		//Cek apakah ada trip dari bus id tersebut
		if(trips.size() != 0) {
			//Jika bus tersebut memiliki trip, kembalikan ok beserta daftar trip
			List<TripResponse> tripResponses = new ArrayList<>();
			
			for(Trip trip: trips) {
				tripResponses.add(new TripResponse(trip.getId(), trip.getFare(), trip.getJourneyTime(),
						trip.getSourceStop().getId(), trip.getDestStop().getId(),
						trip.getBus().getId(), trip.getAgency().getId()));
			}

			return ResponseEntity.status(HttpStatus.OK).body(tripResponses);
		} else {
			//Jika tidak ada trip, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Bus tidak memiliki trip");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	//Mengambil semua trip yang berangkat dari stop yang sama
	@GetMapping("/source-stop/{sourceStopId}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getTripBySourceStopId(@PathVariable(value = "sourceStopId") Long sourceStopId) {
		
		//Jika sourceStopId tidak valid, kembalikan not found
		if(stopServiceImpl.getStopById(sourceStopId).getId() != sourceStopId) {
			Response errorResponse = new Response("404", "Not Found", "Data stop tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
			
		//Get all trip berdasarkan source stop id
		List<Trip> trips = tripServiceImpl.getAllTripBySourceStopId(sourceStopId);
		
		//Cek apakah ada trip dari source stop id tersebut
		if(trips.size() != 0) {
			//Jika source stop tersebut memiliki trip, kembalikan ok beserta daftar trip
			List<TripResponse> tripResponses = new ArrayList<>();
			
			for(Trip trip: trips) {
				tripResponses.add(new TripResponse(trip.getId(), trip.getFare(), trip.getJourneyTime(),
						trip.getSourceStop().getId(), trip.getDestStop().getId(),
						trip.getBus().getId(), trip.getAgency().getId()));
			}

			return ResponseEntity.status(HttpStatus.OK).body(tripResponses);
		} else {
			//Jika tidak ada trip, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Tidak ada trip yang berangkat dari stop tersebut");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
	
	//Mengambil semua trip yang bertujuan ke stop yang sama
	@GetMapping("/dest-stop/{destStopId}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getTripByDestStopId(@PathVariable(value = "destStopId") Long destStopId) {
		
		//Jika destStopId tidak valid, kembalikan not found
		if(stopServiceImpl.getStopById(destStopId).getId() != destStopId) {
			Response errorResponse = new Response("404", "Not Found", "Data stop tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
			
		//Get all trip berdasarkan dest stop id
		List<Trip> trips = tripServiceImpl.getAllTripByDestStopId(destStopId);
		
		//Cek apakah ada trip dari bus id tersebut
		if(trips.size() != 0) {
			//Jika dest stop tersebut memiliki trip, kembalikan ok beserta daftar trip
			List<TripResponse> tripResponses = new ArrayList<>();
			
			for(Trip trip: trips) {
				tripResponses.add(new TripResponse(trip.getId(), trip.getFare(), trip.getJourneyTime(),
						trip.getSourceStop().getId(), trip.getDestStop().getId(),
						trip.getBus().getId(), trip.getAgency().getId()));
			}

			return ResponseEntity.status(HttpStatus.OK).body(tripResponses);
		} else {
			//Jika tidak ada trip, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Tidak ada trip yang bertujuan ke stop tersebut");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
	
	//Mengubah salah satu trip berdasarkan id
	@PutMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateTrip(@Valid @RequestBody TripRequest tripRequest, @PathVariable Long id) {

		//Ubah field sesuai tipe data di Java
		int fare = tripRequest.getFare();
		int journeyTime = tripRequest.getJourneyTime();
		Long sourceStopId = tripRequest.getSourceStopId();
		Long destStopId = tripRequest.getDestStopId();
		Long busId = tripRequest.getBusId();
		
		//Coba mengambil data trip yang exist
		Trip trip = tripServiceImpl.getTripById(id);
		
		//Periksa trip, jika hasilnya object trip dengan null, kembalikan not found
		if(trip.getId() == null) {
			Response errorResponse = new Response("404", "Not Found", "Data trip tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}

		//Jika ada field yang kosong atau 0, kembalikan bad request
		if(fare == 0 || journeyTime == 0 || sourceStopId == null || destStopId == null
				|| busId == null || sourceStopId == 0 || destStopId == 0 || busId == 0) {

			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		//Get bus
		Bus bus = busServiceImpl.getBusById(busId);
		
		//Jika data bus tidak ditemukan, kembalikan bad request
		if(bus.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data bus tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		//Get agency
		Agency agency = bus.getAgency();
		
		//Get destStop
		Stop destStop = stopServiceImpl.getStopById(destStopId);
		
		//Jika data destStop tidak ditemukan, kembalikan bad request
		if(destStop.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data destination stop tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Get sourceStop
		Stop sourceStop = stopServiceImpl.getStopById(sourceStopId);
		
		//Jika data sourceStop tidak ditemukan, kembalikan bad request
		if(sourceStop.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data source stop tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Jika source stop dan dest stop sama, kembalikan bad request
		if(sourceStop.getId() == destStop.getId()) {
			Response errorResponse = new Response("400", "Bad Request", "Field sourceStopId dan destStopId tidak boleh sama");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Set kolom trip
		trip.setFare(fare);
		trip.setJourneyTime(journeyTime);
		trip.setSourceStop(sourceStop);
		trip.setDestStop(destStop);
		trip.setBus(bus);
		trip.setAgency(agency);
		
		//Simpan trip baru ke database
		tripServiceImpl.saveTrip(trip);
		
		//Kembalikan response
		Response successResponse = new Response("200", "OK", "Trip berhasil diubah");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}

	//Menghapus satu trip berdasarkan id
	@DeleteMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteTrip(@PathVariable Long id) {
		//Memeriksa apakah trip dengan id tersebut ada
		try {
			tripServiceImpl.deleteTrip(id);
			
			//Jika ada T=trip dengan id tersebut, kembalikan response ok
			Response successResponse = new Response("200", "OK", "Trip berhasil dihapus");
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		} catch (EmptyResultDataAccessException e) {
			//Jika Trip dengan id tersebut tidak ada, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data trip tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
}