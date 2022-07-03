package com.project.trip.payload.request;

import io.swagger.annotations.ApiModelProperty;

//BusRequest, untuk menghandle request body Bus
public class BusRequest {
	
	//Attribute-attribute
	@ApiModelProperty(hidden = true)
	private Long id;
	
	private String code;
	private int capacity;
	private String make;
	private Long agencyId;

	//Constructor kosong
	public BusRequest() {
	}
	
	//Constructor dengan parameter
	public BusRequest(String code, int capacity, String make, Long agencyId) {
		this.code = code;
		this.capacity = capacity;
		this.make = make;
		this.agencyId = agencyId;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}

	public void setcode(String code) {
		this.code = code;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public void setAgencyId(Long agencyId) {
		this.agencyId = agencyId;
	}

	//Getter
	public Long getId() {
		return id;
	}
	
	public String getCode() {
		return code;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public String getMake() {
		return make;
	}
	
	public Long getAgencyId() {
		return agencyId;
	}
}