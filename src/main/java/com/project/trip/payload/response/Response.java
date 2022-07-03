package com.project.trip.payload.response;

//Response untuk menampilkan response, untuk menampilkan pesan
public class Response {
	
	//Attribute-attribute
	private String code;
	private String status;
	private String details;
	
	//Constructor kosong
	public Response() {	
	}
	
	//Constructor dengan parameter
	public Response(String code, String status, String details) {
		this.code = code;
		this.status = status;
		this.details = details;
	}
	
	//Setter
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}
	
	//Getter
	public String getCode() {
		return code;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getDetails() {
		return details;
	}
}
