package com.project.trip.controller;

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
import com.project.trip.payload.request.BusRequest;
import com.project.trip.payload.response.BusResponse;
import com.project.trip.payload.response.Response;
import com.project.trip.service.impl.AgencyServiceImpl;
import com.project.trip.service.impl.BusServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

//Controller untuk bus
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/bus")
public class BusController {

	//Inisiasi busServiceImpl
	@Autowired
	BusServiceImpl busServiceImpl;

	//Inisiasi agencyServiceImpl
	@Autowired
	AgencyServiceImpl agencyServiceImpl;

	//Menambah bus baru
	@PostMapping("")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createBus(@Valid @RequestBody BusRequest busRequest) {
		
		//Ubah field sesuai tipe data di Java
		String code = busRequest.getCode();
		int capacity = busRequest.getCapacity();
		String make = busRequest.getMake();
		Long agencyId = busRequest.getAgencyId();

		//Jika ada field yang kosong atau 0, kembalikan bad request
		if(code == null || capacity == 0 || make == null || agencyId == null
				|| code.equals("") || make.equals("") || agencyId == 0) {

			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Get agency
		Agency agency = agencyServiceImpl.getAgencyById(agencyId);
		
		//Jika data agency tidak ditemukan, kembalikan bad request
		if(agency.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data agency tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Create new bus object
		Bus bus = new Bus(code, capacity, make, agency);
		
		//Simpan bus baru ke database
		busServiceImpl.saveBus(bus);
		
		//Kembalikan response
		Response successResponse = new Response("200", "OK", "Bus berhasil disimpan");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}
	
	//Mengambil semua bus yang tersimpan
	@GetMapping("")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
	public ResponseEntity<?> getAllBus() {

		//Get all bus
		List<Bus> buses = busServiceImpl.getAllBus();

		//Memeriksa apakah ada bus yang tersimpan
		if(buses.size() != 0) {
			//Jika ada bus yang tersimpan, kembalikan 200 ok beserta daftar bus
			List<BusResponse> busResponses = new ArrayList<>();
			
			for (Bus bus : buses) {
				busResponses.add(new BusResponse(bus.getId(), bus.getCode(), bus.getCapacity(),
						bus.getMake(), bus.getAgency().getId()));
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(busResponses);
		} else {
			//Jika tidak ada bus tersimpan, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Tidak ada bus yang tersimpan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	//Mengambil satu bus berdasarkan id
	@GetMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getBusById(@PathVariable Long id) {
		
		//Get bus
		Bus bus = busServiceImpl.getBusById(id);
		
		//Periksa bus
		if(bus.getId() != null) {
			//Jika bus bukan object berisi null value, kembali response 200 ok beserta bus
			BusResponse busResponse = new BusResponse(bus.getId(), bus.getCode(),
					bus.getCapacity(), bus.getMake(), bus.getAgency().getId());
		
			return ResponseEntity.status(HttpStatus.OK).body(busResponse);
		} else {
			//Jika tidak ada bus dengan id tersebut, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data bus tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
	
	//Mengambil semua bus yang dimiliki suatu agency
	@GetMapping("/agency/{agencyId}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getBusByAgencyId(@PathVariable(value = "agencyId") Long agencyId) {
		
		//Jika agencyId tidak valid, kembalikan not found
		if(agencyServiceImpl.getAgencyById(agencyId).getId() != agencyId) {
			Response errorResponse = new Response("404", "Not Found", "Data agency tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
			
		//Get all bus berdasarkan agency id
		List<Bus> buses = busServiceImpl.getAllBusByAgencyId(agencyId);
		
		//Cek apakah ada bus dengan agency id tersebut
		if(buses.size() != 0) {
			//Jika agency id tersebut memiliki bus, kembalikan ok beserta daftar bus
			List<BusResponse> busResponses = new ArrayList<>();
			
			for(Bus bus: buses) {
				busResponses.add(new BusResponse(bus.getId(), bus.getCode(),
						bus.getCapacity(), bus.getMake(), bus.getAgency().getId()));
			}

			return ResponseEntity.status(HttpStatus.OK).body(busResponses);
		} else {
			//Jika tidak ada bus, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Agency tidak memiliki bus");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
	
	//Mengubah salah satu bus berdasarkan id
	@PutMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@Valid @RequestBody BusRequest busRequest, @PathVariable Long id) {

		//Ubah field sesuai tipe data di Java
		String code = busRequest.getCode();
		int capacity = busRequest.getCapacity();
		String make = busRequest.getMake();
		Long agencyId = busRequest.getAgencyId();

		//Coba mengambil data bus yang exist
		Bus bus = busServiceImpl.getBusById(id);
		
		//Periksa bus, jika hasilnya object bus dengan null, kembalikan not found
		if(bus.getId() == null) {
			Response errorResponse = new Response("404", "Not Found", "Data bus tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}

		//Jika ada field yang kosong atau 0, kembalikan bad request
		if(code == null || capacity == 0 || make == null || agencyId == null
				|| code.equals("") || make.equals("") || agencyId == 0) {

			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Get agency
		Agency agency = agencyServiceImpl.getAgencyById(agencyId);
		
		//Jika data agency tidak ditemukan, kembalikan bad request
		if(agency.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data agency tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Set kolom bus
		bus.setCode(code);
		bus.setCapacity(capacity);
		bus.setMake(make);
		bus.setAgency(agency);
		
		//Simpan bus baru ke database
		busServiceImpl.saveBus(bus);
		
		//Kembalikan response
		Response successResponse = new Response("200", "OK", "Bus berhasil diubah");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}

	//Menghapus satu bus berdasarkan id
	@DeleteMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteBus(@PathVariable Long id) {
		//Memeriksa apakah bus dengan id tersebut ada
		try {
			busServiceImpl.deleteBus(id);
			
			//Jika ada bus dengan id tersebut, kembalikan response ok
			Response successResponse = new Response("200", "OK", "Bus berhasil dihapus");
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		} catch (EmptyResultDataAccessException e) {
			//Jika bus dengan id tersebut tidak ada, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data bus tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
}