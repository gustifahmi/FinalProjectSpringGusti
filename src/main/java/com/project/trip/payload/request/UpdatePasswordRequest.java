package com.project.trip.payload.request;

import io.swagger.annotations.ApiModelProperty;

//UserPasswordRequest, untuk menghandle request body password
public class UpdatePasswordRequest {
	
	//Attribute-attribute
	@ApiModelProperty(hidden = true)
	private Long id;

	private String password;

	//Constructor kosong
	public UpdatePasswordRequest() {
	}

	//Constructor dengan parameter
	public UpdatePasswordRequest(String password) {
		this.password = password;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	//Getter
	public Long getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}
}