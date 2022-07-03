package com.project.trip.payload.response;

import java.util.List;

//JwtResponse untuk menampilkan response dari objek Jwt(setelah autentikasi) ke dalam bentuk json
public class JwtResponse {
	private String token;
	private String type = "Bearer ";
	private Long id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private List<String> roles;

	public JwtResponse() {
	}

	public JwtResponse(String jwt, Long id, String username, String email, String firstName, String lastName,
			String mobileNumber, List<String> roles) {
		this.token = type + jwt;
		this.id = id;
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.roles = roles;
	}
	
	//Setter
	public void setToken(String token) {
		this.token = token;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	//Getter
	public String getToken() {
		return token;
	}
	
	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public List<String> getRoles() {
		return roles;
	}
}