package com.project.trip.payload.response;

//BusResponse untuk menampilkan response dari objek Bus ke dalam bentuk json
public class BusResponse {
	
	//Attribute-attribute
	private Long id;
	private String code;
	private int capacity;
	private String make;
	private Long agencyId;

	//Constructor kosong
	public BusResponse() {
	}
	
	//Constructor dengan parameter
	public BusResponse(Long id, String code, int capacity, String make, Long agencyId) {
		this.id = id;
		this.code = code;
		this.capacity = capacity;
		this.make = make;
		this.agencyId = agencyId;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}

	public void setCode(String code) {
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