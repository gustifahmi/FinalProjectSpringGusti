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

import com.project.trip.model.Stop;
import com.project.trip.payload.request.StopRequest;
import com.project.trip.payload.response.StopResponse;
import com.project.trip.payload.response.Response;
import com.project.trip.service.impl.StopServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

//Controller untuk Stop
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/stop")
public class StopController {

	//Inisiasi stopServiceImpl
	@Autowired
	StopServiceImpl stopServiceImpl;

	//Menambah stop baru
	@PostMapping("")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createStop(@Valid @RequestBody StopRequest stopRequest) {
		
		//Ubah field sesuai tipe data di Java
		String code = stopRequest.getCode();
		String name = stopRequest.getName();
		String detail = stopRequest.getDetail();

		//Jika ada field yang kosong atau 0, kembalikan bad request
		if(code == null || name == null || detail == null || code.equals("")
				|| name.equals("") || detail.equals("")) {

			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
			
		//Create new stop object
		Stop stop = new Stop(code, name, detail);
		
		//Simpan stop baru ke database
		stopServiceImpl.saveStop(stop);
		
		//Kembalikan response
		Response successResponse = new Response("200", "OK", "Stop berhasil disimpan");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}
	
	//Mengambil semua stop yang tersimpan
	@GetMapping("")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getAllStop() {
		
		//Get all stop
		List<Stop> stops = stopServiceImpl.getAllStop();

		//Memeriksa apakah ada stop yang tersimpan
		if(stops.size() != 0) {
			//Jika ada stop yang tersimpan, kembalikan 200 ok beserta daftar stop
			List<StopResponse> stopResponses = new ArrayList<>();
			
			for (Stop stop : stops) {
				stopResponses.add(new StopResponse(stop.getId(), stop.getCode(), stop.getName(), stop.getDetail()));
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(stopResponses);
		} else {
			//Jika tidak ada stop tersimpan, maka response statusnya not found
			Response errorResponse = new Response("404", "Not Found", "Tidak ada stop yang tersimpan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	//Mengambil satu stop berdasarkan id
	@GetMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getStopById(@PathVariable Long id) {
		
		//Get stop
		Stop stop = stopServiceImpl.getStopById(id);
		
		//Periksa stop
		if(stop.getId() != null) {
			//Jika stop bukan object berisi null value, kembali response 200 ok beserta stop
			StopResponse stopResponse = new StopResponse(stop.getId(), stop.getCode(), stop.getName(),
					stop.getDetail());
		
			return ResponseEntity.status(HttpStatus.OK).body(stopResponse);
		} else {
			//Jika tidak ada stop dengan id tersebut, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data stop tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
	
	//Mengubah salah satu stop berdasarkan id
	@PutMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@Valid @RequestBody StopRequest stopRequest, @PathVariable Long id) {

		//Ubah field sesuai tipe data di Java
		String code = stopRequest.getCode();
		String name = stopRequest.getName();
		String detail = stopRequest.getDetail();

		//Coba mengambil data stop yang exist
		Stop stop = stopServiceImpl.getStopById(id);
		
		//Periksa stop, jika hasilnya object stop dengan null, kembalikan not found
		if(stop.getId() == null) {
			Response errorResponse = new Response("404", "Not Found", "Data stop tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}

		//Jika ada field yang kosong atau 0, kembalikan bad request
		if(code == null || name == null || detail == null || code.equals("")
				|| name.equals("") || detail.equals("")) {

			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
			
		//Set kolom stop
		stop.setCode(code);
		stop.setName(name);
		stop.setDetail(detail);
		
		//Simpan stop baru ke database
		stopServiceImpl.saveStop(stop);
		
		//Kembalikan response
		Response successResponse = new Response("200", "OK", "Stop berhasil diubah");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}

	//Menghapus satu stop berdasarkan id
	@DeleteMapping("/{id}")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteStop(@PathVariable Long id) {
		//Memeriksa apakah stop dengan id tersebut ada
		try {
			stopServiceImpl.deleteStop(id);
			
			//Jika ada stop dengan id tersebut, kembalikan response ok
			Response successResponse = new Response("200", "OK", "Stop berhasil dihapus");
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		} catch (EmptyResultDataAccessException e) {
			//Jika Stop dengan id tersebut tidak ada, kembalikan not found
			Response errorResponse = new Response("404", "Not Found", "Data stop tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
}