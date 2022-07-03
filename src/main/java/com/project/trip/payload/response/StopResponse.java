package com.project.trip.payload.response;

//StopResponse untuk menampilkan response dari objek Stop ke dalam bentuk json
public class StopResponse {
	
	//Attribute-attribute
	private Long id;
	private String code;
	private String name;
	private String detail;
	
	//Constructor kosong
	public StopResponse() {
	}
	
	//Constructor dengan parameter
	public StopResponse(Long id, String code, String name, String detail) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.detail = detail;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}

	public void setcode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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
	
	public String getDetail() {
		return detail;
	}
	
}