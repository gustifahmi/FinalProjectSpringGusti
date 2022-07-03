package com.project.trip.payload.request;

import javax.validation.constraints.NotBlank;

//LoginRequest, untuk menghandle request body untuk login/autentikasi
public class LoginRequest {
	
	//Attribute-attribute
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	//Constructor kosong
	public LoginRequest() {
	}
	
	//Constructor dengan parameter
	public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	//Setter
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	//Getter
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}