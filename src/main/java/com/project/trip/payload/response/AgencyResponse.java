package com.project.trip.payload.response;

//AgencyResponse untuk menampilkan response dari objek Agency ke dalam bentuk json
public class AgencyResponse {
	
	//Attribute-attribute
	private Long id;
	private String code;
	private String name;
	private String details;
	private Long ownerId;

	//Constructor kosong
	public AgencyResponse() {
	}
	
	//Constructor dengan parameter
	public AgencyResponse(Long id, String code, String name, String details, Long ownerId) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.details = details;
		this.ownerId = ownerId;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	//Getter
	public Long getId() {
		return id;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDetails() {
		return details;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}
}