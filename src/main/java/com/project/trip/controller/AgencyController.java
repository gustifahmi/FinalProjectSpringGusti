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
import com.project.trip.model.User;
import com.project.trip.payload.request.AgencyRequest;
import com.project.trip.payload.response.AgencyResponse;
import com.project.trip.payload.response.Response;
import com.project.trip.service.impl.AgencyServiceImpl;
import com.project.trip.service.impl.UserServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

//Controller untuk agency
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/agency")
public class AgencyController {

	//Inisiasi agencyServiceImpl
	@Autowired
	AgencyServiceImpl agencyServiceImpl;

	//Inisiasi userServiceImpl
	@Autowired
	UserServiceImpl userServiceImpl;

	//Menambah agency baru
	@PostMapping("")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createAgency(@Valid @RequestBody AgencyRequest agencyRequest) {
		
		//Ubah field sesuai tipe data di Java
		String code = agencyRequest.getCode();
		String name = agencyRequest.getName();
		String details = agencyRequest.getDetails();
		Long ownerId = agencyRequest.getOwnerId();

		//Jika ada field yang kosong atau 0, kembalikan bad request
		if(code == null || name == null || details == null || ownerId == null
				|| code.equals("") || name.equals("") || details.equals("") || ownerId == 0) {

			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Get owner
		User owner = userServiceImpl.getUserById(ownerId);
		
		//Jika data owner tidak ditemukan, kembalikan bad request
		if(owner.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data owner tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Create new agency object
		Agency agency = new Agency(code, name, details, owner);
		
		//Simpan agency baru ke database
		agencyServiceImpl.saveAgency(agency);
		
		//Kembalikan response
		Response successResponse = new Response("200", "OK", "Agency berhasil disimpan");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}
	
	//Mengambil semua agency yang tersimpan
	@GetMapping("")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getAllAgency() {
		
		//Get all Agency
		List<Agency> agencies = agencyServiceImpl.getAllAgency();

		//Memeriksa apakah ada agency yang tersimpan
		if(agencies.size() != 0) {
			//Jika ada agency yang tersimpan, kembalikan 200 ok beserta daftar Agency
			List<AgencyResponse> agencyResponses = new ArrayList<>();
			
			for (Agency agency : agencies) {
				agencyResponses.add(new AgencyResponse(agency.getId(), agency.getCode(), agency.getName(),
						agency.getDetails(), agency.getOwner().getId()));
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(agencyResponses);
		} else {
			//Jika tidak ada Agency tersimpan, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Tidak ada agency yang tersimpan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	//Mengambil satu agency berdasarkan id
	@GetMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getAgencyById(@PathVariable Long id) {
		
		//Get agency
		Agency agency = agencyServiceImpl.getAgencyById(id);
		
		//Periksa agency
		if(agency.getId() != null) {
			//Jika agency bukan object berisi null value, kembali response 200 ok beserta agency
			AgencyResponse agencyResponse = new AgencyResponse(agency.getId(), agency.getCode(),
					agency.getName(), agency.getDetails(), agency.getOwner().getId());
		
			return ResponseEntity.status(HttpStatus.OK).body(agencyResponse);
		} else {
			//Jika tidak ada agency dengan id tersebut, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data agency tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
	
	//Mengambil semua agency yang dimiliki seorang owner
	@GetMapping("/owner/{ownerId}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAgencyByOwnerId(@PathVariable(value = "ownerId") Long ownerId) {
		
		//Jika ownerId tidak valid, kembalikan not found
		if(userServiceImpl.getUserById(ownerId).getId() != ownerId) {
			Response errorResponse = new Response("404", "Not Found", "Data owner tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		
		//Get all agency berdasarkan owner id
		List<Agency> agencies = agencyServiceImpl.getAllAgencyByOwnerId(ownerId);
		
		//Cek apakah ada agency dengan owner id tersebut
		if(agencies.size() != 0) {
			//Jika owner id tersebut memiliki agency, kembalikan ok beserta daftar Agency
			List<AgencyResponse> agencyResponses = new ArrayList<>();
			
			for(Agency agency: agencies) {
				agencyResponses.add(new AgencyResponse(agency.getId(), agency.getCode(),
						agency.getName(), agency.getDetails(), agency.getOwner().getId()));
			}

			return ResponseEntity.status(HttpStatus.OK).body(agencyResponses);
		} else {
			//Jika tidak ada Agency, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "User tidak memiliki agency");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
	
	//Mengubah salah satu agency berdasarkan id
	@PutMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> update(@Valid @RequestBody AgencyRequest agencyRequest, @PathVariable Long id) {
		
		//Ubah field sesuai tipe data di java
		String code = agencyRequest.getCode();
		String name = agencyRequest.getName();
		String details = agencyRequest.getDetails();
		Long ownerId = agencyRequest.getOwnerId();
		
		Agency agency = agencyServiceImpl.getAgencyById(id);
		
		//Periksa agency, jika hasilnya object bus dengan value null, kembalikan not found
		if(agency.getId() == null) {
			Response errorResponse = new Response("404", "Not Found", "Data agency tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}

		//Jika ada field yang kosong atau 0, kembalikan bad request
		if(code == null || name == null || details == null || ownerId == null
				|| code.equals("") || name.equals("") || details.equals("") || ownerId == 0) {

			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Get owner
		User owner = userServiceImpl.getUserById(ownerId);
		
		//Jika data owner tidak ditemukan, kembalikan bad request
		if(owner.getId() == null) {
			Response errorResponse = new Response("400", "Bad Request", "Data owner tidak ditemukan");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Set kolom agency
		agency.setCode(code);
		agency.setName(name);
		agency.setDetails(details);
		agency.setOwner(owner);
		
		//Simpan agency baru ke database
		agencyServiceImpl.saveAgency(agency);
		
		//Kembalikan response
		Response successResponse = new Response("200", "OK", "Agency berhasil diubah");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}

	//Menghapus satu agency berdasarkan id
	@DeleteMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteAgency(@PathVariable Long id) {
		//Memeriksa apakah agency dengan id tersebut ada
		try {
			agencyServiceImpl.deleteAgency(id);
			
			//Jika ada agency dengan id tersebut, kembalikan response ok
			Response successResponse = new Response("200", "OK", "Agency berhasil dihapus");
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		} catch (EmptyResultDataAccessException e) {
			//Jika agency dengan id tersebut tidak ada, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data agency tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
}