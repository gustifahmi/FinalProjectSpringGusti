package com.project.trip.controller.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip.payload.request.LoginRequest;
import com.project.trip.payload.response.JwtResponse;
import com.project.trip.repository.RoleRepository;
import com.project.trip.repository.UserRepository;
import com.project.trip.security.jwt.JwtUtils;
import com.project.trip.security.service.UserDetailsImpl;

//Controller untuk autentikasi
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	//Method untuk menghandle autentikasi
	@PostMapping("/auth")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		//Authenticate
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		//Set Authentication
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		//Generate token
		String jwt = jwtUtils.generateJwtToken(authentication);

		//Buat objek userDetails
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		//Buat List roles
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		//Kembalikan response ok
		JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
				userDetails.getEmail(), userDetails.getFirstName(), userDetails.getLastName(),
				userDetails.getMobileNumber(), roles);
		return ResponseEntity.status(HttpStatus.OK).body(jwtResponse);
	}
}