package com.project.trip.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip.model.Role;
import com.project.trip.model.User;
import com.project.trip.payload.request.SignupRequest;
import com.project.trip.payload.request.UpdateUserRequest;
import com.project.trip.payload.request.UpdatePasswordRequest;
import com.project.trip.payload.response.Response;
import com.project.trip.repository.AgencyRepository;
import com.project.trip.repository.RoleRepository;
import com.project.trip.repository.UserRepository;
import com.project.trip.security.jwt.JwtUtils;
import com.project.trip.service.impl.UserServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

//Controller untuk user
@CrossOrigin(origins = "*", maxAge = 3600, methods = {RequestMethod.PUT, RequestMethod.POST, RequestMethod.GET })
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	AgencyRepository agencyRepository;

	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	JwtUtils jwtUtils;
	
	//Method untuk menghandle signup
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

		//Ubah field sesuai tipe data di Java
		String username = signupRequest.getUsername();
		String email = signupRequest.getEmail();
		String firstName = signupRequest.getFirstName();
		String lastName = signupRequest.getLastName();
		String mobileNumber = signupRequest.getMobileNumber();
		String password = signupRequest.getPassword();
		Set<String> strRoles = signupRequest.getRole();

		//Jika ada field yang kosong atau 0, kembalikan bad request
		if(username == null || email == null || firstName == null || lastName == null || mobileNumber == null
				|| password == null || strRoles == null || username.equals("") || email.equals("")
				|| firstName.equals("") || lastName.equals("") || strRoles.size() == 0) {
			
			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Memeriksa apakah username sudah terpakai
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			Response errorResponse = new Response("400", "Bad Request", "Username sudah terpakai");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		//Memeriksa apakah email sudah terpakai
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			Response errorResponse = new Response("400", "Bad Request", "Email sudah terpakai");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	
		//Membuat objek user
		User user = new User(signupRequest.getFirstName(), signupRequest.getLastName(),
				signupRequest.getMobileNumber(), signupRequest.getUsername(),
				signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));
	
		//Mengambil daftar roles
		Set<Role> roles = new HashSet<>();
	
		//Menerjemahkan input roles
		for(String item: strRoles) {
			if(item.equalsIgnoreCase("user") || item.equalsIgnoreCase("role_user")) {
				Role userRole = roleRepository.findById(1);
				roles.add(userRole);
			} else if(item.equalsIgnoreCase("admin") || item.equalsIgnoreCase("admin")) {
				Role adminRole = roleRepository.findById(2);
				roles.add(adminRole);
			} else {
				Response errorResponse = new Response("400", "Bad Request", "Role tidak dikenal");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
			}
		}
	
		//Menambahkan roles ke objek user
		user.setRoles(roles);
		
		//Menyimpan user baru
		userRepository.save(user);

		//Mengembalikan response
		Response successResponse = new Response("200", "OK", "User berhasil didaftarkan");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}
	
	//Method untuk menghandle update user
	@PutMapping("/update")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {

		String firstName = updateUserRequest.getFirstName();
		String lastName = updateUserRequest.getLastName();
		String mobileNumber = updateUserRequest.getMobileNumber();
		
		//Jika ada field yang kosong atau 0, kembalikan bad request
		if(firstName == null || lastName == null || mobileNumber == null ||
				firstName.equals("") || lastName.equals("") || mobileNumber.equals("")) {

			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Mengambil username dari user yang login
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
		String username = userDetails.getUsername();
		
		//Ambil data user
		User user = userServiceImpl.getUserByUsername(username);
		
		//Jika user tidak ditemukan, kembalikan response not found
		if (user == null) {
			Response errorResponse = new Response("404", "Not Found", "User tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		
		//Set firstName, lastName, dan mobileNumber ke objek user
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setMobileNumber(mobileNumber);
	
		//Update user ke database
		userRepository.save(user);

		//Mengembalikan response
		Response successResponse = new Response("200", "OK", "Data user berhasil diupdate");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}
	
	//Method untuk menghandle mengubah password
	@PutMapping("/update/password")
	@ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey") })
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

		String password = updatePasswordRequest.getPassword();
		
		//Jika ada field yang kosong atau 0, kembalikan bad request
		if(password == null || password.equals("")) {
			String errorDetails = "Pastikan field tidak kosong dan tidak bernilai 0 atau null atau string kosong";
			Response errorResponse = new Response("400", "Bad Request", errorDetails);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		
		//Mengambil username dari user yang login
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
		String username = userDetails.getUsername();

		//Ambil data user
		User user = userServiceImpl.getUserByUsername(username);

		//Jika user tidak ditemukan, kembalikan response not found
		if (user == null) {
			Response errorResponse = new Response("404", "Not Found", "Data user tidak ditemukan");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	
		//Set password
		user.setPassword(encoder.encode(password));
	
		//Menyimpan user ke database
		userRepository.save(user);

		//Mengembalikan response
		Response successResponse = new Response("200", "OK", "Password berhasil diubah");
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}
}
